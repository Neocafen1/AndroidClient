package com.example.neocafeteae1prototype.view.authorization

import `in`.aabhasjindal.otptextview.OTPListener
import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.Consts
import com.example.neocafeteae1prototype.data.models.Resource
import com.example.neocafeteae1prototype.databinding.FragmentGetMessageAuthorizationBinding
import com.example.neocafeteae1prototype.view.registration.ReceiveMessageFirebaseFragmentDirections
import com.example.neocafeteae1prototype.view.tools.notVisible
import com.example.neocafeteae1prototype.view.tools.showSnackBar
import com.example.neocafeteae1prototype.view.tools.showToast
import com.example.neocafeteae1prototype.view.tools.visible
import com.example.neocafeteae1prototype.view_model.regisration_vm.RegistrationViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class GetMessageAuthorization : Fragment() {

    private var _binding: FragmentGetMessageAuthorizationBinding? = null
    private val viewModel by activityViewModels<RegistrationViewModel>()
    private lateinit var phoneNumber: String
    private var id: String = ""
    private val args: GetMessageAuthorizationArgs by navArgs()
    private lateinit var sharedPref: SharedPreferences
    private lateinit var firebaseCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGetMessageAuthorizationBinding.inflate(inflater, container, false)
        phoneNumber = args.phoneNumber
        sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)!!

        _binding?.registrationButton?.setOnClickListener {
            sendMessage()
            startTimer()
        }

        firebaseCallback = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                signIn(p0)
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                _binding?.let {
                    p0.message?.showSnackBar(
                        it.registrationButton,
                        Snackbar.LENGTH_LONG
                    )
                }
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                id = p0
                "Сообщение отправлено. Ждите".showToast(requireContext(), Toast.LENGTH_LONG)
            }
        }
        sendMessage()
        listenOTR()
        _binding?.appCompatButton?.setOnClickListener {
            _binding!!.registrationButton.isEnabled = false
            _binding!!.registrationButton.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.button_disable_custom_item)
        }

        return _binding?.root
    }

    private fun sendMessage() {
        PhoneAuthOptions.newBuilder()
            .setActivity(requireActivity())
            .setPhoneNumber("+996$phoneNumber")
            .setTimeout(30L, TimeUnit.SECONDS)
            .setCallbacks(firebaseCallback)
            .build()
            .apply {
                PhoneAuthProvider.verifyPhoneNumber(this)
            }
    }

    private fun listenOTR() {
        _binding?.otpTextView?.otpListener = object : OTPListener {
            override fun onInteractionListener() {
                _binding?.appCompatButton?.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.button_disable_custom_item
                )
            }

            override fun onOTPComplete(otp: String) {
                _binding?.appCompatButton?.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.button_enable_custom_style
                )
                _binding?.appCompatButton?.isEnabled = true
                signInWithCredential()
            }
        }
    }

    private fun signInWithCredential() {
        val phoneAuthCredential = PhoneAuthProvider.getCredential(id, _binding?.otpTextView?.otp!!)
        FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(ReceiveMessageFirebaseFragmentDirections.actionReceiveMessageFirebaseFragmentToRegistrationBirthdayFragment())
                }
            }
    }

    private fun startTimer() {
        object : CountDownTimer(30000, 1000) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millisUntilFinished: Long) {
                _binding?.registrationButton?.isEnabled = false
                _binding?.registrationButton?.text =
                    "Отправить повторно ${millisUntilFinished / 1000}"
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                _binding?.registrationButton?.text = "Отправить повторно"
                _binding?.registrationButton?.isEnabled = true
                _binding?.registrationButton?.background = ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.button_enable_custom_style
                )
            }
        }.start()
    }

    private fun signIn(phone: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phone).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _binding?.progress?.visible()
                _binding?.otpTextView?.setOTP(phone.smsCode!!)
                val uid = FirebaseAuth.getInstance().uid
                val number = sharedPref.getString(Consts.USER_NUMBER, "0")?.toInt()
                viewModel.JWTtoken(number!!, uid!!)
                viewModel.tokens.observe(viewLifecycleOwner){
                    listenUserInfo(it.access)
                }
            }
        }
    }

    @SuppressLint("CommitPrefEdits")
    private fun listenUserInfo(access: String) {
        viewModel.getUserInfo(access)
        viewModel.userInfo.observe(viewLifecycleOwner){
            when(it){
                is Resource.Success -> {
                    _binding?.progress?.notVisible()
                    sharedPref.edit().putString(Consts.USER_NAME, it.value.first_name)
                    findNavController().navigate(GetMessageAuthorizationDirections.actionGetMessageAuthorizationToBottomViewFragment3())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}