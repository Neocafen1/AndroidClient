package com.example.neocafeteae1prototype.view.tools.bottom_sheet

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.widget.addTextChangedListener
import com.example.neocafeteae1prototype.databinding.BonusAlertDialogItemBinding
import com.example.neocafeteae1prototype.view.tools.notVisible
import com.example.neocafeteae1prototype.view.tools.visible
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class BonusBottomSheet(val bonus: Int,val function: (Int) -> Unit) : BaseBottomSheet<BonusAlertDialogItemBinding>() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        editTextChangeListener()
        binding.currentBonus.text = bonus.toString()
        binding.ready.setOnClickListener {
            function(binding.bonusEditText.text.toString().toInt())

        }
    }

    private fun editTextChangeListener() {
        binding.bonusEditText.addTextChangedListener {
            if(it.toString() != ""){
                if (it.toString().toInt() > bonus){
                    binding.alertMessag.visible()
                }else {
                    binding.alertMessag.notVisible()
                }
            }
        }
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): BonusAlertDialogItemBinding {
        return BonusAlertDialogItemBinding.inflate(inflater)
    }
}