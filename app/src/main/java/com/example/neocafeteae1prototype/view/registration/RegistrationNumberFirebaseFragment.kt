package com.example.neocafeteae1prototype.view.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.local.LocalDatabase
import com.example.neocafeteae1prototype.databinding.FragmentRegistrationNumberFirebaseBinding
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.tools.*
import com.example.neocafeteae1prototype.view_model.regisration_vm.RegistrationViewModel
import com.vicmikhailau.maskededittext.MaskedFormatter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationNumberFirebaseFragment : BaseFragment<FragmentRegistrationNumberFirebaseBinding>() {

    @Inject lateinit var sharedPreferences: LocalDatabase
    private val viewModel by activityViewModels<RegistrationViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextChangeListener()
        binding.nextButton.setOnClickListener { sendMessage() }
        viewModel.isNumberLocateInDB.observe(viewLifecycleOwner){
            it?.let {
                binding.progress.notVisible()
                if (!it){
                    val name = binding.name.text.toString()
                    val unMaskedNumber = MaskedFormatter("###-###-###").formatString(binding.numberPhoneTextView.text.toString())?.unMaskedString?.toInt()
                    sharedPreferences.saveUserNumber(unMaskedNumber)
                    sharedPreferences.saveUserName(name)
                    viewModel.isNumberLocateInDB.postValue(null)
                    navigate(RegistrationNumberFirebaseFragmentDirections.actionRegistrationNumberFirebaseFragmentToReceiveMessageFirebaseFragment(unMaskedNumber.toString()))
                }else if(!it){
                    "Вы уше проходили регистрацию. Пожалуйста пройдите авторизацию".showRedSnackbar(binding.cardView)
                }
            }
        }
    }

    private fun editTextChangeListener() {
        binding.numberPhoneTextView.addTextChangedListener {
            viewModel.isNumberLocateInDB.postValue(null)
            if (it?.length == 11 && binding.name.text.isNotEmpty()) {
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
    }

    private fun sendMessage() {
        binding.progress.visible()
        val unMaskedNumber = MaskedFormatter("###-###-###").formatString(binding.numberPhoneTextView.text.toString())?.unMaskedString?.toInt()
        viewModel.checkNumber(unMaskedNumber ?: 0)

    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?, ): FragmentRegistrationNumberFirebaseBinding {
        return FragmentRegistrationNumberFirebaseBinding.inflate(inflater)
    }

    override fun setUpToolbar() {
        "RegistrationNumberToolbar".logging()
    }
}