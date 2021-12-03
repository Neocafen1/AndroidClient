package com.example.neocafeteae1prototype.view.bottom_navigation_items.qr

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.Consts
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.data.models.Resource
import com.example.neocafeteae1prototype.databinding.FragmentQrcodeBinding
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.tools.alert_dialog.CustomAlertDialog
import com.example.neocafeteae1prototype.view.tools.alert_dialog.DoneAlertDialog
import com.example.neocafeteae1prototype.view.tools.navigate
import com.example.neocafeteae1prototype.view.tools.showRedSnackbar
import com.example.neocafeteae1prototype.view.tools.visible
import com.example.neocafeteae1prototype.view_model.qr_vm.QRViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
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
    private val bottomNavigation by lazy { activity?.findViewById(R.id.bottomNavigationView) as BottomNavigationView }
    private var tableQr = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        scanner = CodeScanner(requireContext(), binding.scannerView)
        checkCameraPermissions()
        checkIsUserHaveTable()
        binding.goToMenu.setOnClickListener {
            bottomNavigation.selectedItemId = R.id.home_nav_graph
        }

        viewModel.lockedTable.observe(viewLifecycleOwner) {
            it?.let {
                if (it){
                    checkIsUserHaveTable()
                    DoneAlertDialog("Стол забронирован") { }.show(childFragmentManager, "TAG")
                    viewModel.lockedTable.postValue(null)
                }
            }
        }

        viewModel.table.observe(viewLifecycleOwner) {
            it?.let {
                when (it) {
                    is Resource.Failure -> {
                        "Введите верные данные".showRedSnackbar(binding.scannerView)
                        viewModel.table.postValue(null)
                    }
                    is Resource.Success -> {
                        if (it.value?.user == null) {
                            CustomAlertDialog({ lockTable(it.value) }, "Стол свободен", "Забронировать?").show(childFragmentManager, "TAG")
                        } else {
                            "Стол занят".showRedSnackbar(binding.scannerView)
                        }
                        viewModel.table.postValue(null)
                    }
                }
            }
        }
    }

    private fun checkCameraPermissions() {
        Dexter.withContext(context) // Dexter библиотека которая помогает работать с permissions
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

    // Метод проверяет есть ли у юзера забронированный стол если false то у него столов нет
    private fun checkIsUserHaveTable() {
        viewModel.checkIsUserHaveTable()
        viewModel.isUserHaveTable.observe(viewLifecycleOwner) {
            if (it.have_table == false) { // если у него не стола
                binding.apply {
                    scannerView.visible()
                    textView.visible()
                }
            } else if(it.have_table){
                binding.apply {
                    title.text = "Стол №${it.table} в филиале ${it.filial}"
                    title.visible()
                    message.visible()
                    goToMenu.visible()
                }
            }
        }
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.CAMERA),
            Consts.PERMISSION_REQUEST_CODE
        )
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
                tableQr = result.text
                checkTableIsFree()
            }
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun checkTableIsFree() {
        viewModel.checkTable(tableQr) // Проверка не забронирован ли за этот стол
    }

    private fun lockTable(value: AllModels.Table?) {
        viewModel.lockTable(value?.qrCode ?: "filial")
    }

    override fun setUpToolbar() {
        with(binding.include) {
            notification.setOnClickListener { navigate(QRcodeFragmentDirections.actionQRcodeFragmentToNotification4()) }
            textView.text = "QR код"
        }
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentQrcodeBinding {
        return FragmentQrcodeBinding.inflate(inflater)
    }
}