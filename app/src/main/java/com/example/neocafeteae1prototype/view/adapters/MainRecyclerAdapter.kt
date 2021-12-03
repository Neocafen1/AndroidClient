package com.example.neocafeteae1prototype.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafeteae1prototype.R
import com.example.neocafeteae1prototype.view.tools.delegates.RecyclerItemClickListener
import com.example.neocafeteae1prototype.databinding.*
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.view.adapters.AllViewHolders
import com.example.neocafeteae1prototype.view.tools.setSafeOnClickListener

class MainRecyclerAdapter(private val recyclerItemClick: RecyclerItemClickListener?) :
    RecyclerView.Adapter<AllViewHolders>() {
    private var items = listOf<AllModels>()
    fun setList(list: List<AllModels>) {
        items = list
        notifyDataSetChanged()
    }


    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is AllModels.Popular -> super.getItemViewType(position)
            is AllModels.Product -> R.layout.product_reciept_item
            is AllModels.Receipt -> R.layout.history_of_receipt_item
            is AllModels.Notification -> R.layout.notification_item
            is AllModels.Menu -> R.layout.menu_item
            is AllModels.Filial -> R.layout.map_item
            is AllModels.Schedule -> R.layout.branch_time_work_item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllViewHolders {
        return when (viewType) {
            R.layout.menu_item -> {
                AllViewHolders.MenuItemViewHolder(
                    MenuItemBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            R.layout.map_item -> AllViewHolders.NeoCafeViewHolder(
                MapItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            R.layout.branch_time_work_item -> AllViewHolders.BranchTimeWorkViewHolder(
                BranchTimeWorkItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            R.layout.history_of_receipt_item -> AllViewHolders.ReceiptViewHolder(
                HistoryOfReceiptItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            R.layout.notification_item -> AllViewHolders.NotificationViewHolder(
                NotificationItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )

            R.layout.product_reciept_item -> AllViewHolders.ProductReceiptViewHolder(
                ProductRecieptItemBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
            )
            else -> throw IllegalArgumentException("Invalid Type from adapter")
        }
    }

    override fun onBindViewHolder(holder: AllViewHolders, position: Int) {
        return when (holder) {
            is AllViewHolders.MenuItemViewHolder -> {
                holder.bind(items[position] as AllModels.Menu)
                holder.itemView.setSafeOnClickListener {
                    recyclerItemClick?.itemClicked(items[position])
                }
            }
            is AllViewHolders.NeoCafeViewHolder -> {
                holder.bind(items[position] as AllModels.Filial)
                holder.itemView.setSafeOnClickListener {
                    recyclerItemClick?.itemClicked(items[position])
                }
            }
            is AllViewHolders.BranchTimeWorkViewHolder -> holder.bind(items[position] as AllModels.Schedule)
            is AllViewHolders.ReceiptViewHolder -> {
                holder.bind(items[position] as AllModels.Receipt)
                holder.itemView.setSafeOnClickListener {
                    recyclerItemClick?.itemClicked(items[position])
                }
            }
            is AllViewHolders.ProductReceiptViewHolder -> holder.bind(items[position] as AllModels.Product)
            is AllViewHolders.NotificationViewHolder -> holder.bind(items[position] as AllModels.Notification)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}