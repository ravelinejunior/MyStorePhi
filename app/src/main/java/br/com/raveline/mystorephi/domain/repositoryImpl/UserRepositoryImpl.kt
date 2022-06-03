package br.com.raveline.mystorephi.domain.repositoryImpl

import br.com.raveline.mystorephi.data.repository.UserRepository
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
) : UserRepository {

    override suspend fun saveUserOnServer(email: String, password: String): Task<AuthResult> {
       return firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{}
    }

    override suspend fun loginUserFromServer(email: String, password: String):Task<AuthResult> {
        return firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {  }
    }
}