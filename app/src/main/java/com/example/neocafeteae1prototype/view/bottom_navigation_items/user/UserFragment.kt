package com.example.neocafeteae1prototype.view.bottom_navigation_items.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.navigation.fragment.findNavController
import com.example.neocafeteae1prototype.data.Consts
import com.example.neocafeteae1prototype.databinding.FragmentUserBinding
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.root.BaseFragmentWithErrorLiveData
import com.example.neocafeteae1prototype.view.tools.alert_dialog.CustomAlertDialog
import com.example.neocafeteae1prototype.view.tools.notVisible
import com.example.neocafeteae1prototype.view_model.user_vm.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserFragment : BaseFragmentWithErrorLiveData<FragmentUserBinding>() {

    private val viewModel by viewModels<UserViewModel>()
    private val nav by lazy { findNavController() }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getUserData()
        setUpButtonsListener()
    }

    private fun getUserData() {
        with(viewModel) {
            getUserInfo()
            userData.observe(viewLifecycleOwner) {
                with(binding) {
                    numberPhoneTextView.text = it.number.toString()
                    birthdayEditText.text = it.birthDate
                    userNameTextView.text = it.first_name
                    nameEditText.setText(it.first_name)
                    progress.notVisible()
                }
            }

            viewModel.bonus.observe(viewLifecycleOwner) {
                binding.bonusResult.text = it.toString()
            }
        }
    }

    private fun deleteAccount() {
        FirebaseAuth.getInstance().signOut()
        activity?.finish()
    }

    private fun setUpButtonsListener() {
        with(binding) {
            nameEditText.addTextChangedListener { // Изменяет его имя и отправляет данные в бэк
                userNameTextView.text = it.toString()
                viewModel.changeUserName(it.toString())
            }
            exit.setOnClickListener {
                CustomAlertDialog(this@UserFragment::deleteAccount, "Вы точно хотите выйти из аккаунта?", "Для обратной регистрации зайдите в приложение")
                    .show(childFragmentManager, "TAG")
            }
        }
        binding.shoppingHistory.setOnClickListener { nav.navigate(UserFragmentDirections.actionUserFragmentToUserShoppingHistory())
        }
    }

    override fun errorListener(): LiveData<Boolean> = viewModel.errorLiveData

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentUserBinding {
        return FragmentUserBinding.inflate(inflater)
    }

    override fun setUpToolbar() {
        binding.include.notification.setOnClickListener { nav.navigate(UserFragmentDirections.actionUserFragmentToNotification5()) }
    }
}

