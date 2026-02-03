package com.example.ceramicsymphony.Retrofit.SealedClassesForResponse

import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseAllProducts
import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseProductItem

sealed class ProductsState {
    object Idle : ProductsState()
    object Loading : ProductsState()
    data class Success(
        val responseAllProducts: ResponseProductItem
    ): ProductsState()
    data class Error(
        val message: String
    ): ProductsState()
}
