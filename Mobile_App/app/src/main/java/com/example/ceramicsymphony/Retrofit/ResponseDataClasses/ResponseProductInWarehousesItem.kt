package com.example.ceramicsymphony.Retrofit.ResponseDataClasses

import kotlinx.serialization.Serializable

@Serializable
data class ResponseProductInWarehousesItem(
    val success: Boolean? = null,
    val message: String? = null,
    val data: List<RespondAllProductInWarehouses>? = null,
    val error_response: String? = null
)
