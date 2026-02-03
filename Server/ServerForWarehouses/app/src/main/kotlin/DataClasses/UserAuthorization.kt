package org.example.app.DataClasses

data class UserAuthorization(
    val id_users: Int?,
    val name_user: String?,
    val login: String?,
    val password: String?,
    val role: String?,
    val error_response: String? = null
)
