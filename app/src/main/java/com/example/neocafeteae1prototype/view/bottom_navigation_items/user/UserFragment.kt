package com.example.neocafeteae1prototype.view.bottom_navigation_items.user

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.neocafeteae1prototype.data.Consts
import com.example.neocafeteae1prototype.data.models.Resource
import com.example.neocafeteae1prototype.databinding.FragmentUserBinding
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.tools.alert_dialog.CustomAlertDialog
import com.example.neocafeteae1prototype.view.tools.bearerToken
import com.example.neocafeteae1prototype.view.tools.notVisible
import com.example.neocafeteae1prototype.view.tools.visible
import com.example.neocafeteae1prototype.view_model.user_vm.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : BaseFragment<FragmentUserBinding>() {

    private val viewModel by viewModels<UserViewModel>()
    private val token by lazy {sharedPreferences.getString(Consts.ACCESS, "null") ?: "U don't have access token"}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserData()
        with(binding){
            shoppingHistory.setOnClickListener { findNavController().navigate(UserFragmentDirections.actionUserFragmentToUserShoppingHistory()) }
            userNameTextView.text = sharedPreferences.getString(Consts.USER_NAME,"")
            nameEditText.setText(sharedPreferences.getString(Consts.USER_NAME, ""))
            nameEditText.addTextChangedListener {
                userNameTextView.text = it.toString()
                viewModel.changeUserName(token, it.toString())

            }
            exit.setOnClickListener {
                CustomAlertDialog(this@UserFragment::deleteAccount, "Вы точно хотите выйти из аккаунта?", "Для обратной регистрации зайдите в приложение")
                    .show(childFragmentManager, "TAG")

            }
        }
    }

    private fun getUserData() {
        viewModel.getUserInfo(token)
        viewModel.userData.observe(viewLifecycleOwner) {
            with(binding) {
                when(it){
                    is Resource.Success -> { it.value
                        numberPhoneTextView.text = it.value.number.toString()
                        birthdayEditText.text = it.value.birthDate
                        userNameTextView.text = it.value.first_name
                        nameEditText.setText(it.value.first_name)
                        progress.notVisible()
                        bonusResult.text = viewModel.bonus.toString()
                    }
                    is Resource.Loading -> {
                        progress.visible()
                    }
                }

            }
        }
    }

    private fun deleteAccount(){
        FirebaseAuth.getInstance().signOut()
        activity?.finish()
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentUserBinding {
        return FragmentUserBinding.inflate(inflater)
    }

    override fun setUpToolbar() {
        binding.include.notification.setOnClickListener {
            findNavController().navigate(
                UserFragmentDirections.actionUserFragmentToNotification5()
            )
        }
    }

}