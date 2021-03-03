package com.sneyder.fundingrates.data.repository

import com.sneyder.fundingrates.data.model.CoinData
import com.sneyder.fundingrates.data.model.FundingRate
import com.sneyder.fundingrates.data.model.Resource
import kotlinx.coroutines.flow.Flow

abstract class FundingRatesRepository {

    abstract fun fetchCoinsData(): Flow<Resource<List<CoinData>>>
    abstract fun fetchFundingRates(symbol: String): Flow<Resource<List<FundingRate>>>

}