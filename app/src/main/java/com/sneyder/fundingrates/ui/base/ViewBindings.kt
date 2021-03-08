package com.sneyder.fundingrates.ui.base

import android.graphics.Typeface.BOLD
import android.graphics.Typeface.ITALIC
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.sneyder.fundingrates.data.model.CoinData
import kotlin.math.pow
import kotlin.math.roundToInt

@BindingAdapter(value = ["score", "scoreRange"])
fun TextView.score(coinData: CoinData, scoreRange: Int) {
    val factor = 10.0.pow(2.0)
    val d = (coinData.getScoreForSorting(scoreRange) * factor).roundToInt() / factor
    text = d.toString()
}

@BindingAdapter(value = ["labelledScore", "labelledScoreRange"])
fun TextView.scoreWithLabel(coinData: CoinData, scoreRange: Int) {
    val factor = 10.0.pow(2.0)
    val d = (coinData.getScoreForSorting(scoreRange) * factor).roundToInt() / factor
    val s = when (scoreRange) {
        0 -> "7d"
        1 -> "14d"
        else -> "30d"
    }
    val spannableString = SpannableString("$s Score: \n$d")
    spannableString.setSpan(
        StyleSpan(BOLD),
        spannableString.toString().indexOf(":"), spannableString.length,
        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
    )
    text = spannableString
}

@BindingAdapter(value = ["roundPercentage"])
fun TextView.roundPercentage(number: Double) {
    val factor = 10.0.pow(3.0)
    val d = (number * factor).roundToInt() / factor
    text = "$d%"
}

@BindingAdapter(value = ["symbol"])
fun TextView.symbol(symbol: String) {
    val coin = symbol.substring(0, symbol.length - 4)
    val spannableString = SpannableString(coin + "\n" + symbol.takeLast(4))
    spannableString.setSpan(
        RelativeSizeSpan(0.9f),
        symbol.length - 4, symbol.length + 1,
        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
    )
    spannableString.setSpan(
        StyleSpan(BOLD),
        0, symbol.length - 4,
        Spannable.SPAN_INCLUSIVE_EXCLUSIVE
    )
    text = spannableString
}

@BindingAdapter(value = ["setImageUrl"])
fun ImageView.bindImageUrl(url: String?) {
    if (url != null && url.isNotBlank()) {
        Glide.with(this.context)
            .load(url)
            .centerCrop()
            .into(this)
    }
}