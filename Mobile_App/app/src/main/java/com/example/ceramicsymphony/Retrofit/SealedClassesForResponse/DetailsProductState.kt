package com.example.ceramicsymphony.Retrofit.SealedClassesForResponse

import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseDetailsProductItem

sealed class DetailsProductState {
    object Idle : DetailsProductState()
    object Loading : DetailsProductState()
    data class Success(
        val responseDetailsProductItem: ResponseDetailsProductItem
    ): DetailsProductState()
    data class Error(
        val message: String
    ): DetailsProductState()
}
