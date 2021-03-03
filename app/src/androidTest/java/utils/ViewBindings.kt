package com.sneyder.productslist.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter(value = ["setImageUrl"])
fun ImageView.bindImageUrl(url: String?) {
    if (url != null && url.isNotBlank()) {
        Glide.with(this.context)
            .load(url)
            .centerCrop()
            .into(this)
    }
}