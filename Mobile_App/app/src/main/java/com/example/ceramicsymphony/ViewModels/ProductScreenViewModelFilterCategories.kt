package com.example.ceramicsymphony.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ceramicsymphony.Retrofit.RetrofitClient
import com.example.ceramicsymphony.Retrofit.SealedClassesForResponse.ProductInFilterCategoriesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductScreenViewModelFilterCategories : ViewModel() {
    private val _productInCategoriesState = MutableStateFlow<ProductInFilterCategoriesState>(ProductInFilterCategoriesState.Idle)
    val productInCategoriesState: StateFlow<ProductInFilterCategoriesState> = _productInCategoriesState

    fun getAllProductInFilterCategories(categoriesName: String){
        viewModelScope.launch {
            _productInCategoriesState.value = ProductInFilterCategoriesState.Loading
            try {
                val response = RetrofitClient.retrofitAPI.getFilterCategories(categoriesName)
                if (response.isSuccessful){
                    val productResponseFilterCategoriesData = response.body()
                    if (productResponseFilterCategoriesData?.error_response == null && productResponseFilterCategoriesData != null){
                        _productInCategoriesState.value = ProductInFilterCategoriesState.Success(productResponseFilterCategoriesData)
                    }else{
                        _productInCategoriesState.value = ProductInFilterCategoriesState.Error(productResponseFilterCategoriesData?.error_response ?: "")
                    }
                }else{
                    _productInCategoriesState.value = ProductInFilterCategoriesState.Error("SERVER ERROR: ${response.code()}")
                }
            }catch (e:Exception){
                _productInCategoriesState.value = ProductInFilterCategoriesState.Error("SERVER CONNECTION ERROR: ${e.message}")
            }
        }
    }
}
