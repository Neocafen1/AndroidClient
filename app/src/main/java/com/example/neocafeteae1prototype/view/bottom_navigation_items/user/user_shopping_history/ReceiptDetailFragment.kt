package com.example.neocafeteae1prototype.view.bottom_navigation_items.user.user_shopping_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.databinding.FragmentReceiptDetailBinding
import com.example.neocafeteae1prototype.view.adapters.MainRecyclerAdapter
import com.example.neocafeteae1prototype.view.root.BaseFragment


class ReceiptDetailFragment : BaseFragment<FragmentReceiptDetailBinding>() {
    private val recyclerAdapter by lazy { MainRecyclerAdapter(null) }
    private val safeArgs: ReceiptDetailFragmentArgs by navArgs()
    private val nav by lazy {findNavController()}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi(safeArgs.receiptModel)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.recycler.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        recyclerAdapter.setList(safeArgs.receiptModel.list)
    }

    private fun setUpUi(receiptModel: AllModels.Receipt) {
        with(binding){
            time.text = receiptModel.time
            address.text = receiptModel.street
            totalPrice.text = receiptModel.total
        }
    }

    override fun setUpToolbar() {
        with(binding.include){
            backButton.setOnClickListener { nav.navigateUp() }
            notification.setOnClickListener { nav.navigate(ReceiptDetailFragmentDirections.actionReceiptDetailFragmentToNotification5()) }
            textView.text = resources.getText(R.string.history)
        }
    }
    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentReceiptDetailBinding {
        return FragmentReceiptDetailBinding.inflate(inflater)
    }

    override fun setUpButtonsListener() {
        binding.repeatOrder.setOnClickListener {  }
    }
}