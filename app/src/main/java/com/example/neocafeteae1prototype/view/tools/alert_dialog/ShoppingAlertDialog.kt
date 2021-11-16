package com.example.neocafeteae1prototype.view.tools.alert_dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.databinding.AlertDialogLayoutBinding
import kotlin.reflect.KFunction1


class ShoppingAlertDialog(
    var function: () -> Unit,
    val function2: (Int) -> Unit,
    var title: String?,
    var message: String?
) : BaseAlertDialog<AlertDialogLayoutBinding>() {

    override fun inflateView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): AlertDialogLayoutBinding {
        return AlertDialogLayoutBinding.inflate(inflater)
    }

    override fun onResume() {
        super.onResume()
        val width = resources.getDimensionPixelSize(R.dimen.choose_alert_width)
        val height = resources.getDimensionPixelSize(R.dimen.choose_gender_height)
        dialog?.window?.setLayout(width, height)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (title != null) {
            binding.title.text = title
        }
        if (message != null) {
            binding.message.text = message
        }

        binding.positiveButton.setOnClickListener {
            function()
        }

        binding.negativeButton.setOnClickListener {
            function2(0)
        }
    }
}