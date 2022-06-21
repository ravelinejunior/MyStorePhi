package br.com.raveline.mystorephi.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.raveline.mystorephi.utils.addressLocalDatabaseTable

@Entity(tableName = addressLocalDatabaseTable)
data class AddressModel(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val address:String,
    var isSelected:Boolean = false
)
