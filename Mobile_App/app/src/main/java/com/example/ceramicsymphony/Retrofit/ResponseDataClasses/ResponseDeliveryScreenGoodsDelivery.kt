package com.example.ceramicsymphony.Retrofit.ResponseDataClasses

data class ResponseDeliveryScreenGoodsDelivery(
    val name_products: String,
    val article: String,
    val date_delivery: String,
    val document: String,
    val comment: String,
    val name_manufacturer: String,
    val error_message: String? = null
)
