package com.example.ceramicsymphony.Retrofit.SealedClassesForResponse

import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseProductInCategoriesInWarehousesItem

sealed class ProductInCategoriesInWarehousesState {
    object Idle : ProductInCategoriesInWarehousesState()
    object Loading : ProductInCategoriesInWarehousesState()
    data class Success(
        val responseProductInCategoriesInWarehousesItem: ResponseProductInCategoriesInWarehousesItem
    ): ProductInCategoriesInWarehousesState()
    data class Error(
        val message: String
    ): ProductInCategoriesInWarehousesState()
}