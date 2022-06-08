package br.com.raveline.mystorephi.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.raveline.mystorephi.data.model.BestSellModel
import kotlinx.coroutines.flow.Flow

@Dao
interface BestSellDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBestSells(bestSells: List<BestSellModel>)

    @Query("SELECT * FROM BestSellTable ORDER BY ID")
    fun getBestSells(): Flow<List<BestSellModel>>

    @Query("DELETE FROM BestSellTable")
    suspend fun deleteFromBestSells()

}