package com.example.ceramicsymphony.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ceramicsymphony.Retrofit.RetrofitClient
import com.example.ceramicsymphony.Retrofit.SealedClassesForResponse.DetailsProductState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailsProductViewModel : ViewModel(){
    private val _detailsProductState = MutableStateFlow<DetailsProductState>(DetailsProductState.Idle)
    val detailsProductState: StateFlow<DetailsProductState> = _detailsProductState

    fun getDetailsProduct(name_product: String){
        viewModelScope.launch {
            try {
                val response = RetrofitClient.retrofitAPI.getDetailsProduct(name_product)
                if (response.isSuccessful){
                    val detailsProduct = response.body()
                    if (detailsProduct?.error_response == null && detailsProduct != null){
                        _detailsProductState.value = DetailsProductState.Success(detailsProduct)
                    }else{
                        _detailsProductState.value = DetailsProductState.Error(detailsProduct?.error_response ?: "")
                    }
                }else{
                    _detailsProductState.value = DetailsProductState.Error("SERVER ERROR: ${response.code()}")
                }
            }catch (e: Exception){
                _detailsProductState.value = DetailsProductState.Error("SERVER CONNECTED ERROR: ${e.message}")
            }
        }
    }
}
