package com.example.neocafeteae1prototype.view.bottom_navigation_items.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.databinding.FragmentHomeBinding
import com.example.neocafeteae1prototype.view.adapters.MainRecyclerAdapter
import com.example.neocafeteae1prototype.view.adapters.ProductRecyclerAdapter
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.tools.bottom_sheet.ProductModalSheet
import com.example.neocafeteae1prototype.view.tools.delegates.RecyclerItemClickListener
import com.example.neocafeteae1prototype.view.tools.delegates.SecondItemClickListener
import com.example.neocafeteae1prototype.view.tools.notVisible
import com.example.neocafeteae1prototype.view_model.menu_shopping_vm.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>(), RecyclerItemClickListener,
    SecondItemClickListener {

    private var recyclerPosition = 0
    private val shareViewModel: SharedViewModel by activityViewModels()
    private val popularAdapter by lazy { ProductRecyclerAdapter(this) } // Для продуктоа категории популярное
    private val mainAdapter by lazy { MainRecyclerAdapter(this) } // Для категории меню

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecyclers()
        setUpButtonsListener()
        with(shareViewModel){
            getUserInfo()
            userData.observe(viewLifecycleOwner){
                binding.userName.text = "Привет ${it.first_name}"
            }
        }
    }

    private fun setUpRecyclers() {
        binding.menuRecycler.apply {
            adapter = mainAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
        }
        mainAdapter.setList(shareViewModel.menuList)

        binding.popularRecycler.apply {
            adapter = popularAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }
        shareViewModel.productList.observe(viewLifecycleOwner) {
            shareViewModel.getPopularProduct(it)
            popularAdapter.setList(shareViewModel.popularList)
            binding.progress.notVisible()
        }
    }

    override fun itemClicked(item: AllModels?) {
        val category = item as AllModels.Menu
        navController.navigate(HomeFragmentDirections.actionHomeFragmentToMenuFragment(category.name))
    }

    override fun holderClicked(model: AllModels?, position:Int) {
        ProductModalSheet(model!! as AllModels.Popular) { recyclerDataChanged(position) }.show(childFragmentManager, "TAG")
    }

    //Когда в Modal Sheet изменится данные то оно срабатывает
    private fun recyclerDataChanged(position: Int){
        popularAdapter.notifyItemChanged(position)
    }



    private fun setUpButtonsListener() {
        binding.all.setOnClickListener { navController.navigate(HomeFragmentDirections.actionHomeFragmentToPopularFragment()) }
    }

    override fun setUpToolbar() {
        with(binding) {
            notificationIcon.setOnClickListener { navController.navigate(HomeFragmentDirections.actionHomeFragmentToNotification()) }
        }
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater)
    }
}