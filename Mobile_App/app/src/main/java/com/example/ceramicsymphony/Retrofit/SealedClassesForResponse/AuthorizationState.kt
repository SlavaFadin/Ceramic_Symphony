package com.example.ceramicsymphony.Retrofit.SealedClassesForResponse

import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseUserAuthorization

sealed class AuthorizationState {
    object Idle : AuthorizationState()
    object Loading : AuthorizationState()
    data class Success(
        val responseUserAuthorization: ResponseUserAuthorization
    ): AuthorizationState()
    data class Error(
        val message: String
    ): AuthorizationState()
}