package com.example.ceramicsymphony.Retrofit.ResponseDataClasses

import kotlinx.serialization.Serializable

@Serializable
data class ResponseProductItem(
    val success: Boolean? = null,
    val message: String? = null,
    val data: List<ResponseAllProducts>? = null,
    val error_response: String? = null
)
