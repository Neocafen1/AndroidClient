package com.example.neocafeteae1prototype.view.tools.bottom_sheet

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.databinding.FragmentProductBinding
import com.example.neocafeteae1prototype.view.tools.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog

class ProductModalSheet(private val model: AllModels.Popular,private val function: () -> Unit) : BaseBottomSheet<FragmentProductBinding>() {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = BottomSheetDialog(requireContext(), theme)
        dialog.setOnShowListener {

            val bottomSheetDialog = it as BottomSheetDialog
            val parentLayout =
                bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            parentLayout?.let { it ->
                val behaviour = BottomSheetBehavior.from(it)
                setupFullHeight(it)
                behaviour.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
        return dialog
    }

    private fun setupFullHeight(bottomSheet: View) {
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT
        bottomSheet.layoutParams = layoutParams
    }


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        changeVisibility(model.county)
        binding.quantity.text = model.county.toString()
    }

    private fun changeVisibility(county: Int) {
        with(binding) {
            val views = listOf<View>(addButton, removeButton, quantity, addElement)
            if (county > 0) {
                add.notVisible()
                views.forEach { it.visible() }
            } else if (county <= 0) {
                add.visible()
                views.forEach { it.notVisible() }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setUpUI() {
        printTotalPrice()
        binding.close.setSafeOnClickListener { dismiss() }

        with(model) {

            binding.productImage.loadWithGlide(image)

            binding.productName.text = title
            binding.productPrice.text = "$price c"
            binding.description.text = description

            binding.add.setOnClickListener {
                if (county == 0){
                    county++
                    printTotalPrice()
                    binding.quantity.text = county.toString()
                    function()
                }
                changeVisibility(county)
            }

            binding.addButton.setOnClickListener {
                this.county += 1
                printTotalPrice()
                binding.quantity.text = this.county.toString()
                function()
            }
            binding.removeButton.setOnClickListener {
                this.county -= 1
                binding.quantity.text = this.county.toString()
                if (county == 0){
                    changeVisibility(county)
                }
                printTotalPrice()
                function()
            }
            binding.addElement.setSafeOnClickListener {
                dismiss()
            }
        }
    }

    private fun printTotalPrice(){
        binding.totalPrice.text = "${model.county * model.price}c"
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentProductBinding {
        return FragmentProductBinding.inflate(inflater, container, false)
    }
}