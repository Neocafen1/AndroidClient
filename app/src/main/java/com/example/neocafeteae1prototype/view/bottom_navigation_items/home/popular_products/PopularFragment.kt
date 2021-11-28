package com.example.neocafeteae1prototype.view.bottom_navigation_items.home.popular_products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.databinding.FragmentPopularBinding
import com.example.neocafeteae1prototype.view.adapters.ProductRecyclerAdapter
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view_model.menu_shopping_vm.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularFragment : BaseFragment<FragmentPopularBinding>() {

    private val viewModel: SharedViewModel by activityViewModels()
    private val recyclerAdapter by lazy { ProductRecyclerAdapter(null) }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentPopularBinding {
        return FragmentPopularBinding.inflate(inflater)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.recycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = recyclerAdapter
        }
        recyclerAdapter.setList(viewModel.popularList)
    }



    override fun setUpToolbar() {
        with(binding.include){
            textView.text = resources.getText(R.string.popular)
            backButton.setOnClickListener { navController.navigateUp() }
            notification.setOnClickListener { navController.navigate(PopularFragmentDirections.actionPopularFragmentToNotification()) }
        }
    }
}