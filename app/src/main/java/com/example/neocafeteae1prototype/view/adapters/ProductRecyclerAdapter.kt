package com.example.neocafeteae1prototype.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafeteae1prototype.databinding.PopularItemBinding
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.view.tools.delegates.SecondItemClickListener
import com.example.neocafeteae1prototype.view.tools.loadWithGlide
import com.example.neocafeteae1prototype.view.tools.notVisible
import com.example.neocafeteae1prototype.view.tools.visible

class ProductRecyclerAdapter(private val clicker: SecondItemClickListener?) :
    RecyclerView.Adapter<ProductRecyclerAdapter.ViewHolder>() {

    private var list = mutableListOf<AllModels.Popular>()

    fun setList(list: MutableList<AllModels.Popular>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: PopularItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PopularItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.binding
        with(list[position]) {

            binding.foodImage.loadWithGlide(image)

            binding.foodName.text = title
            binding.foodPrice.text = "$price c"

            if (isPopular){
                binding.star.visible()
            }

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
                notifyItemChanged(position)
            }
            binding.minus.setOnClickListener {
                county -= 1
                notifyItemChanged(position)
            }
        }

        holder.itemView.setOnClickListener {
            clicker?.holderClicked(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}