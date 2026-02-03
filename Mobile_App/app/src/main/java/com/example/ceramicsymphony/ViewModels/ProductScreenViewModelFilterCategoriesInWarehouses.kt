package com.example.ceramicsymphony.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ceramicsymphony.Retrofit.RetrofitClient
import com.example.ceramicsymphony.Retrofit.SealedClassesForResponse.ProductInCategoriesInWarehousesState
import com.example.ceramicsymphony.Retrofit.SealedClassesForResponse.ProductInFilterCategoriesState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.net.URLEncoder

class ProductScreenViewModelFilterCategoriesInWarehouses : ViewModel() {
    private val _productScreenViewModelFilterCategoriesInWarehouses = MutableStateFlow<ProductInCategoriesInWarehousesState>(ProductInCategoriesInWarehousesState.Idle)
    val productScreenViewModelFilterCategoriesInWarehouses: StateFlow<ProductInCategoriesInWarehousesState> = _productScreenViewModelFilterCategoriesInWarehouses

    fun getAllProductInFilterCategoriesInWarehouses(nameCategories: String){
        viewModelScope.launch {
            _productScreenViewModelFilterCategoriesInWarehouses.value = ProductInCategoriesInWarehousesState.Loading
            try {
                val response = RetrofitClient.retrofitAPI.getAllProductInFilterCategoriesInWarehouses(nameCategories)

                if (response.isSuccessful){
                    val data = response.body()
                    if (data?.success == true){
                        _productScreenViewModelFilterCategoriesInWarehouses.value = ProductInCategoriesInWarehousesState.Success(data)
                    }else{
                        _productScreenViewModelFilterCategoriesInWarehouses.value = ProductInCategoriesInWarehousesState.Error(data?.message ?: "UNKNOWING ERROR")
                    }
                }else{
                    _productScreenViewModelFilterCategoriesInWarehouses.value = ProductInCategoriesInWarehousesState.Error("SERVER ERROR ${response.code()}")
                }
            }catch (e: Exception){
                _productScreenViewModelFilterCategoriesInWarehouses.value = ProductInCategoriesInWarehousesState.Error("SERVER CONNECTED ERROR ${e.message}")
            }
        }
    }
}