package com.example.neocafeteae1prototype.view.bottom_navigation_items.home.popular_products

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.databinding.FragmentPopularBinding
import com.example.neocafeteae1prototype.view.adapters.ProductRecyclerAdapter
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.tools.bottom_sheet.ProductModalSheet
import com.example.neocafeteae1prototype.view.tools.delegates.SecondItemClickListener
import com.example.neocafeteae1prototype.view.tools.navigate
import com.example.neocafeteae1prototype.view_model.menu_shopping_vm.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PopularFragment : BaseFragment<FragmentPopularBinding>(), SecondItemClickListener {

    private val viewModel: SharedViewModel by activityViewModels()
    private val recyclerAdapter by lazy { ProductRecyclerAdapter(this) }

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
        viewModel.productList.observe(viewLifecycleOwner){
            recyclerAdapter.setList(viewModel.getPopularProduct(it))
        }
    }

    override fun holderClicked(model: AllModels?, position:Int) {
        ProductModalSheet(model!! as AllModels.Popular) { recyclerDataChanged(position) }.show(childFragmentManager, "TAG")
    }

    //Когда в Modal Sheet изменится данные то оно срабатывает
    private fun recyclerDataChanged(position: Int){
        recyclerAdapter.notifyItemChanged(position)
    }

    override fun setUpToolbar() {
        with(binding.include){
            textView.text = resources.getText(R.string.popular)
            backButton.setOnClickListener { navController.navigateUp() }
            notification.setOnClickListener { navigate(PopularFragmentDirections.actionPopularFragmentToNotification()) }
        }
    }
}