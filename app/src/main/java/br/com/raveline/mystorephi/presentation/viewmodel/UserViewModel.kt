package br.com.raveline.mystorephi.presentation.viewmodel

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.raveline.mystorephi.data.model.AddressModel
import br.com.raveline.mystorephi.data.model.UserModel
import br.com.raveline.mystorephi.domain.repositoryImpl.UserRepositoryImpl
import br.com.raveline.mystorephi.presentation.listener.UiState
import br.com.raveline.mystorephi.utils.*
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repositoryImpl: UserRepositoryImpl,
    private val firestore: FirebaseFirestore,
    @ApplicationContext val context: Context,
    private val sharedPreferences: SharedPreferences
) :
    ViewModel() {
    private val mutableUser = MutableLiveData<UserModel>()
    val userLiveData: LiveData<UserModel> get() = mutableUser

    private val _uiStateFlow = MutableStateFlow<UiState>(UiState.Initial)
    val uiStateFlow: StateFlow<UiState> get() = _uiStateFlow

    init {
        getUserFromPrefs()
    }

    fun addAddress(
        name: String,
        city: String,
        address: String,
        zipCode: String,
        number: String
    ) = viewModelScope.launch {
        try {
            _uiStateFlow.value = UiState.Loading
            if (SystemFunctions.isNetworkAvailable(context)) {
                val addressModel = AddressModel(
                    "${name.trim().capitalize()}, ${city.trim().capitalize()}, ${
                        address.trim().capitalize()
                    }, ${zipCode.trim()}, ${number.trim()}"
                )

                if (checkNonFilledFields(name, city, address, zipCode, number)) {
                    repositoryImpl.saveAddress(userLiveData.value?.uid.toString())
                        .add(addressToMap(addressModel)).addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                _uiStateFlow.value = UiState.Success

                            } else {
                                _uiStateFlow.value = UiState.Error
                            }
                        }
                } else {
                    _uiStateFlow.value = UiState.Error
                }
            } else {
                _uiStateFlow.value = UiState.NoConnection
            }
        } catch (e: Exception) {
            e.printStackTrace()
            _uiStateFlow.value = UiState.Error
        }
    }

    fun registerUser(email: String, password: String, name: String, phone: String) =
        viewModelScope.launch {
            try {
                _uiStateFlow.value = UiState.Loading
                if (SystemFunctions.isNetworkAvailable(context)) {

                    repositoryImpl.saveUserOnServer(email, password).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val userModel = UserModel(
                                uid = task.result.user!!.uid,
                                name = name,
                                email = email,
                                phoneNumber = phone
                            )

                            firestore.collection(userDatabaseReference).document(userModel.uid)
                                .set(userToMap(userModel)).addOnCompleteListener {
                                    if (task.isSuccessful) {
                                        _uiStateFlow.value = UiState.Success
                                        mutableUser.postValue(userModel)
                                        val gson = Gson()
                                        val stringUserJson = gson.toJson(userModel)
                                        sharedPreferences.edit(true) {
                                            putString(userSharedSaved, stringUserJson)
                                        }
                                    } else {
                                        _uiStateFlow.value = UiState.Error
                                    }
                                }
                        } else {
                            _uiStateFlow.value = UiState.Error
                        }
                    }
                } else {
                    _uiStateFlow.value = UiState.NoConnection
                }
            } catch (e: FirebaseAuthException) {
                _uiStateFlow.value = UiState.Error
            }
        }

    fun loginUser(email: String, password: String) = viewModelScope.launch {
        try {
            _uiStateFlow.value = UiState.Loading
            if (SystemFunctions.isNetworkAvailable(context)) {
                repositoryImpl.loginUserFromServer(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        firestore.collection(userDatabaseReference)
                            .document(task.result.user!!.uid)
                            .get()
                            .addOnSuccessListener { result ->

                                val userModel = UserModel(
                                    uid = result.id,
                                    name = result.get(userFieldName).toString(),
                                    email = result.get(userFieldEmail).toString(),
                                    phoneNumber = result.get(userFieldPhoneNumber).toString()
                                )

                                val gson = Gson()
                                val stringUserJson = gson.toJson(userModel)
                                sharedPreferences.edit(true) {
                                    putString(userSharedSaved, stringUserJson)
                                }

                                _uiStateFlow.value = UiState.Success
                            }
                            .addOnFailureListener { exception ->
                                exception.printStackTrace()
                                _uiStateFlow.value = UiState.Error
                            }

                    } else {
                        _uiStateFlow.value = UiState.Error
                    }
                }
            } else {
                _uiStateFlow.value = UiState.NoConnection
            }

        } catch (e: FirebaseAuthException) {
            _uiStateFlow.value = UiState.Error
        }
    }

    private fun checkNonFilledFields(
        name: String,
        city: String,
        address: String,
        zipCode: String,
        number: String
    ): Boolean {
        if (name.isNotEmpty()) {
            if (city.isNotEmpty()) {
                if (address.isNotEmpty()) {
                    if (zipCode.isNotEmpty()) {
                        return number.isNotEmpty()
                    } else {
                        return false
                    }
                } else {
                    return false
                }
            } else {
                return false
            }
        } else {
            return false
        }
    }

    private fun addressToMap(addressModel: AddressModel): HashMap<String, String> =
        hashMapOf(
            addressFieldName to addressModel.address.trim()
        )

    private fun userToMap(userModel: UserModel): HashMap<String, String> {
        return hashMapOf(
            userFieldId to userModel.uid,
            userFieldName to userModel.name,
            userFieldEmail to userModel.email,
            userFieldPhoneNumber to userModel.phoneNumber
        )
    }

    private fun getUserFromPrefs() {
        if (sharedPreferences.contains(userSharedSaved)) {
            val userJson = sharedPreferences.getString(userSharedSaved, null)
            val gson = Gson()
            mutableUser.postValue(gson.fromJson(userJson, UserModel::class.java))
        }
    }

}




