package br.com.raveline.mystorephi.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.raveline.mystorephi.utils.featuresDatabaseTable
import kotlinx.parcelize.Parcelize

@Entity(tableName = featuresDatabaseTable)
@Parcelize
data class FeaturesModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val rating: Int
) : Parcelable
