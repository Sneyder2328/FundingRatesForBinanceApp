package com.sneyder.fundingrates.ui.symbolDetails

import androidx.lifecycle.*
import com.sneyder.fundingrates.data.model.FundingRate
import com.sneyder.fundingrates.data.model.Resource
import com.sneyder.fundingrates.data.repository.FundingRatesRepository
import javax.inject.Inject

class SymbolDetailsViewModel
@Inject constructor(
    private val fundingRatesRepository: FundingRatesRepository
) : ViewModel() {

    private val symbol: MutableLiveData<String> by lazy { MutableLiveData() }
    val fundingRates: LiveData<Resource<List<FundingRate>>> =
        symbol.distinctUntilChanged().switchMap {
            fundingRatesRepository.fetchFundingRates(it).asLiveData()
        }

    fun getFundingRatesBySymbol(symbolToFetch: String) {
        symbol.value = symbolToFetch
    }

}