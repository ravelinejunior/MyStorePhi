package br.com.raveline.mystorephi.domain.repositoryImpl

import br.com.raveline.mystorephi.data.repository.UserRepository
import br.com.raveline.mystorephi.utils.addressDatabaseReference
import br.com.raveline.mystorephi.utils.userDatabaseReference
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) : UserRepository {

    override suspend fun saveUserOnServer(email: String, password: String): Task<AuthResult> {
        return firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {}
    }

    override suspend fun loginUserFromServer(email: String, password: String): Task<AuthResult> {
        return firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { }
    }

    override suspend fun saveAddress(userId: String): CollectionReference {
        return firestore.collection(userDatabaseReference).document(userId)
            .collection(addressDatabaseReference)
    }

    override suspend fun getSavedAddresses(userId: String): Task<QuerySnapshot> {
        return firestore.collection(userDatabaseReference).document(userId)
            .collection(addressDatabaseReference).get()
    }
}