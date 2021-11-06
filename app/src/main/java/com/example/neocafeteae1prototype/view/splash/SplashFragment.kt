package com.example.neocafeteae1prototype.view.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.Consts
import com.example.neocafeteae1prototype.data.internet_checker.ConnectionLiveData
import com.example.neocafeteae1prototype.databinding.FragmentSplashBinding
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.tools.*
import com.example.neocafeteae1prototype.view_model.main_vm.MainViewModel
import com.example.neocafeteae1prototype.view_model.regisration_vm.RegistrationViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var connectionLiveData: ConnectionLiveData
    private lateinit var sharedPref:SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectionLiveData = ConnectionLiveData(requireContext()) // Слушаетель интернета
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)!!
        nextFragment()
    }

    private fun nextFragment() {
        connectionLiveData.observe(viewLifecycleOwner){
            if (it){
                if (FirebaseAuth.getInstance().currentUser == null) {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToSignInOrRegistrationFragment())
                } else {
                    getToken()
                }
            }
        }

    }

    @SuppressLint("CommitPrefEdits")
    private fun getToken() {
        val uid = FirebaseAuth.getInstance().uid
        val number = sharedPref.getString(Consts.USER_NUMBER, "0")?.toInt()

        viewModel.JWTtoken(number!!, uid!!)
        viewModel.list.observe(viewLifecycleOwner){
            if (it.access.isNotEmpty()){
                binding.progress.notVisible()
                with(sharedPref.edit()){
                    putString(Consts.ACCESS, it.access)
                    putString(Consts.REFRESH, it.refresh)
                }.apply()
                findNavController().navigate(R.id.action_splashFragment_to_bottomViewFragment3)
            }
        }
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater)
    }

    override fun setUpToolbar() {
        "SplashFragmentToolbar".logging()
    }
}