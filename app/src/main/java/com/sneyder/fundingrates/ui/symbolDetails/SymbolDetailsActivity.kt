package com.sneyder.fundingrates.ui.symbolDetails

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.sneyder.fundingrates.R
import com.sneyder.fundingrates.data.model.CoinData
import com.sneyder.fundingrates.data.model.Resource
import com.sneyder.fundingrates.databinding.ActivityDetailsSymbolBinding
import com.sneyder.fundingrates.ui.base.BaseActivity
import com.sneyder.fundingrates.ui.main.MainViewModel
import com.sneyder.fundingrates.utils.debug

class SymbolDetailsActivity : BaseActivity() {

    companion object {

        private const val EXTRA_COIN_DATA = "coinData"

        fun starterIntent(context: Context, coinData: CoinData): Intent {
            val starter = Intent(context, SymbolDetailsActivity::class.java)
            starter.putExtra(EXTRA_COIN_DATA, coinData as Parcelable)
            return starter
        }

    }

    private val symbolDetailsViewModel by viewModels<SymbolDetailsViewModel> { viewModelFactory }
    private val coinData: CoinData? by lazy { intent.getParcelableExtra(EXTRA_COIN_DATA) }
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityDetailsSymbolBinding>(
            this,
            R.layout.activity_details_symbol
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.lifecycleOwner = this@SymbolDetailsActivity
        symbolDetailsViewModel.fundingRates.observe(this) { response->
            when(response){
                is Resource.Loading -> {
                    debug("loading")
                }
                is Resource.Failure -> {
                    debug("failure")
                }
                is Resource.Success -> {
                    debug("success ${response.data.take(10)}")
                }
            }
        }
        coinData?.let {
            supportActionBar?.title = it.symbol
            binding.coinData = it
            symbolDetailsViewModel.getFundingRatesBySymbol(it.symbol)
        }
        binding.executePendingBindings()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

}