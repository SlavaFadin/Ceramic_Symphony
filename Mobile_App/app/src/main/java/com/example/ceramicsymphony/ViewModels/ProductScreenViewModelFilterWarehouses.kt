package com.example.ceramicsymphony.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ceramicsymphony.Retrofit.RetrofitClient
import com.example.ceramicsymphony.Retrofit.SealedClassesForResponse.ProductInWarehousesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductScreenViewModelFilterWarehouses : ViewModel() {
    private val _productInWarehousesState = MutableStateFlow<ProductInWarehousesState>(ProductInWarehousesState.Idle)
    val productInWarehousesState: StateFlow<ProductInWarehousesState> = _productInWarehousesState

    fun getAllProductInWarehouses(){
        viewModelScope.launch {
            _productInWarehousesState.value = ProductInWarehousesState.Loading
            try {
                val response = RetrofitClient.retrofitAPI.getAllProductsInWarehouses()
                if (response.isSuccessful){
                    val productInWarehousesResponse = response.body()
                    if (productInWarehousesResponse != null){
                        _productInWarehousesState.value = ProductInWarehousesState.Success(productInWarehousesResponse)
                    }else{
                        _productInWarehousesState.value = ProductInWarehousesState.Error(productInWarehousesResponse?.error_response ?: "")
                    }
                }else{
                    _productInWarehousesState.value = ProductInWarehousesState.Error("SERVER ERROR: ${response.code()}")
                }
            }catch (e: Exception){
                _productInWarehousesState.value = ProductInWarehousesState.Error("SERVER CONNECTION ERROR: ${e.message}")
            }
        }
    }
}
