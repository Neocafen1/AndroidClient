package com.example.neocafeteae1prototype.view.bottom_navigation_items.user.user_shopping_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.databinding.FragmentReceiptDetailBinding
import com.example.neocafeteae1prototype.view.adapters.MainRecyclerAdapter
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.tools.alert_dialog.CustomAlertDialog
import com.example.neocafeteae1prototype.view.tools.alert_dialog.DoneAlertDialog
import com.example.neocafeteae1prototype.view.tools.navigate
import com.example.neocafeteae1prototype.view.tools.setSafeOnClickListener
import com.example.neocafeteae1prototype.view_model.user_shopping_history_vm.UserShoppingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class ReceiptDetailFragment : BaseFragment<FragmentReceiptDetailBinding>() {
    private val recyclerAdapter by lazy { MainRecyclerAdapter(null) }
    private val safeArgs: ReceiptDetailFragmentArgs by navArgs()
    private val viewModel: UserShoppingViewModel by activityViewModels()
    private val bottomNavigation by lazy {activity?.findViewById(R.id.bottomNavigationView) as BottomNavigationView }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi(safeArgs.receiptModel)
        setUpRecyclerView()
        binding.repeatOrder.setSafeOnClickListener {
            CustomAlertDialog(this::repeatOrder, "Вы точно хотите повторить заказ?", null).show(childFragmentManager, "TAG")
        }

        with(viewModel){
            isProductListSent.observe(viewLifecycleOwner){result ->
                if(result == true) {
                    DoneAlertDialog("Ваш заказ оформлен") { navController.navigateUp() }.show(childFragmentManager, "TAG")
                    viewModel.isProductListSent.postValue(false)
                    viewModel.isUserHaveTable.postValue(null)
                }
            }
            isUserHaveTable.observe(viewLifecycleOwner){
                it?.let {
                    if (!it.have_table){
                        bottomNavigation.selectedItemId = R.id.qr_nav_graph
                    }else{
                        sendProductList(safeArgs.receiptModel.details.orderItems, 0)
                    }
                }
            }
        }
    }

    private fun repeatOrder() {
        viewModel.checkIsUserHaveTable()
    }

    private fun setUpRecyclerView() {
        binding.recycler.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        recyclerAdapter.setList(safeArgs.receiptModel.details.orderItems)
    }

    private fun setUpUi(receiptModel: AllModels.Receipt) {
        with(binding) {
            time.text = receiptModel.time
            date.text = receiptModel.date
            address.text = receiptModel.details.filial
            totalPrice.text = "${receiptModel.finalPrice} c"
        }
    }

    override fun setUpToolbar() {
        with(binding.include){
            backButton.setOnClickListener { navController.navigateUp() }
            notification.setOnClickListener { navigate(ReceiptDetailFragmentDirections.actionReceiptDetailFragmentToNotification5()) }
            textView.text = resources.getText(R.string.history)
        }
    }
    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentReceiptDetailBinding {
        return FragmentReceiptDetailBinding.inflate(inflater)
    }
}