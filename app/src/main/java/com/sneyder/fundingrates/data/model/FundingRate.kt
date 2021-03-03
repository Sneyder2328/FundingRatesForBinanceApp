package com.sneyder.fundingrates.data.model

import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Keep
@Parcelize
data class FundingRate(
    var fundingRate: Double = 0.0,
    var fundingTime: Long = 0,
): Serializable, Parcelable
