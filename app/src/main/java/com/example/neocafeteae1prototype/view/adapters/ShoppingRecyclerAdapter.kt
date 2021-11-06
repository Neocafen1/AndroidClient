package com.example.neocafeteae1prototype.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.databinding.PopularItemBinding
import com.example.neocafeteae1prototype.view.tools.delegates.RecyclerItemClickListener
import com.example.neocafeteae1prototype.view.tools.delegates.SecondItemClickListener
import com.example.neocafeteae1prototype.view.tools.loadWithGlide
import com.example.neocafeteae1prototype.view.tools.notVisible
import com.example.neocafeteae1prototype.view.tools.visible


class ShoppingRecyclerAdapter(private val clicker:RecyclerItemClickListener, private val secondItemClickListener: SecondItemClickListener) :
    RecyclerView.Adapter<ShoppingRecyclerAdapter.ViewHolder>() {

    private var list = mutableListOf<AllModels.Popular>()

    fun setList(list: MutableList<AllModels.Popular>) {
        this.list = list
    }

    inner class ViewHolder(val binding: PopularItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        with(list[position]) {

            binding.foodImage.loadWithGlide(image)

            binding.foodName.text = title
            binding.foodPrice.text = "$price c"

            if (county > 0) {
                binding.county.apply {
                    visible()
                    text = county.toString()
                }
                binding.minus.visible()
            } else {
                binding.county.notVisible()
                binding.minus.notVisible()
            }

            binding.plus.setOnClickListener {
                county += 1
                clicker.itemClicked(null)
                notifyItemChanged(position)
            }
            binding.minus.setOnClickListener {
                county -= 1
                clicker.itemClicked(null)
                notifyItemChanged(position)
                if (county == 0){
                    list.removeAt(position)
                    secondItemClickListener.holderClicked(null)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}