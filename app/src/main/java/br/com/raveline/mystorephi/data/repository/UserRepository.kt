package br.com.raveline.mystorephi.data.repository

import br.com.raveline.mystorephi.data.model.AddressModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun saveUserOnServer(email: String, password: String): Task<AuthResult>
    suspend fun loginUserFromServer(email: String, password: String): Task<AuthResult>
    suspend fun saveAddress(userId: String): CollectionReference
    suspend fun getSavedAddresses(userId: String): Task<QuerySnapshot>

    //Locals
    suspend fun insertLocalAddress(addressModel: AddressModel)
    suspend fun updateLocalAddress(addressModel: AddressModel)
    suspend fun deleteLocalAddress(addressModel: AddressModel)
    fun deleteAddressTable()
    fun getAllAddress(): Flow<List<AddressModel>>
}