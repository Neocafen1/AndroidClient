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
import com.example.neocafeteae1prototype.view.tools.WrapContentLinearLayoutManager
import com.example.neocafeteae1prototype.view.tools.alert_dialog.ShoppingAlertDialog
import com.example.neocafeteae1prototype.view.tools.bottom_sheet.BonusBottomSheet
import com.example.neocafeteae1prototype.view.tools.cardActivate
import com.example.neocafeteae1prototype.view.tools.cardNotActive
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
        binding.receiptHistory.setOnClickListener { bottomNavigation.selectedItemId = R.id.user_nav_graph }
        binding.goToMenu.setOnClickListener { bottomNavigation.selectedItemId = R.id.home_nav_graph }

        binding.delivery.setOnClickListener {  // Слушатель в заведении или нет меняет background кнопки
            inCafe = false
            buttonListener()
        }
        binding.inShop.setOnClickListener {
            inCafe = true
            buttonListener()
        }
        binding.order.setOnClickListener { // Идет проверка есть ли у него бонусы если есть выходит окно где него спрашивают использовать их
            checkIsUserHaveTable()
        }

    }

    // Слушатель 2 cardView (В заведении или нет) меняет их background (Клиент таким  образом узнает какой из них активный)
    private fun buttonListener() {
        if (inCafe) {
            binding.inShop.cardActivate(binding.inShopText)
            binding.delivery.cardNotActive(binding.deliveryText)
        } else {
            binding.delivery.cardActivate(binding.deliveryText)
            binding.inShop.cardNotActive(binding.inShopText)
        }
    }

    // Метод проверяет есть ли у юзера забронированный стол если false то у него столов нет
    private fun checkIsUserHaveTable(){
        sharedViewModel.checkIsUserHaveTable()
        sharedViewModel.isUserHaveTable.observe(viewLifecycleOwner){
            if (it){
                checkIsUserHaveBonus()
            }else{
                bottomNavigation.selectedItemId = R.id.qr_nav_graph
            }
        }
    }

    private fun checkIsUserHaveBonus(){
        if (sharedViewModel.bonus != 0){
            ShoppingAlertDialog(this::showBonusAlertDialog, this::useBonus, "Вы накопили $bonus бонусов", "Хотите снять их?")
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

    private fun getShoppingList(){ // Сортирует массив и берет данные у которых кол во больше 0
        sharedViewModel.productList.observe(viewLifecycleOwner) {
                    sharedViewModel.sortProductForShopping(it)
                    if (sharedViewModel.shoppingList.isEmpty()){ // Если он пустой открывается окно что корзина пустая
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


    private fun showBonusAlertDialog() { // Показывает 2ой Alert Dialog где юзер выбирает сколько бонусов использовать
        BonusBottomSheet(sharedViewModel.bonus, this::useBonus).show(childFragmentManager, "TAG")
    }


    //Проверка бонусов и стола
    private fun useBonus(bonus: Int) { // Сработает когда в AlertDialog срабатывает использовать бонусы
            val shoppingList = AllModels.Test(sharedViewModel.shoppingList) // Это просто Serializable модел чтобы отправлять дынные через  safeArgs
            navController.navigate(ShoppingFragmentDirections.actionShoppingFragmentToShoppingOrderFragment2(shoppingList, bonus, inCafe))
    }

    override fun setUpToolbar() {
        with(binding.include) {
            notification.setOnClickListener { findNavController().navigate(ShoppingFragmentDirections.actionShoppingFragmentToNotification3()) }
            textView.text = "Корзина"
        }
    }

    override fun inflateView(inflater: LayoutInflater, container: ViewGroup?): FragmentShoppingBinding {
        return FragmentShoppingBinding.inflate(inflater)
    }

    override fun itemClicked(item: AllModels?) {
        getTotalPrice()
    }

    override fun holderClicked(model: AllModels?,position:Int) {
        if (sharedViewModel.shoppingList.isEmpty()) { // ЕСли он пустой открывается окно что корзина пустая
            navController.navigate(ShoppingFragmentDirections.actionShoppingFragmentToEmptyIllustrativeFragment())
        }
    }
}
