package com.example.ceramicsymphony.Retrofit.ResponseDataClasses

data class ResponseDetailsProductItem(
    val success: Boolean? = null,
    val message: String? = null,
    val data: List<ResponseDetailsProduct>? = null,
    val error_response: String? = null
)
