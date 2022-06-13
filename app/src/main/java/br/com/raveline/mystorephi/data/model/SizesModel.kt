package br.com.raveline.mystorephi.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SizesModel(
    val selectedSize: String,
    var isSelected: Boolean = false
):Parcelable
