package com.example.ceramicsymphony.Retrofit.ResponseDataClasses

data class ResponseDeliveryScreenGoodsDeliveryItem(
    val success: Boolean? = null,
    val message: String? = null,
    val data: List<ResponseDeliveryScreenGoodsDelivery>? = null,
    val error_response: String? = null
)
