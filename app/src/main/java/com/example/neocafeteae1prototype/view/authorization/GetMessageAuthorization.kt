package com.example.neocafeteae1prototype.view.authorization

import `in`.aabhasjindal.otptextview.OTPListener
import android.annotation.SuppressLint
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.local.LocalDatabase
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.databinding.FragmentGetMessageAuthorizationBinding
import com.example.neocafeteae1prototype.view.registration.RegistrationBirthdayFragmentDirections
import com.example.neocafeteae1prototype.view.tools.navigate
import com.example.neocafeteae1prototype.view.tools.showSnackBar
import com.example.neocafeteae1prototype.view.tools.showToast
import com.example.neocafeteae1prototype.view.tools.visible
import com.example.neocafeteae1prototype.view_model.regisration_vm.RegistrationViewModel
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class GetMessageAuthorization : Fragment() {

    private var _binding: FragmentGetMessageAuthorizationBinding? = null
    private val viewModel by activityViewModels<RegistrationViewModel>()
    private var phoneNumber: Int = 0
    private var id: String = ""
    private val args: GetMessageAuthorizationArgs by navArgs()

    @Inject lateinit var localDatabase: LocalDatabase

    private lateinit var firebaseCallback: PhoneAuthProvider.OnVerificationStateChangedCallbacks

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGetMessageAuthorizationBinding.inflate(inflater, container, false)
        phoneNumber = args.phoneNumber
        startTimer()

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
                    getData()
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

    @SuppressLint("CommitPrefEdits")
    private fun signIn(phone: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(phone).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                _binding?.otpTextView?.setOTP(phone.smsCode!!)
                getData()
            }
        }
    }

    private fun getData(){
        _binding?.progress?.visible()
        val uid = FirebaseAuth.getInstance().uid
        viewModel.get_jwt_token(phoneNumber, uid!!)
        viewModel.tokens.observe(viewLifecycleOwner){
            localDatabase.saveAccessToken(it.access)
            localDatabase.saveRefreshToken(it.refresh)
            saveFCM()
        }
    }

    private fun saveFCM() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("TAG", "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            task.addOnSuccessListener {
                viewModel.saveFCM(AllModels.FCM_token(it, "client"))
                viewModel.isFcmSaved.observe(viewLifecycleOwner){
                    if (it){
                        navigate(GetMessageAuthorizationDirections.actionGetMessageAuthorizationToBottomViewFragment3())
                    }
                }
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}