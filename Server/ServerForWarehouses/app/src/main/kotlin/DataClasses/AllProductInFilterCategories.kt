package org.example.app.DataClasses

data class AllProductInFilterCategories(
    val name_products: String?,
    val image_name: String?,
    val image: String? = null,
    val size: String?,
    val article: String?,
    val error_response: String? = null,
)
