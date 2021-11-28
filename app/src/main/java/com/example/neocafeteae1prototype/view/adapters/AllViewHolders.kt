package com.example.neocafeteae1prototype.view.adapters

import android.annotation.SuppressLint
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.databinding.*
import com.example.neocafeteae1prototype.view.tools.loadWithGlide
import com.example.neocafeteae1prototype.view.tools.setWhiteColor

sealed class AllViewHolders(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class MenuItemViewHolder(val binding: MenuItemBinding) : AllViewHolders(binding) {
        fun bind(item: AllModels.Menu) {
            with(binding) {
                Glide.with(menuImage.context)
                    .load(item.image)
                    .into(menuImage)

                menuName.text = item.name
            }
        }
    }

    class NeoCafeViewHolder(val binding: MapItemBinding) : AllViewHolders(binding){

        @SuppressLint("SetTextI18n")
        fun bind(item: AllModels.Filial){

            with(binding){
                streetMap.text = item.adress
                timeWork.text = "${item.schedule[0].start} - ${item.schedule[0].end}"
                imageView3.loadWithGlide(item.image)
            }
        }
    }

    class BranchTimeWorkViewHolder(val binding: BranchTimeWorkItemBinding) : AllViewHolders(binding){

        fun bind(item: AllModels.Schedule){
            with(binding){
                startWorkTime.text = item.start
                endWorkTime.text = item.end
                day.text = item.day

                if (item.isToday) {
                    linearLayout.setBackgroundResource(R.drawable.layout_background_with_radius_enable)
                    startWorkTime.setWhiteColor(startWorkTime.context)
                    endWorkTime.setWhiteColor(endWorkTime.context)
                    day.setWhiteColor(day.context)
                }
            }
        }
    }

    class ReceiptViewHolder(val binding: HistoryOfReceiptItemBinding): AllViewHolders(binding){

        fun bind(item : AllModels.Receipt){

            val firstProduct = item.details.orderItems[0]
            val secondProduct = item.details.orderItems[1]

            with(binding){
                with(item){
                    binding.date.text = date
                    binding.time.text = time
                    binding.totalPrice.text = "$finalPrice c"
                    setUpProductList(productName, productPrice, productCounty, totalProductPrice, firstProduct, item.finalPrice)
                    setUpProductList(secondProductName, secondProductPrice, secondProductCounty, secondTotalProductPrice, secondProduct, item.finalPrice)
                }
            }
        }

        // Установ данные о первых 2 элементах
        @SuppressLint("SetTextI18n")
        private fun setUpProductList(name: TextView, price:TextView, county:TextView, totalProductPrice:TextView, data: AllModels.Product, totalPrice:Int){
            name.text = data.productTitle
            price.text = "${data.price} c"
            county.text = "${data.quantity} шт"
            totalProductPrice.text = "${data.sum} c"
        }
    }

    class ProductReceiptViewHolder(val binding: ProductRecieptItemBinding) : AllViewHolders(binding){

        @SuppressLint("SetTextI18n")
        fun bind(item: AllModels.Product){
                with(binding){
                    productName.text = item.productTitle
                    productPrice.text = "${item.price} c "
                    productCounty.text = "${item.quantity} шт"
                    totalProductPrice.text = "${item.sum} c"
            }
        }
    }


    class NotificationViewHolder(val binding:NotificationItemBinding) : AllViewHolders(binding){

        fun bind(item: AllModels.Notification){
            with(binding){
                message.text = item.message
                title.text = item.title
                time.text = item.time
            }
        }
    }
}
