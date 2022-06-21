package br.com.raveline.mystorephi.data.database.dao

import androidx.room.*
import br.com.raveline.mystorephi.data.model.AddressModel
import kotlinx.coroutines.flow.Flow

@Dao
interface UserAddressDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAddress(address: AddressModel)

    @Update
    suspend fun updateAddress(address: AddressModel)

    @Delete
    suspend fun deleteAddress(address: AddressModel)

    @Query("DELETE FROM AddressTable")
    fun deleteAddressTable()

    @Query("SELECT * FROM AddressTable ORDER BY id")
    fun getAllSavedAddress(): Flow<List<AddressModel>>

}