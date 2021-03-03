package com.sneyder.fundingrates.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.sneyder.fundingrates.data.model.*
import com.sneyder.fundingrates.utils.debug
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*

@Singleton
class AppFundingRatesRepository
@Inject constructor(
    private val firebaseFirestore: FirebaseFirestore
) : FundingRatesRepository() {

    @ExperimentalCoroutinesApi
    override fun fetchCoinsData(): Flow<Resource<List<CoinData>>> {
        return firebaseFirestore.collection("pairs")
            .asFlow<CoinData>()
            .flowOn(Dispatchers.IO)
            .asResource()
    }

    @ExperimentalCoroutinesApi
    override fun fetchFundingRates(symbol: String): Flow<Resource<List<FundingRate>>> {
        debug("fetchFundingRates $symbol")
        return firebaseFirestore.collection("pairs/$symbol/fundingRates")
            .asFlow<FundingRate>()
            .flowOn(Dispatchers.IO)
            .asResource()
    }

}