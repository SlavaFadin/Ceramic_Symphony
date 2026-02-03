package com.example.ceramicsymphony.Retrofit.ResponseDataClasses

data class ResponseProductInCategoriesInWarehouses(
    val name_products: String,
    val colInWarehouses: Int? = null,
    val status: String?,
    val error_response: String? = null,
    val image_name: String?,
    val image: String? = null,
    val size: String?,
    val article: String?
)
