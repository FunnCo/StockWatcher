package com.funnco.stockwatcher.activity.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.funnco.stockwatcher.common.finn.Repository
import com.funnco.stockwatcher.common.model.StockModel
import com.funnco.stockwatcher.databinding.ItemStockBinding
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class RecyclerAdapter(val listOfItems: List<StockModel>) : RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder>() {
    class RecyclerViewHolder(var itemView: View) : RecyclerView.ViewHolder(itemView){

        lateinit var binding: ItemStockBinding

        fun bind(item: StockModel) {
            binding = ItemStockBinding.bind(itemView)

            binding.stockName.text = item.symbol.description
            binding.stockPrice.text = '$'+item.quote.c.toString()

            MainScope().launch {
                Repository.subscribeToUpdates(updateInterface, item.symbol)
            }
        }

        val updateInterface = object : StockUpdateInterface {
            override fun updateCertainStock(price: Double) {
                binding.stockPrice.text = "$${price}"
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(ItemStockBinding.inflate(LayoutInflater.from(parent.context), parent, false).root)
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        holder.bind(listOfItems[position])
    }

    override fun getItemCount(): Int {
        return listOfItems.size
    }
}