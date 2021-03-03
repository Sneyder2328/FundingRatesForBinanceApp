package com.sneyder.fundingrates.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.firebase.Timestamp
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Keep
@Parcelize
data class CoinData(
    var symbol: String = "",
    var iconUrl: String = "",
    var score30d: Double = 0.0,
    var score14d: Double = 0.0,
    var score7d: Double = 0.0,
    var last24HoursRate: Double = 0.0,
    var last16HoursRate: Double = 0.0,
    var last8HoursRate: Double = 0.0,
    var timestamp: Timestamp? = null,
) : Serializable, Parcelable {
    fun getScoreForSorting(scoreRange: Int): Double {
        return when (scoreRange) {
            0 -> score7d
            1 -> score14d
            else -> score30d
        }
    }
}