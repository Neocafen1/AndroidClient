package com.example.neocafeteae1prototype.view.bottom_navigation_items.shopping

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.databinding.FragmentShoppingBinding
import com.example.neocafeteae1prototype.view.adapters.ShoppingRecyclerAdapter
import com.example.neocafeteae1prototype.view.root.BaseFragment
import com.example.neocafeteae1prototype.view.tools.*
import com.example.neocafeteae1prototype.view.tools.alert_dialog.ShoppingAlertDialog
import com.example.neocafeteae1prototype.view.tools.bottom_sheet.BonusBottomSheet
import com.example.neocafeteae1prototype.view.tools.delegates.RecyclerItemClickListener
import com.example.neocafeteae1prototype.view.tools.delegates.SecondItemClickListener
import com.example.neocafeteae1prototype.view_model.menu_shopping_vm.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShoppingFragment : BaseFragment<FragmentShoppingBinding>(), RecyclerItemClickListener ,SecondItemClickListener{

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private val bonus by lazy {sharedViewModel.bonus}
    private var inCafe = true
    private val bottomNavigation by lazy {activity?.findViewById(R.id.bottomNavigationView) as BottomNavigationView}
    private val recyclerAdapter by lazy { ShoppingRecyclerAdapter(this,this) }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getTotalPrice()
        setUpRecycler()
        buttonListener()
        sharedViewModel.getBonus()
        sharedViewModel.checkIsUserHaveTable()
        with(binding){
            receiptHistory.setOnClickListener { bottomNavigation.selectedItemId = R.id.user_nav_graph }
            goToMenu.setOnClickListener { bottomNavigation.selectedItemId = R.id.home_nav_graph }

            delivery.setSafeOnClickListener {  // ?????????????????? ?? ?????????????????? ?????? ?????? ???????????? background ????????????
                inCafe = false
                buttonListener()
            }
            inShop.setSafeOnClickListener {
                inCafe = true
                buttonListener()
            }
            order.setOnClickListener { // ???????? ???????????????? ???????? ???? ?? ???????? ???????????? ???????? ???????? ?????????????? ???????? ?????? ???????? ???????????????????? ???????????????????????? ????
                checkIsUserHaveTable()
            }
        }
    }

    // ?????????????????? 2 cardView (?? ?????????????????? ?????? ??????) ???????????? ???? background (???????????? ??????????  ?????????????? ???????????? ?????????? ???? ?????? ????????????????)
    private fun buttonListener() {
        if (inCafe) {
            binding.inShop.cardActivate(binding.inShopText)
            binding.delivery.cardNotActive(binding.deliveryText)
        } else {
            binding.delivery.cardActivate(binding.deliveryText)
            binding.inShop.cardNotActive(binding.inShopText)
        }
    }

    // ?????????? ?????????????????? ???????? ???? ?? ?????????? ?????????????????????????????? ???????? ???????? false ???? ?? ???????? ???????????? ??????
    private fun checkIsUserHaveTable(){
        sharedViewModel.isUserHaveTable.observe(viewLifecycleOwner){
            if (it.have_table){
                checkIsUserHaveBonus()
            }else{
                bottomNavigation.selectedItemId = R.id.qr_nav_graph
            }
        }
    }

    private fun checkIsUserHaveBonus(){
        if (sharedViewModel.bonus != 0){
            ShoppingAlertDialog(this::showBonusAlertDialog, this::useBonus, "???? ???????????????? $bonus ??????????????", "???????????? ?????????? ?????")
                .show(childFragmentManager, "TAG")
        }else {
            useBonus(0)
        }
    }

    private fun setUpRecycler() {
        binding.recyclerView.apply {
            layoutManager = WrapContentLinearLayoutManager(requireContext())
            adapter = recyclerAdapter
        }
        getShoppingList()
    }

    private fun getShoppingList(){ // ?????????????????? ???????????? ?? ?????????? ???????????? ?? ?????????????? ?????? ???? ???????????? 0
        sharedViewModel.productList.observe(viewLifecycleOwner) {
                    sharedViewModel.sortProductForShopping(it)
                    if (sharedViewModel.shoppingList.isEmpty()){ // ???????? ???? ???????????? ?????????????????????? ???????? ?????? ?????????????? ????????????
                        navController.navigate(ShoppingFragmentDirections.actionShoppingFragmentToEmptyIllustrativeFragment())
                    }else {
                        recyclerAdapter.setList(sharedViewModel.shoppingList)
                    }
            getTotalPrice()
                }
    }

    @SuppressLint("SetTextI18n")
    private fun getTotalPrice() {
        binding.result.text = "${sharedViewModel.getTotalPrice()} c"
    }


    private fun showBonusAlertDialog() { // ???????????????????? 2???? Alert Dialog ?????? ???????? ???????????????? ?????????????? ?????????????? ????????????????????????
        BonusBottomSheet(sharedViewModel.bonus, this::useBonus).show(childFragmentManager, "TAG")
    }


    //???????????????? ?????????????? ?? ??????????
    private fun useBonus(bonus: Int) { // ?????????????????? ?????????? ?? AlertDialog ?????????????????????? ???????????????????????? ????????????
            val shoppingList = AllModels.Test(sharedViewModel.shoppingList) // ?????? ???????????? Serializable ?????????? ?????????? ???????????????????? ???????????? ??????????  safeArgs
            navigate(ShoppingFragmentDirections.actionShoppingFragmentToShoppingOrderFragment2(shoppingList, bonus, inCafe))
    }

    override fun setUpToolbar() {
        with(binding.include) {
            notification.setOnClickListener { navigate(ShoppingFragmentDirections.actionShoppingFragmentToNotification3()) }
            textView.text = "??????????????"
        }
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentShoppingBinding {
        return FragmentShoppingBinding.inflate(inflater)
    }

    override fun itemClicked(item: AllModels?) {
        getTotalPrice()
    }

    override fun holderClicked(model: AllModels?,position:Int) {
        if (sharedViewModel.shoppingList.isEmpty()) { // ???????? ???? ???????????? ?????????????????????? ???????? ?????? ?????????????? ????????????
            navController.navigate(ShoppingFragmentDirections.actionShoppingFragmentToEmptyIllustrativeFragment())
        }
    }
}
