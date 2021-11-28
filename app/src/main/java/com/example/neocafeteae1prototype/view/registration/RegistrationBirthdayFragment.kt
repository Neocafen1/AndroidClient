package com.example.neocafeteae1prototype.view.registration

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.local.LocalDatabase
import com.example.neocafeteae1prototype.databinding.FragmentRegistrationBirthdayBinding
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.tools.logging
import com.example.neocafeteae1prototype.view.tools.showToast
import com.example.neocafeteae1prototype.view.tools.visible
import com.example.neocafeteae1prototype.view_model.regisration_vm.RegistrationViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class RegistrationBirthdayFragment : BaseFragment<FragmentRegistrationBirthdayBinding>() {
    private val viewModel by activityViewModels<RegistrationViewModel>()
    @Inject lateinit var sharedPreferences:LocalDatabase


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
//            skip.setOnClickListener { sendUserData() }
            signIn.setOnClickListener {
                sendUserData()
            }
            editText.addTextChangedListener {
                if (it?.length == 10){
                    binding.signIn.apply{
                        background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.button_enable_custom_style
                        )
                        isEnabled = true
                    }
                }else{
                    binding.signIn.apply {
                        background = ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.button_disable_custom_item
                        )
                        isEnabled = false
                    }
                }
            }
            }
    }

    private fun sendUserData(){
        val name = sharedPreferences.fetchUserName()
        val number = sharedPreferences.fetchUserNumber()
        val uid = FirebaseAuth.getInstance().uid
        val birthday = binding.editText.text.toString()

        binding.progress.visible()
        viewModel.sendUserData( number, uid!!, name ?: "N/A", birthday)

        viewModel.userCreated.observe(viewLifecycleOwner){
           if (it){
               getToken(uid, number)
               navController.navigate(RegistrationBirthdayFragmentDirections.actionRegistrationBirthdayFragmentToBottomViewFragment3())
           }else "Такой юзер существует".showToast(requireContext(), Toast.LENGTH_LONG)
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun getToken(uid: String, number: Int) {
        viewModel.JWTtoken(number, uid)
        viewModel.tokens.observe(viewLifecycleOwner){
            sharedPreferences.saveAccessToken(it.access)
            sharedPreferences.saveRefreshToken(it.refresh)
        }
    }

    override fun setUpToolbar() {
        "RegistrationBirthdayToolbar".logging()
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup? ): FragmentRegistrationBirthdayBinding {
        return FragmentRegistrationBirthdayBinding.inflate(inflater)
    }
}