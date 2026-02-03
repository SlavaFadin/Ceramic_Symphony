package com.example.ceramicsymphony.Retrofit.ResponseDataClasses

data class ResponseProductInCategoriesInWarehousesItem(
    val success: Boolean? = null,
    val message: String? = null,
    val data: List<ResponseProductInCategoriesInWarehouses>? = null,
    val error_response: String? = null
)
