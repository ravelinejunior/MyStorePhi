package br.com.raveline.mystorephi.domain.repositoryImpl

import br.com.raveline.mystorephi.data.repository.AdapterRepository
import br.com.raveline.mystorephi.utils.categoriesDatabaseReference
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

}