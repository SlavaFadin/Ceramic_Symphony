package com.example.ceramicsymphony.Retrofit.SealedClassesForResponse

import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseProductInWarehousesItem


sealed class ProductInWarehousesState {
    object Idle : ProductInWarehousesState()
    object Loading : ProductInWarehousesState()
    data class Success(
        val responseProductInWarehousesItem: ResponseProductInWarehousesItem
    ): ProductInWarehousesState()
    data class Error(
        val message: String
    ): ProductInWarehousesState()

}