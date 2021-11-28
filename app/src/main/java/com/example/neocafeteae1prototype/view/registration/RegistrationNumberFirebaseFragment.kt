package com.example.neocafeteae1prototype.view.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.navigation.fragment.findNavController
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.local.LocalDatabase
import com.example.neocafeteae1prototype.databinding.FragmentRegistrationNumberFirebaseBinding
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.tools.logging
import com.vicmikhailau.maskededittext.MaskedFormatter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationNumberFirebaseFragment : BaseFragment<FragmentRegistrationNumberFirebaseBinding>() {

    @Inject lateinit var sharedPreferences: LocalDatabase

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        editTextChangeListener()
        binding.nextButton.setOnClickListener { sendMessage() }
    }

    private fun editTextChangeListener() {
        binding.numberPhoneTextView.addTextChangedListener {
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
        val name = binding.name.text.toString()
        val unMaskedNumber = MaskedFormatter("###-###-###").formatString(binding.numberPhoneTextView.text.toString())?.unMaskedString
        sharedPreferences.saveUserNumber(unMaskedNumber.toString().toInt())
        sharedPreferences.saveUserName(name)
        navController.navigate(
            RegistrationNumberFirebaseFragmentDirections.actionRegistrationNumberFirebaseFragmentToReceiveMessageFirebaseFragment(unMaskedNumber.toString())
        )
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?, ): FragmentRegistrationNumberFirebaseBinding {
        return FragmentRegistrationNumberFirebaseBinding.inflate(inflater)
    }

    override fun setUpToolbar() {
        "RegistrationNumberToolbar".logging()
    }
}