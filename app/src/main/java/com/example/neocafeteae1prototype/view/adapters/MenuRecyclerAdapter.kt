package com.example.neocafeteae1prototype.view.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.neocafeteae1prototype.databinding.PopularItemBinding
import com.example.neocafeteae1prototype.data.models.AllModels
import com.example.neocafeteae1prototype.view.tools.delegates.RecyclerItemClickListener
import com.example.neocafeteae1prototype.view.tools.delegates.SecondItemClickListener
import com.example.neocafeteae1prototype.view.tools.loadWithGlide


class MenuRecyclerAdapter(private val click:SecondItemClickListener) : RecyclerView.Adapter<MenuRecyclerAdapter.ViewHolder>() {

    private var list = mutableListOf<AllModels.Popular>()
    private var filter = ""

    fun setList(list: MutableList<AllModels.Popular>, filter:String) {
        this.list = list
        this.filter = filter
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
        holder.itemView.setOnClickListener {
            click.holderClicked(list[position], position)
        }
        with(list[position]) {
            if (category_name == filter || filter == "Все") {
                binding.foodImage.loadWithGlide(image)

                binding.foodName.text = title
                binding.foodPrice.text = "$price c"

                if (county > 0) {
                    binding.county.apply {
                        visibility = View.VISIBLE
                        text = county.toString()
                    }
                    binding.minus.visibility = View.VISIBLE
                } else {
                    binding.county.visibility = View.GONE
                    binding.minus.visibility = View.GONE
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
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }
}