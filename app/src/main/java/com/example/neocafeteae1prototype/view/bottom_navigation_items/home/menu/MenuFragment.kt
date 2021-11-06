package com.example.neocafeteae1prototype.view.bottom_navigation_items.home.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.data.models.Resource
import com.example.neocafeteae1prototype.databinding.FragmentMenuBinding
import com.example.neocafeteae1prototype.view.adapters.MenuRecyclerAdapter
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.tools.bottom_sheet.ProductModalSheet
import com.example.neocafeteae1prototype.view.tools.delegates.RecyclerItemClickListener
import com.example.neocafeteae1prototype.view_model.menu_shopping_vm.SharedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MenuFragment : BaseFragment<FragmentMenuBinding>(), RecyclerItemClickListener {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val recyclerAdapter by lazy { MenuRecyclerAdapter(this) }
    private val args: MenuFragmentArgs by navArgs()
    private val mapOfCategory = mutableMapOf<String, Int>(
        "Выпечка" to R.id.bakery, "Кофе" to R.id.coffee, "Чай" to R.id.tea,
        "Напитки" to R.id.drinks, "Десерты" to R.id.desserts
    )


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        binding.recycler.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = recyclerAdapter
        }

        val viewId = mapOfCategory[args.category]
        if (viewId != null) {
            binding.chipGroup.check(viewId)
            recyclerSetList(viewId)
        }

        binding.chipGroup.setOnCheckedChangeListener { group, checkedId ->
            recyclerSetList(checkedId)
        }
    }


    private fun recyclerSetList(checkedId: Int) {
        val checkedText = when (checkedId) {
            R.id.bakery -> "Выпечка"
            R.id.coffee -> "Кофе"
            R.id.tea -> "Чай"
            R.id.drinks -> "Напитки"
            R.id.desserts -> "Десерты"
            R.id.all -> "Все"
            else -> ""
        }
        sharedViewModel.list.observe(viewLifecycleOwner, {
            when (it){
                is Resource.Success -> {
                    val sorted = sharedViewModel.sort(checkedText, it.value)
                    recyclerAdapter.setList(sorted, checkedText)
                }
            }
        })
    }

    override fun setUpToolbar() {
        val navController = findNavController()
        with(binding.include) {
            textView.text = resources.getText(R.string.Menu)
            backButton.setOnClickListener { navController.navigateUp() }
            notification.setOnClickListener {
                navController.navigate(MenuFragmentDirections.actionMenuFragmentToNotification())
            }
        }
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentMenuBinding {
        return FragmentMenuBinding.inflate(inflater)
    }

    override fun itemClicked(item: AllModels?) {
        ProductModalSheet(item as AllModels.Popular).show(childFragmentManager, "TAG")
    }

}