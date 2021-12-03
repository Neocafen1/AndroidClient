package com.example.neocafeteae1prototype.view.tools.alert_dialog

import android.graphics.Color
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.databinding.DoneAlertDialogItemBinding


class DoneAlertDialog(private val title: String,private val function: () -> Unit, ) : BaseAlertDialog<DoneAlertDialogItemBinding>() {

    private lateinit var avd:AnimatedVectorDrawableCompat
    private lateinit var avd2:AnimatedVectorDrawable

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.done_alert_dialog_width)
        val height = resources.getDimensionPixelSize(R.dimen.done_alert_dialog_height)
        dialog?.window?.setLayout(width, height)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val drawable = binding.done.drawable
        binding.title.text = title

        if (drawable is AnimatedVectorDrawableCompat){
            avd = drawable as AnimatedVectorDrawableCompat
            avd.start()
        }else if(drawable is AnimatedVectorDrawable){
            avd2 = drawable as AnimatedVectorDrawable
            avd2.start()
        }

        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                function()
            }
        }.start()

    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): DoneAlertDialogItemBinding {
        return DoneAlertDialogItemBinding.inflate(inflater)
    }
}