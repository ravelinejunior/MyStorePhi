package br.com.raveline.mystorephi.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.raveline.mystorephi.data.model.FeaturesModel
import kotlinx.coroutines.flow.Flow

@Dao
interface FeaturesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeatures(features: List<FeaturesModel>)

    @Query("SELECT * FROM FeaturesTable ORDER BY ID")
    fun getFeatures(): Flow<List<FeaturesModel>>

    @Query("DELETE FROM FeaturesTable")
    suspend fun deleteFromFeatures()
}