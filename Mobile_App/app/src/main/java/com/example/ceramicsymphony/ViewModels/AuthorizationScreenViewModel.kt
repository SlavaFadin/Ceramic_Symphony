package com.example.ceramicsymphony.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseUserAuthorization
import com.example.ceramicsymphony.Retrofit.RetrofitClient
import com.example.ceramicsymphony.Retrofit.SealedClassesForResponse.AuthorizationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthorizationScreenViewModel: ViewModel() {
    private val _authorizationState = MutableStateFlow<AuthorizationState>(AuthorizationState.Idle)
    val authorizationState: StateFlow<AuthorizationState> = _authorizationState

    private val _currentUser = MutableStateFlow<ResponseUserAuthorization?>(null)
    val currentUser: StateFlow<ResponseUserAuthorization?> = _currentUser.asStateFlow()

    fun authorizationUser(login: String, password: String){
//        _authorizationState.value = AuthorizationState.Loading
        viewModelScope.launch {
            _authorizationState.value = AuthorizationState.Loading
            try {
                val response = RetrofitClient.retrofitAPI.authorisationUser(login, password)
                if (response.isSuccessful){
                    val userResponseData = response.body()
                    if (userResponseData?.error_response == null && userResponseData?.id_users != null){
                        _currentUser.value = userResponseData
                        _authorizationState.value = AuthorizationState.Success(userResponseData)
                    }else{
                        _authorizationState.value = AuthorizationState.Error(userResponseData?.error_response ?: "ERROR 404: USER NOT FOUND")
                    }
                }else{
                    _authorizationState.value = AuthorizationState.Error("SERVER ERROR: ${response.code()}")
                }
            }catch (e: Exception){
                _authorizationState.value = AuthorizationState.Error("CONNECTION SERVER ERROR: ${e.message}")
            }
        }
    }
}
