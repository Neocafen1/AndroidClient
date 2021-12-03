package com.example.neocafeteae1prototype.view.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.neocafeteae1prototype.data.internet_checker.ConnectionLiveData
import com.example.neocafeteae1prototype.databinding.FragmentSignInOrRegistrationBinding
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.tools.logging
import com.example.neocafeteae1prototype.view.tools.navigate


class SignInOrRegistrationFragment : BaseFragment<FragmentSignInOrRegistrationBinding>() {

    private lateinit var connectionLiveData: ConnectionLiveData

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            signIn.setOnClickListener{
                navigate(SignInOrRegistrationFragmentDirections.actionSignInOrRegistrationFragmentToAuthWithNumberFragment())
            }
            register.setOnClickListener{
                navigate(SignInOrRegistrationFragmentDirections.actionSignInOrRegistrationFragmentToRegistrationNumberFirebaseFragment())
            }
        }
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentSignInOrRegistrationBinding {
        return FragmentSignInOrRegistrationBinding.inflate(inflater)
    }

    override fun setUpToolbar() {
        "SignInOrRegistrationFragment".logging()
    }

}