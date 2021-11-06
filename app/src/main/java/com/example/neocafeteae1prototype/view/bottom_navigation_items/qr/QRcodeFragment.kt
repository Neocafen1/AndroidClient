package com.example.neocafeteae1prototype.view.bottom_navigation_items.qr

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.neocafeteae1prototype.data.Consts
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.data.models.Resource
import com.example.neocafeteae1prototype.databinding.FragmentQrcodeBinding
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.tools.alert_dialog.CustomAlertDialog
import com.example.neocafeteae1prototype.view.tools.alert_dialog.DoneAlertDialog
import com.example.neocafeteae1prototype.view.tools.showSnackBar
import com.example.neocafeteae1prototype.view.tools.showToast
import com.example.neocafeteae1prototype.view_model.qr_vm.QRViewModel
import com.google.android.material.snackbar.Snackbar
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class QRcodeFragment : BaseFragment<FragmentQrcodeBinding>() {

    private val viewModel by viewModels<QRViewModel>()
    private lateinit var scanner: CodeScanner
    private lateinit var table:AllModels.Table
    private val token by lazy {sharedPreferences.getString(Consts.ACCESS, "0")}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanner = CodeScanner(requireContext(), binding.scannerView)

        Dexter.withContext(context) // Dexter библиотека которая помогает работать с permisssions
            .withPermission(Manifest.permission.CAMERA)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {
                    QRcodeChecker() // Если дано разрешение оно срабатывает
                    scanner.startPreview()
                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    requestPermission()
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ContextCompat.checkSelfPermission(
                                requireContext(),
                                Manifest.permission.CAMERA
                            )
                            != PackageManager.PERMISSION_GRANTED // идет двойная проверка я хз на что
                        ) {

                        }
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?,
                ) {

                }

            }).check()

    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.CAMERA), Consts.PERMISSION_REQUEST_CODE)
    }

    private fun QRcodeChecker() {

        scanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS
            autoFocusMode = AutoFocusMode.CONTINUOUS
            scanMode = ScanMode.SINGLE
            isAutoFocusEnabled = true
            isFlashEnabled = false
        }

        scanner.decodeCallback = DecodeCallback { result ->
            requireActivity().runOnUiThread {
                checkTable(result.text)
            }
        }
    }

    private fun checkTable(result: String) {
        viewModel.checkTable(result, token!!)
        viewModel.table.observe(viewLifecycleOwner){ // Проверяю чтобы не ввел ли юзер совсем другой QR
            when(it){
                is Resource.Success -> checkTableIsFree(it.value)
                is Resource.Failure -> "Неправильный QR Code".showToast(requireContext(), Toast.LENGTH_LONG)
            }
        }

    }

    @SuppressLint("CommitPrefEdits")
    private fun checkTableIsFree(value: AllModels.Table) {
        if (value.status){
            table = value
            sharedPreferences.edit().putInt(Consts.TABLE, value.id)
            CustomAlertDialog(this::lockTable, "Стол свободен", "Забранировать?").show(childFragmentManager, "TAG")
        }else{
            "Стол занят".showSnackBar(binding.scannerView, Snackbar.LENGTH_LONG)
        }
    }

    private fun lockTable(){
        viewModel.lockTable(table.qrCode, token!!)
        viewModel.lockedTable.observe(viewLifecycleOwner){
            DoneAlertDialog("Стол забронирован").show(childFragmentManager, "TAG")
        }
    }

    override fun setUpToolbar() {
        binding.include.notification.setOnClickListener { findNavController().navigate(
            QRcodeFragmentDirections.actionQRcodeFragmentToNotification4()
        ) }
    }


    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentQrcodeBinding {
        return FragmentQrcodeBinding.inflate(inflater)
    }
}