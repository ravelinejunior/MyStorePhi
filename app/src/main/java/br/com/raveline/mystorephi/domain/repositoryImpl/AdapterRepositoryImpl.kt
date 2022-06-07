package br.com.raveline.mystorephi.domain.repositoryImpl

import br.com.raveline.mystorephi.data.repository.AdapterRepository
import br.com.raveline.mystorephi.utils.bestSellDatabaseReference
import br.com.raveline.mystorephi.utils.categoriesDatabaseReference
import br.com.raveline.mystorephi.utils.featuresDatabaseReference
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class AdapterRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore
) : AdapterRepository {
    override suspend fun getCategoriesRepository(): Task<QuerySnapshot> {
        return firestore.collection(categoriesDatabaseReference)
            .get()
    }

    override suspend fun getFeaturesRepository(): Task<QuerySnapshot> {
        return firestore.collection(featuresDatabaseReference).get()
    }

    override suspend fun getBesSellRepository(): Task<QuerySnapshot> {
        return firestore.collection(bestSellDatabaseReference).get()
    }

}