package com.example.ceramicsymphony.Retrofit.ResponseDataClasses

data class ResponseProductInFilterCategoriesItem(
    val success: Boolean? = null,
    val message: String? = null,
    val data: List<ResponseAllProductInFilterCategories>? = null,
    val error_response: String? = null
)
