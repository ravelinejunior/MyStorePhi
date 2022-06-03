package br.com.raveline.mystorephi.presentation.viewmodel.viewmodel_factory


import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import br.com.raveline.mystorephi.domain.repositoryImpl.UserRepositoryImpl
import br.com.raveline.mystorephi.presentation.viewmodel.UserViewModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class UserViewModelFactory @Inject constructor(
    private val userRepository: UserRepositoryImpl,
    private val firestore: FirebaseFirestore,
    @ApplicationContext val context: Context,
    private val sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            return UserViewModel(userRepository, firestore, context,sharedPreferences) as T
        }

        throw IllegalArgumentException("Wrong class")
    }
}