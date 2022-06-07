package br.com.raveline.mystorephi.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CategoryModel(
    val type: String,
    val imageUrl: String
) : Parcelable
