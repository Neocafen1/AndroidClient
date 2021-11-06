package com.example.neocafeteae1prototype.view.registration

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
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
import com.example.neocafeteae1prototype.data.Consts
import com.example.neocafeteae1prototype.databinding.FragmentRegistrationBirthdayBinding
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.tools.logging
import com.example.neocafeteae1prototype.view.tools.showToast
import com.example.neocafeteae1prototype.view.tools.visible
import com.example.neocafeteae1prototype.view_model.regisration_vm.RegistrationViewModel
import com.google.firebase.auth.FirebaseAuth
import com.vicmikhailau.maskededittext.MaskedFormatter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationBirthdayFragment : BaseFragment<FragmentRegistrationBirthdayBinding>() {
    private val viewModel by activityViewModels<RegistrationViewModel>()
    private lateinit var birthdayDate: String
    private lateinit var sharedPref:SharedPreferences

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with(binding){
//            skip.setOnClickListener { sendUserData() }
            signIn.setOnClickListener {
                val formatter = MaskedFormatter("###-###-###").formatString(binding.editText.text.toString())?.unMaskedString
                insertDataToSharedPreference(formatter!!)
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

    private fun insertDataToSharedPreference(birthday: String){
        with(sharedPref.edit()) {
            putString(Consts.USER_BIRTHDAY, birthday)
            apply()
        }

    }

    private fun sendUserData(){
        val name = sharedPref.getString(Consts.USER_NAME, "null")
        val number = sharedPref.getString(Consts.USER_NUMBER, "0")?.toInt()
        val uid = FirebaseAuth.getInstance().uid
        val birthday = binding.editText.text.toString() ?: "null"

        binding.progress.visible()
        viewModel.sendUserData( number!!.toInt(), uid!!, name!!, birthday)

        viewModel.userCreated.observe(viewLifecycleOwner){
           if (it){
               getToken(uid, number)
               findNavController().navigate(RegistrationBirthdayFragmentDirections.actionRegistrationBirthdayFragmentToBottomViewFragment3())
           }else "Такой юзер существует".showToast(requireContext(), Toast.LENGTH_LONG)
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun getToken(uid: String, number: Int) {
        viewModel.JWTtoken(number, uid)
        viewModel.tokens.observe(viewLifecycleOwner){
            with(sharedPref.edit()){
                putString(Consts.ACCESS, it.access)
                putString(Consts.REFRESH, it.refresh)
            }
        }
    }

    override fun setUpToolbar() {
        "RegistrationBirthdayToolbar".logging()
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?, ): FragmentRegistrationBirthdayBinding {
        return FragmentRegistrationBirthdayBinding.inflate(inflater)
    }
}