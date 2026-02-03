package org.example.app

import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMethod
import io.ktor.serialization.gson.gson
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.application.install
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.cors.routing.CORS
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.routing
import io.ktor.*
import io.ktor.server.response.respond
import io.ktor.server.response.respondFile
import org.example.app.DataClasses.AllProductInFilterCategories
import org.example.app.DataClasses.AllProducts
import org.example.app.DataClasses.AllProductsInWarehouses
import org.example.app.DataClasses.DetailsProduct
import org.example.app.DataClasses.GoodsDelivery
import org.example.app.DataClasses.UserAuthorization
import java.io.File
import java.util.Base64
import kotlin.collections.mapOf


fun main(){
    try {
        val database = DatabaseManagement()
        val server = embeddedServer(Netty, port = 11111, host = "0.0.0.0"){
            module(database)
        }
        Runtime.getRuntime().addShutdownHook(Thread{
            server.stop(1000, 5000)
        })
        server.start(wait = true)

    }catch (e: Exception){
        println("Error: $e")
    }
}

fun Application.module(database: DatabaseManagement){

    install(ContentNegotiation){
        gson()
    }

    install(CORS){
        anyHost()
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Options)
        allowHeader(HttpHeaders.ContentType)
    }

    routing {
        get("/test"){
            call.respondText("SERVER WORK")
        }

        get("/api/database/authorization/{login}/{password}"){
            try {
                val login = call.parameters["login"].toString() ?: ""
                val password = call.parameters["password"].toString() ?: ""
                if (login == "" || password == ""){
                    call.respond(UserAuthorization(
                        id_users = null,
                        name_user = null,
                        login = null,
                        password = null,
                        role = null,
                        error_response = "PLEASE GET CORRECT DATA..."
                    ))
                    return@get
                }
                val users = database.getUserByLoginAndPassword(login, password)
                if (users != null){
                    call.respond(UserAuthorization(
                        id_users = users.id_users,
                        name_user = users.name_user,
                        login = users.login,
                        password = users.password,
                        role = users.role
                    ))
                }else{
                    call.respond(UserAuthorization(
                        id_users = null,
                        name_user = null,
                        login = null,
                        password = null,
                        role = null,
                        error_response = "ERROR 404: USER NOT FOUND!"
                    ))
                }
            }catch (e: Exception){
                call.respond(UserAuthorization(
                    id_users = null,
                    name_user = null,
                    login = null,
                    password = null,
                    role = null,
                    error_response = "SERVER ERROR: $e"
                ))
            }
        }

        get("/api/database/products"){
            try {
                val products = database.getAllProducts()
                if (products.isEmpty()){
                    call.respond(mapOf(
                        "success" to false,
                        "message" to "плитка не найдена",
                        "data" to emptyList<AllProducts>()
                    ))
                }else{
                    call.respond(mapOf(
                        "success" to true,
                        "message" to "плитка найдена",
                        "data" to products
                    ))
                }
            }catch (e: Exception){
                call.respond(mapOf(
                    "success" to false,
                    "message" to "server error: ${e.message}",
                    "data" to emptyList<AllProducts>()
                ))
            }
        }

        get("/api/database/productFromWarehouses"){
            try {
                val productsInWarehouses = database.getProductsInWarehouses()
                if (productsInWarehouses.isEmpty()){
                    call.respond(mapOf(
                        "success" to false,
                        "message" to "плитка не найдена",
                        "data" to emptyList<AllProductsInWarehouses>()
                    ))
                }else{
                    call.respond(mapOf(
                        "success" to true,
                        "message" to "плитка найдена",
                        "data" to productsInWarehouses
                    ))
                }
            }catch (e: Exception){
                call.respond(mapOf(
                    "success" to false,
                    "message" to "server error: ${e.message}",
                    "data" to emptyList<AllProductsInWarehouses>()
                ))
            }
        }

        get("api/database/productInFilterCategories/{nameCategories}"){
            val nameCategories = call.parameters["nameCategories"].toString() ?: ""
            try {
                val productInFilterCategories = database.getCategoriesTile(nameCategories)
                if (nameCategories == ""){
                    call.respond(mapOf(
                        "success" to false,
                        "message" to "не правильные данные",
                        "data" to emptyList<AllProductInFilterCategories>()
                    ))
                }else{
                    if (productInFilterCategories.isEmpty()){
                        call.respond(mapOf(
                            "success" to false,
                            "message" to "плитка с такой категории не найдены",
                            "data" to emptyList<AllProductInFilterCategories>()
                        ))
                    }else{
                        call.respond(mapOf(
                            "success" to true,
                            "message" to "плитка найдена",
                            "data" to productInFilterCategories
                        ))
                    }
                }
            }catch (e: Exception){
                call.respond(mapOf(
                    "success" to false,
                    "message" to "server error: ${e.message}",
                    "data" to emptyList<AllProductInFilterCategories>()
                ))
            }
        }

        get("/api/database/getGoodsDeliveryByDate/{date}"){
            try {
                val date = call.parameters["date"].toString() ?: ""
                if (date == ""){
                    call.respond(mapOf("ERROR" to "NOT CORRECT DATE"))
                    return@get
                }else{
                    val goodsDeliveryByDate = database.getGoodsDeliveryByDate(date)
                    if (goodsDeliveryByDate != null){
                        call.respond(goodsDeliveryByDate)
                    }
                }
            }catch (e: Exception){
                call.respond(mapOf("EXCEPTION!" to "SERVER ERROR: $e"))
            }
        }

        get("/api/database/productInFilterCategoriesInWarehouses/{nameCategories}"){
            val nameCategories = call.parameters["nameCategories"].toString() ?: ""
            try {
                val products = database.getCategoriesTileInWarehouses(nameCategories)

                if (nameCategories.isEmpty()){
                    call.respond(mapOf(
                        "success" to false,
                        "message" to "не правильные данные",
                        "data" to emptyList<AllProductsInWarehouses>()
                    ))
                }else {
                    if (products.isEmpty()){
                        call.respond(mapOf(
                            "success" to false,
                            "message" to "плитки с такой категорией нет на складе",
                            "data" to emptyList<AllProductsInWarehouses>()
                        ))
                    }else{
                        call.respond(mapOf(
                            "success" to true,
                            "message" to "плитка есть на складе",
                            "data" to products
                        ))
                    }
                }
            }catch (e: Exception){
                call.respond(mapOf(
                    "success" to false,
                    "message" to "server error: ${e.message}",
                    "data" to emptyList<AllProductsInWarehouses>()
                ))
            }
        }

        get("/api/database/detailsProduct/{name_product}"){
            val name_product = call.parameters["name_product"].toString() ?: ""
            try {
                val detailsProduct = database.getDetailsProduct(name_product)
                if (detailsProduct.isEmpty()){
                    call.respond(mapOf(
                        "success" to false,
                        "message" to "данные не найдены",
                        "data" to emptyList<DetailsProduct>()
                    ))
                }else{
                    call.respond(mapOf(
                        "success" to true,
                        "message" to "данные о продукте",
                        "data" to detailsProduct
                    ))
                }
            }catch (e: Exception){
                call.respond(mapOf(
                    "success" to false,
                    "message" to "server error: ${e.message}",
                    "data" to emptyList<DetailsProduct>()
                ))
            }
        }

        get("/api/database/getGoodsDelivery"){
            try {
                val goodsDelivery = database.getAllGoodsDeliveryForAdmin()
                if (goodsDelivery.isEmpty()){
                    call.respond(mapOf(
                        "success" to false,
                        "message" to "данные отсутствуют",
                        "data" to emptyList<GoodsDelivery>()
                    ))
                }else{
                    call.respond(mapOf(
                        "success" to true,
                        "message" to "данные о доставке",
                        "data" to goodsDelivery
                    ))
                }
            }catch (e: Exception){
                call.respond(mapOf(
                    "success" to false,
                    "message" to "server error: ${e.message}",
                    "data" to emptyList<GoodsDelivery>()
                ))
            }
        }

        get("/api/getLogoCompany"){
            val imageLogoCompanyFile = File("app/src/main/resources/image_authorizationScreen/logo_company.jpg")
            if (imageLogoCompanyFile.exists()){
                call.respondFile(imageLogoCompanyFile)
            }else{
                call.respond(mapOf("SERVER ERROR" to "IMAGE NOT FOUND"))
            }
        }

        get("/api/getGifTile"){
            val gifTileFile = File("app/src/main/resources/Gifs/tile_gif.gif")
            if (gifTileFile.exists()){
                call.respondFile(gifTileFile)
            }else{
                call.respond(mapOf("SERVER ERROR" to "GIF NOT FOUND"))
            }
        }

        get("/api/getFullLogoCompany"){
            val fullImageLogoCompany = File("app/src/main/resources/image_presentationScreen/fullLogoCompany.png")
            if (fullImageLogoCompany.exists()){
                call.respondFile(fullImageLogoCompany)
            }else{
                call.respond(mapOf("SERVER ERROR" to "IMAGE NOT FOUND"))
            }
        }
    }
}
