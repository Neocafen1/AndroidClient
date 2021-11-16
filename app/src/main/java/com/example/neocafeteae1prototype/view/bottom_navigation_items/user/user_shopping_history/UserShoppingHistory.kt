package com.example.neocafeteae1prototype.view.bottom_navigation_items.user.user_shopping_history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.adapters.MainRecyclerAdapter
import com.example.neocafeteae1prototype.view.tools.alert_dialog.CustomAlertDialog
import com.example.neocafeteae1prototype.view.tools.delegates.RecyclerItemClickListener
import com.example.neocafeteae1prototype.databinding.FragmentUserShoppingHistoryBinding
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.view_model.user_shopping_history_vm.UserShoppingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserShoppingHistory : BaseFragment<FragmentUserShoppingHistoryBinding>(), RecyclerItemClickListener {

    private val recyclerAdapter by lazy { MainRecyclerAdapter(this) }
    private val viewModel: UserShoppingViewModel by viewModels()
    private val bottomNavigationView by lazy {activity?.findViewById(R.id.bottomNavigationView) as BottomNavigationView}

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycler()
        setUpButtonsListener()
    }

    private fun setUpRecycler() {
        binding.recyclerView.apply {
            adapter = recyclerAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        if (viewModel.list.isEmpty()){
            updateVisibility(View.GONE, View.VISIBLE)
        }else{
            recyclerAdapter.setList(viewModel.list)
        }
    }


    private fun showAlertDialog() {
        CustomAlertDialog(this::clearAllReceipt, null, "Вы правда хотите очистить историю заказ?"
        ).show(childFragmentManager, "TAG")
    }

    override fun itemClicked(item: AllModels?) {
        findNavController().navigate(UserShoppingHistoryDirections.actionUserShoppingHistoryToReceiptDetailFragment(item as AllModels.Receipt))
    }

    private fun clearAllReceipt() {
        viewModel.list.clear()
        updateVisibility(View.VISIBLE, View.GONE)
    }

    private fun updateVisibility(isEmptyViewsVisibility: Int, defaultViewsVisibility:Int){
        with(binding){
            imageView2.visibility = isEmptyViewsVisibility
            textView5.visibility = isEmptyViewsVisibility
            goToMenuButton.visibility = isEmptyViewsVisibility
            recyclerView.visibility = defaultViewsVisibility
            clearReceipt.visibility = defaultViewsVisibility
        }
    }

    override fun setUpToolbar() {
        with(binding.include){
            textView.text = resources.getText(R.string.history)
            backButton.setOnClickListener { findNavController().navigateUp() }
            notification.setOnClickListener { findNavController().navigate(UserShoppingHistoryDirections.actionUserShoppingHistoryToNotification5()) }
        }
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentUserShoppingHistoryBinding {
        return FragmentUserShoppingHistoryBinding.inflate(inflater)
    }

    private fun setUpButtonsListener() {
        with(binding){
            clearReceipt.setOnClickListener { showAlertDialog() }
            bottomNavigationView.setOnClickListener{bottomNavigationView.selectedItemId = R.id.home_nav_graph}
        }
    }
}