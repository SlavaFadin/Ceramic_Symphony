package com.example.ceramicsymphony.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ceramicsymphony.Retrofit.RetrofitClient
import com.example.ceramicsymphony.Retrofit.SealedClassesForResponse.AuthorizationState
import com.example.ceramicsymphony.Retrofit.SealedClassesForResponse.ProductsState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductScreenViewModel : ViewModel() {
    private val _productsState = MutableStateFlow<ProductsState>(ProductsState.Idle)
    val productsState: StateFlow<ProductsState> = _productsState

    fun getAllProducts(){
        viewModelScope.launch {
            _productsState.value = ProductsState.Loading
            try {
                val response = RetrofitClient.retrofitAPI.getAllProducts()
                if (response.isSuccessful){
                    val productsResponse = response.body()
                    if (productsResponse != null){
                        _productsState.value = ProductsState.Success(productsResponse)
                    }else{
                        _productsState.value = ProductsState.Error(productsResponse?.error_response ?: "ERROR 404: NO PRODUCTS")
                    }
                }else{
                    _productsState.value = ProductsState.Error("SERVER ERROR: ${response.code()}")
                }
            }catch (e: Exception){
                _productsState.value = ProductsState.Error("CONNECTION SERVER ERROR: ${e.message}")
            }
        }
    }
}
