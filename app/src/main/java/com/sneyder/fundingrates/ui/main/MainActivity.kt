package com.sneyder.fundingrates.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sneyder.fundingrates.R
import com.sneyder.fundingrates.data.model.Order
import com.sneyder.fundingrates.data.model.Resource
import com.sneyder.fundingrates.databinding.ActivityMainBinding
import com.sneyder.fundingrates.ui.base.BaseActivity
import com.sneyder.fundingrates.ui.symbolDetails.SymbolDetailsActivity
import com.sneyder.fundingrates.utils.debug

class MainActivity : BaseActivity(), FilterOptionsDialog.SelectFilterOptionsListener {

    private val mainViewModel by viewModels<MainViewModel> { viewModelFactory }
    private val binding by lazy {
        DataBindingUtil.setContentView<ActivityMainBinding>(
            this,
            R.layout.activity_main
        )
    }
    private val coinsDataAdapter by lazy {
        CoinsDataAdapter(onCoinDataSelected = {
            startActivity(SymbolDetailsActivity.starterIntent(this, it))
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding.lifecycleOwner = this@MainActivity
        binding.coinsDataRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            adapter = coinsDataAdapter
        }
        binding.filterButton.setOnClickListener { showFilterOptionsBottomSheet() }
        binding.searchInput.addTextChangedListener { coinsDataAdapter.query = it.toString() }
        observeCoinsData()
    }

    private fun showFilterOptionsBottomSheet() {
        val filterOptionsDialog = FilterOptionsDialog.newInstance(coinsDataAdapter.scoreRange, coinsDataAdapter.order)
        filterOptionsDialog.show(supportFragmentManager, filterOptionsDialog.tag)
    }

    override fun onScoreRangeSelected(index: Int) {
        debug("onScoreRangeSelected $index")
        coinsDataAdapter.scoreRange = index
    }

    override fun onOrderSelected(order: Order) {
        coinsDataAdapter.order = order
    }

    private fun observeCoinsData() {
        mainViewModel.coinsData.observe(this) { coinsData ->
            when (coinsData) {
                is Resource.Success -> {
                    debug("is Resource.Success")
                    coinsDataAdapter.originalCoinsData = coinsData.data
                }
                is Resource.Loading -> {
                    debug("is Resource.Loading")
                }
                is Resource.Failure -> {
                    debug("is Resource.Error ${coinsData.error}")
                }
            }
        }
    }

}