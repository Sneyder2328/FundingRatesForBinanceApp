package com.sneyder.fundingrates.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


sealed class Order: Parcelable {
    @Parcelize
    object ASC : Order()
    @Parcelize
    object DESC : Order()
}