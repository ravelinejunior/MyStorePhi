package br.com.raveline.mystorephi.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.firestore.CollectionReference

interface UserRepository {
    suspend fun saveUserOnServer(email: String, password: String): Task<AuthResult>
    suspend fun loginUserFromServer(email: String, password: String): Task<AuthResult>
    suspend fun saveAddress(userId: String): CollectionReference
}