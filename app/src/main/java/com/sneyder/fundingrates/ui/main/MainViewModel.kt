package com.sneyder.fundingrates.ui.main

import androidx.lifecycle.*
import com.sneyder.fundingrates.data.model.CoinData
import com.sneyder.fundingrates.data.model.Resource
import com.sneyder.fundingrates.data.model.asResourceLiveData
import com.sneyder.fundingrates.data.repository.FundingRatesRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject

class MainViewModel
@Inject constructor(
    private val fundingRatesRepository: FundingRatesRepository
) : ViewModel() {

    val coinsData: LiveData<Resource<List<CoinData>>> by lazy {
        fundingRatesRepository.fetchCoinsData().asLiveData()
    }

}

