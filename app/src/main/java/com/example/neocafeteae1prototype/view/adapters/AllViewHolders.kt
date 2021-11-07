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

            val firstProduct = item.list[0]
            val secondProduct = item.list[1]

            with(binding){
                with(item){
                    `when`.text = time
                    setUpProductList(productName, productPrice, productCounty, totalProductPrice, firstProduct)
                    setUpProductList(secondProductName, secondProductPrice, secondProductCounty, secondTotalProductPrice, secondProduct)
                }
            }
        }

        private fun setUpProductList(name: TextView, price:TextView, county:TextView, totalProductPrice:TextView, data: AllModels.Product){
            name.text = data.productName
            price.text = data.productPrice
            county.text = data.county
            totalProductPrice.text = data.totalProductPrice

        }
    }

    class ProductReceiptViewHolder(val binding: ProductRecieptItemBinding) : AllViewHolders(binding){

        @SuppressLint("SetTextI18n")
        fun bind(item: AllModels.Product){
                with(binding){
                    productName.text = item.productName
                    productPrice.text = "${item.productPrice} c "
                    productCounty.text = "${item.county} шт"
                    val a = item.county.toInt() * item.productPrice.toInt()
                    totalProductPrice.text = "$a c"
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
