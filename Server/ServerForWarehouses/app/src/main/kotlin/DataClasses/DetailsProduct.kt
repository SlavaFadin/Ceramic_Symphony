package org.example.app.DataClasses

data class DetailsProduct(
    val id_products: Int?,
    val article: String?,
    val name_products: String?,
    val size: String?,
    val unit: String?,
    val color: String?,
    val image_name: String?,
    val image: String? = null,
    val name_categories: String?,
    val description: String?,
    val quantity: String?,
    val name_manufacturer: String?,
    val status_: String?
)
