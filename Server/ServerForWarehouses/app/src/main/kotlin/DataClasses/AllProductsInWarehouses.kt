package org.example.app.DataClasses

data class AllProductsInWarehouses(
    val name_products: String,
    val colInWarehouses: Int? = null,
    val status: String? = null,
    val error_response: String? = null,
    val image_name: String?,
    val image: String? = null,
    val size: String?,
    val article: String?
)