package com.example.ceramicsymphony.Retrofit.SealedClassesForResponse

import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseDeliveryScreenGoodsDeliveryItem

sealed class GoodsDeliveryState {
    object Idle: GoodsDeliveryState()
    object Loading: GoodsDeliveryState()
    data class Success(
        val responseDeliveryScreenGoodsDeliveryItem: ResponseDeliveryScreenGoodsDeliveryItem
    ): GoodsDeliveryState()
    data class Error(
        val message: String
    ): GoodsDeliveryState()
}