package com.example.ceramicsymphony.Retrofit

import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseAllProducts
import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseDeliveryScreenGoodsDeliveryItem
import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseDetailsProductItem
import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseProductInCategoriesInWarehousesItem
import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseProductInFilterCategoriesItem
import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseProductInWarehousesItem
import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseProductItem
import com.example.ceramicsymphony.Retrofit.ResponseDataClasses.ResponseUserAuthorization
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Streaming

interface RetrofitAPI {
//    @GET("test")
//    suspend fun testConnection(): Response<String>

    @GET("api/database/authorization/{login}/{password}")
    suspend fun authorisationUser(
        @Path("login") login: String,
        @Path("password") password: String
    ): Response<ResponseUserAuthorization>


    @GET("api/getLogoCompany")
    @Streaming
    suspend fun getLogoCompany(): Response<ResponseBody>


    @GET("api/getGifTile")
    @Streaming
    suspend fun getGifTile(): Response<ResponseBody>


    @GET("api/database/products")
    suspend fun getAllProducts(): Response<ResponseProductItem>


    @GET("api/database/productFromWarehouses")
    suspend fun getAllProductsInWarehouses(): Response<ResponseProductInWarehousesItem>


    @GET("api/database/productInFilterCategories/{nameCategories}")
    suspend fun getFilterCategories(
        @Path("nameCategories") nameCategories: String
    ): Response<ResponseProductInFilterCategoriesItem>


    @GET("api/database/productInFilterCategoriesInWarehouses/{nameCategories}")
    suspend fun getAllProductInFilterCategoriesInWarehouses(
        @Path("nameCategories") nameCategories: String
    ): Response<ResponseProductInCategoriesInWarehousesItem>


    @GET("api/database/detailsProduct/{name_product}")
    suspend fun getDetailsProduct(
        @Path("name_product") name_product: String
    ): Response<ResponseDetailsProductItem>

    @GET("api/database/getGoodsDelivery")
    suspend fun getAllGoodsDelivery(): Response<ResponseDeliveryScreenGoodsDeliveryItem>
}
