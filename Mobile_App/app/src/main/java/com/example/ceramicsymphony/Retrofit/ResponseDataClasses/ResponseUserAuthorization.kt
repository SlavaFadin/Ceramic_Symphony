package com.example.ceramicsymphony.Retrofit.ResponseDataClasses

data class ResponseUserAuthorization(
    val id_users: Int?,
    val name_user: String?,
    val login: String?,
    val password: String?,
    val role: String?,
    val error_response: String? = null
)
