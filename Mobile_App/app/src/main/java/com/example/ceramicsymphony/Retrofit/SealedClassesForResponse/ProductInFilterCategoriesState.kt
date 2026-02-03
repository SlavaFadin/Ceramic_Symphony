package com.example.ceramicsymphony.Retrofit.SealedClassesForResponse

import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseProductInFilterCategoriesItem

sealed class ProductInFilterCategoriesState {
    object Idle: ProductInFilterCategoriesState()
    object Loading: ProductInFilterCategoriesState()
    data class Success(
        val responseProductInFilterCategoriesItem: ResponseProductInFilterCategoriesItem
    ): ProductInFilterCategoriesState()
    data class Error(
        val message: String
    ): ProductInFilterCategoriesState()
}
