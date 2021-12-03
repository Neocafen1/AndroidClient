package com.example.neocafeteae1prototype.view.bottom_navigation_items.shopping

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.databinding.FragmentShoppingOrderBinding
import com.example.neocafeteae1prototype.view.adapters.MainRecyclerAdapter
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.tools.alert_dialog.DoneAlertDialog
import com.example.neocafeteae1prototype.view.tools.navigate
import com.example.neocafeteae1prototype.view.tools.notVisible
import com.example.neocafeteae1prototype.view.tools.setSafeOnClickListener
import com.example.neocafeteae1prototype.view.tools.visible
import com.example.neocafeteae1prototype.view_model.menu_shopping_vm.SharedViewModel
import com.example.neocafeteae1prototype.view_model.menu_shopping_vm.ShoppingOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ShoppingOrderFragment : BaseFragment<FragmentShoppingOrderBinding>() {

    private val args by navArgs<ShoppingOrderFragmentArgs>()
    private var totalPrice = 0
    private val recyclerAdapter by lazy {MainRecyclerAdapter(null)}
    private val viewModel by viewModels<ShoppingOrderViewModel>()
    private val shaveViewModel by activityViewModels<SharedViewModel>()


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.isProductListSent.postValue(false)
        setUpRecycler()
        setDate()
        viewModel.getProductList(args.shoppingList.products)
        args.shoppingList.products.forEach {
            totalPrice += it.price * it.county
        }
        with(binding) {
            sum.text = "$totalPrice c"
            bonus.text = args.bonus.toString()
            result.text = "${totalPrice.minus(args.bonus)} c"
            takeOrder.setSafeOnClickListener { sendProducts() }
            viewModel.isProductListSent.observe(viewLifecycleOwner) {
                it?.let {
                    if (it) {
                        shaveViewModel.deleteAllQuantity()
                        binding.progress.notVisible()
                        DoneAlertDialog("Ваш заказ оформлен") { navController.navigateUp() }.show(
                            childFragmentManager,
                            "TAG"
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setDate() {
        val calendar = Calendar.getInstance().time
        binding.apply {
            date.text = (SimpleDateFormat(getString(R.string.date_format)).format(calendar))
            time.text = (SimpleDateFormat(getString(R.string.hour_format)).format(calendar))
        }
    }

    private fun sendProducts() {
        viewModel.sendProductList(args.bonus, args.inCafe)
        binding.progress.visible()
    }

    private fun setUpRecycler() {
        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }
        recyclerAdapter.setList(viewModel.productList)
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentShoppingOrderBinding {
        return FragmentShoppingOrderBinding.inflate(inflater)
    }

    override fun setUpToolbar() {
        with(binding.include){
            backButton.setOnClickListener{navController.navigateUp()}
            notification.setOnClickListener { navigate(ShoppingOrderFragmentDirections.actionShoppingOrderFragment2ToNotification3()) }
        }
    }
}