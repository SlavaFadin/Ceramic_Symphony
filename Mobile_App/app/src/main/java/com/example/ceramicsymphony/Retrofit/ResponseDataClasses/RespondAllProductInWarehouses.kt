package com.example.ceramicsymphony.Retrofit.ResponseDataClasses

import kotlinx.serialization.Serializable

@Serializable
data class RespondAllProductInWarehouses(
    val name_products: String,
    val colInWarehouses: Int,
    val status: String,
    val error_response: String? = null,
    val image_name: String?,
    val image: String? = null,
    val size: String?,
    val article: String?
)
