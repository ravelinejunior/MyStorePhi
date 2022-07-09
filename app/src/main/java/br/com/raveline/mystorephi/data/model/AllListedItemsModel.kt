package br.com.raveline.mystorephi.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.raveline.mystorephi.utils.itemsDatabaseTable
import kotlinx.parcelize.Parcelize

@Entity(tableName = itemsDatabaseTable)
@Parcelize
data class AllListedItemsModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val description: String,
    val imageUrl: String,
    val type: String,
    val price: Double,
    val rating: Int
) : Parcelable
