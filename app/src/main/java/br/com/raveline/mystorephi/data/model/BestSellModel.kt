package br.com.raveline.mystorephi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.raveline.mystorephi.utils.bestSellDatabaseTable

@Entity(tableName = bestSellDatabaseTable)
data class BestSellModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val name: String,
    val description: String,
    val imageUrl: String,
    val price: Double,
    val rating: Int
)

fun bestSellToFeature(bestSellModel: BestSellModel): FeaturesModel {
    return FeaturesModel(
        bestSellModel.id,
        bestSellModel.name,
        bestSellModel.description,
        bestSellModel.imageUrl,
        bestSellModel.price,
        bestSellModel.rating,
    )
}
