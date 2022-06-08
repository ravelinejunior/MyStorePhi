package br.com.raveline.mystorephi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.raveline.mystorephi.utils.featuresDatabaseTable

@Entity(tableName = featuresDatabaseTable)
data class FeaturesModel(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,

    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val rating: Int
)
