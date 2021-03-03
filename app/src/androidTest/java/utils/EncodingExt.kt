package com.sneyder.productslist.utils

import android.util.Base64

// No line break
fun String.asBase64(): String {
    return Base64.encodeToString(this.toByteArray(), Base64.NO_WRAP)
}