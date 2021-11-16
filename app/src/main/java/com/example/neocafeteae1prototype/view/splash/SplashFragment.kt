package com.example.neocafeteae1prototype.view.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.internet_checker.ConnectionLiveData
import com.example.neocafeteae1prototype.data.local.LocalDatabase
import com.example.neocafeteae1prototype.databinding.FragmentSplashBinding
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.tools.logging
import com.example.neocafeteae1prototype.view_model.main_vm.MainViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    private val viewModel by viewModels<MainViewModel>()
    private lateinit var connectionLiveData: ConnectionLiveData
    @Inject lateinit var localDatabase: LocalDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        connectionLiveData = ConnectionLiveData(requireContext()) // Слушаетель интернета
        nextFragment()
    }

    private fun nextFragment() {
        connectionLiveData.observe(viewLifecycleOwner) {
            if (it) {
                if (FirebaseAuth.getInstance().uid == null) {
                    findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToSignInOrRegistrationFragment())
                } else {
                    getToken()
                }
            }

        }
    }

    private fun getToken() {
        val uid = FirebaseAuth.getInstance().uid
        viewModel.JWTtoken(localDatabase.fetchUserNumber(), uid!!)
        viewModel.list.observe(viewLifecycleOwner) {
            localDatabase.saveRefreshToken(it.refresh)
            localDatabase.saveAccessToken(it.access)
            goToNextFragment()
        }

    }

    private fun goToNextFragment() {
        if (localDatabase.fetchAccessToken() != null){
            findNavController().navigate(R.id.action_splashFragment_to_bottomViewFragment3)
        }
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater)
    }

    override fun setUpToolbar() {
        "SplashFragmentToolbar".logging()
    }
}