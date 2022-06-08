package br.com.raveline.mystorephi.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.raveline.mystorephi.data.model.CategoryModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryModel>)

    @Query("SELECT * FROM CategoriesTable ORDER BY ID")
    fun getCategories(): Flow<List<CategoryModel>>

    @Query("DELETE FROM CategoriesTable")
    suspend fun deleteFromCategories()
}