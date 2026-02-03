package org.example.app.DataClasses

data class AllProducts(
    val id_products: Int? = null,
    val article: String,
    val name_products: String,
    val size: String,
    val image_name: String,
    val image: String? = null,
    val error_response: String? = null
)
