package com.sneyder.fundingrates.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sneyder.fundingrates.data.model.CoinData
import com.sneyder.fundingrates.data.model.Order
import com.sneyder.fundingrates.databinding.ActivityMainItemBinding
import com.sneyder.fundingrates.utils.debug

class CoinsDataAdapter(
    private val onCoinDataSelected: (coinData: CoinData) -> Unit
) : RecyclerView.Adapter<CoinsDataAdapter.CoinDataViewHolder>() {

    var originalCoinsData: List<CoinData> = ArrayList()
        set(value) {
            field = value
            updateCoinsData()
        }
    private var coinsData: List<CoinData> = ArrayList()

    private fun updateCoinsData() {
        coinsData = originalCoinsData.filter { it.symbol.contains(query, true) }
        coinsData =
            if (order == Order.DESC) coinsData.sortedByDescending { it.getScoreForSorting(scoreRange) }
            else coinsData.sortedBy { it.getScoreForSorting(scoreRange) }
        notifyDataSetChanged()
    }

    var scoreRange: Int = 0
        set(value) {
            field = value
            updateCoinsData()
        }

    var order: Order = Order.DESC
        set(value) {
            field = value
            updateCoinsData()
        }

    var query: String = ""
        set(value) {
            field = value.trim()
            updateCoinsData()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinDataViewHolder {
        val binding =
            ActivityMainItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoinDataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoinDataViewHolder, position: Int) {
        holder.bind(coinsData[position])
    }

    override fun getItemCount(): Int = coinsData.count()

    inner class CoinDataViewHolder(private val binding: ActivityMainItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(coinData: CoinData) {
            debug("bind $coinData")
            binding.coinData = coinData
            binding.scoreRange = scoreRange
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                onCoinDataSelected(coinData)
            }
        }
    }

//    interface ProductListListener {
//        fun onProductSelected(product: Product)
//    }

}