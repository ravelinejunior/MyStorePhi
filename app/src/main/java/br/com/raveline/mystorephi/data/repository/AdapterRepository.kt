package br.com.raveline.mystorephi.data.repository

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.QuerySnapshot

interface AdapterRepository {
    suspend fun getCategoriesRepository(): Task<QuerySnapshot>
    suspend fun getFeaturesRepository():Task<QuerySnapshot>
    suspend fun getBesSellRepository():Task<QuerySnapshot>
}