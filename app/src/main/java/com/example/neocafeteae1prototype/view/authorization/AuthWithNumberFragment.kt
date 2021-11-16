package com.example.neocafeteae1prototype.view.authorization

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.local.LocalDatabase
import com.example.neocafeteae1prototype.databinding.FragmentAuthWithNumberBinding
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.tools.logging
import com.example.neocafeteae1prototype.view.tools.showToast
import com.example.neocafeteae1prototype.view_model.regisration_vm.RegistrationViewModel
import com.vicmikhailau.maskededittext.MaskedFormatter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthWithNumberFragment : BaseFragment<FragmentAuthWithNumberBinding>() {

    private val viewModel by activityViewModels<RegistrationViewModel>()
    @Inject lateinit var localDatabase: LocalDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.numberPhoneTextView.addTextChangedListener {
            if (it?.length == 11) {
                binding.nextButton.apply {
                    background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.button_enable_custom_style
                    )
                    isEnabled = true
                }
            } else {
                binding.nextButton.apply {
                    background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.button_disable_custom_item
                    )
                    isEnabled = false
                }
            }
        }

        binding.nextButton.setOnClickListener { sendMessage() }

    }

    private fun sendMessage() {
        val phoneNumber = MaskedFormatter("###-###-###").formatString(binding.numberPhoneTextView.text.toString())!!.unMaskedString.toInt()
        viewModel.checkNumber(phoneNumber)
        viewModel.isNumberLocateInDB.observe(viewLifecycleOwner){
            if (it){
                localDatabase.saveUserNumber(phoneNumber)
                findNavController().navigate(AuthWithNumberFragmentDirections.actionAuthWithNumberFragmentToGetMessageAuthorization(phoneNumber))
            }else{
                "Пожалуйста, проверьте ваш номер".showToast(requireContext(), Toast.LENGTH_LONG)
            }
        }
    }

    override fun setUpToolbar() {
        "AuthWithNumberFragmentToolbar".logging()
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentAuthWithNumberBinding {
        return FragmentAuthWithNumberBinding.inflate(inflater)
    }
}