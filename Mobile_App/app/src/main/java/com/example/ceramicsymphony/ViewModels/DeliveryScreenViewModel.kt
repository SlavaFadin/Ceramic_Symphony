package com.example.ceramicsymphony.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ceramicsymphony.Retrofit.RetrofitClient
import com.example.ceramicsymphony.Retrofit.SealedClassesForResponse.GoodsDeliveryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeliveryScreenViewModel : ViewModel() {
    private val _goodsDeliveryState = MutableStateFlow<GoodsDeliveryState>(GoodsDeliveryState.Idle)
    val goodsDeliveryState: StateFlow<GoodsDeliveryState> = _goodsDeliveryState

    fun getAllGoodsDelivery(){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.retrofitAPI.getAllGoodsDelivery()
                if (response.isSuccessful){
                    val goodsDelivery = response.body()
                    if (goodsDelivery?.error_response == null && goodsDelivery != null){
                        _goodsDeliveryState.value = GoodsDeliveryState.Success(goodsDelivery)
                    }else{
                        _goodsDeliveryState.value = GoodsDeliveryState.Error(goodsDelivery?.error_response ?: "")
                    }
                }else{
                    _goodsDeliveryState.value = GoodsDeliveryState.Error("SERVER ERROR: ${response.code()}")
                }
            }catch (e: Exception){
                _goodsDeliveryState.value = GoodsDeliveryState.Error("SERVER CONNECTED ERROR: ${e.message}")
            }
        }
    }
}
