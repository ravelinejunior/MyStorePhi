package br.com.raveline.mystorephi.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.raveline.mystorephi.utils.categoriesDatabaseTable
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = categoriesDatabaseTable)
data class CategoryModel(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,

    val type: String,
    val imageUrl: String
) : Parcelable
