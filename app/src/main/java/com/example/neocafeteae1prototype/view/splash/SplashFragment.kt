package com.example.neocafeteae1prototype.view.splash

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.internet_checker.ConnectionLiveData
import com.example.neocafeteae1prototype.data.local.LocalDatabase
import com.example.neocafeteae1prototype.databinding.FragmentSplashBinding
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.tools.firebaseLogging
import com.example.neocafeteae1prototype.view.tools.logging
import com.example.neocafeteae1prototype.view_model.main_vm.MainViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessaging
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

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            task.addOnSuccessListener {
                it.firebaseLogging()
            }

            // Get new FCM registration token
            val token = task.result
            token?.firebaseLogging()
        })
    }

    private fun nextFragment() {
//        connectionLiveData.observe(viewLifecycleOwner) {
//            if (it) {
//                if (FirebaseAuth.getInstance().uid == null) {
//                    navController.navigate(SplashFragmentDirections.actionSplashFragmentToSignInOrRegistrationFragment())
//                } else {
//                    getToken()
//                }
//            }
//
//        }


        getToken()
    }

    private fun getToken() {
        val uid = FirebaseAuth.getInstance().uid
//        viewModel.JWTtoken(localDatabase.fetchUserNumber(), uid!!)
        viewModel.JWTtoken(222222227, "test")
        viewModel.list.observe(viewLifecycleOwner) {
            localDatabase.saveRefreshToken(it.refresh)
            localDatabase.saveAccessToken(it.access)
            goToNextFragment()
        }
    }

    private fun goToNextFragment() {
        if (localDatabase.fetchAccessToken() != null){
            navController.navigate(R.id.action_splashFragment_to_bottomViewFragment3)
        }
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentSplashBinding {
        return FragmentSplashBinding.inflate(inflater)
    }

    override fun setUpToolbar() {
        "SplashFragmentToolbar".logging()
    }
}