package org.example.app

import org.example.app.DataClasses.AllProductInFilterCategories
import org.example.app.DataClasses.AllProducts
import org.example.app.DataClasses.AllProductsInWarehouses
import org.example.app.DataClasses.DetailsProduct
import org.example.app.DataClasses.GoodsDelivery
import org.example.app.DataClasses.UserAuthorization
import java.io.File
import java.sql.*
import java.util.Base64

class DatabaseManagement {
    private val connection: Connection

    init {
        val url = "jdbc:sqlserver://localhost:1433;databaseName=PP01;encrypt=true;trustServerCertificate=true;"
        val user = "programming_user"
        val password = "123456789"

        connection = DriverManager.getConnection(url, user, password)
        println("\u001b[32mConnection OK!!!\u001b[0m")
        testConnection()
    }

    private fun testConnection(){
        try {
            val statement = connection.createStatement()
            val resultSet = statement.executeQuery("SELECT @@VERSION as version")
            if (resultSet.next()){
                println("SQL VERSION: ${resultSet.getString("version").substring(0, 50)}")
            }
        }catch (e: Exception){
            println("EXCEPTION: ${e.message}")
        }
    }

    fun getUserByLoginAndPassword(login: String, password: String): UserAuthorization?{
        val sql = "SELECT * FROM Users$ WHERE login=? AND password=?"
        return try {
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, login)
                statement.setString(2, password)
                val resultSet = statement.executeQuery()
                if (resultSet.next()){
                    UserAuthorization(
                        id_users = resultSet.getInt("id_users"),
                        name_user = resultSet.getString("name_user"),
                        login = resultSet.getString("login"),
                        password = resultSet.getString("password"),
                        role = resultSet.getString("role")
                    )
                }else{
                    null
                }
            }
        }catch (e: SQLException){
            println("ERROR: $e")
            null
        }
    }

    fun getAllProducts(): List<AllProducts>{
        val sql = "SELECT * FROM Products$"
        val products = mutableListOf<AllProducts>()
        return try {
            connection.prepareStatement(sql).use { statement ->
                val resultSet = statement.executeQuery()
                while (resultSet.next()){
                    val imageName = resultSet.getString("image_name")
                    val imagePath = "app/src/main/resources/images_tile/$imageName"
                    val imageBase64 = File(imagePath).takeIf { it.exists() }?.readBytes()?.let {
                        Base64.getEncoder().encodeToString(it)
                    }
                    products.add(
                        AllProducts(
                            id_products = resultSet.getInt("id_products"),
                            name_products = resultSet.getString("name_products"),
                            article = resultSet.getString("article"),
                            size = resultSet.getString("size"),
                            image_name = resultSet.getString("image_name"),
                            image = imageBase64
                        )
                    )
                }
            }
            products
        }catch (e: SQLException){
            println("SERVER ERROR $e")
            emptyList()
        }
    }

    fun getProductsInWarehouses(): List<AllProductsInWarehouses>{
        val sql = """
            SELECT Products$.name_products, Products$.article, Products$.size, Products$.image_name,
            COALESCE(SUM(Stock_balance$.quantity), 0) AS col_in_warehouses,
            CASE WHEN SUM(Stock_balance$.quantity) IS NULL OR SUM(Stock_balance$.quantity) = 0
            THEN 'НЕТ НА СКЛАДЕ'
            ELSE 'ЕСТЬ НА СКЛАДЕ'
            END AS status_
            FROM Products$ LEFT JOIN Stock_balance$ ON Products$.id_products = Stock_balance$.id_products
            GROUP BY Products$.id_products, Products$.name_products, Products$.article, Products$.size, Products$.image_name
            HAVING COALESCE(SUM(Stock_balance$.quantity), 0) > 0
            ORDER BY Products$.name_products
        """
        val productsInWarehouses = mutableListOf<AllProductsInWarehouses>()
        return try {
            connection.prepareStatement(sql).use { statement ->
                val resultSet = statement.executeQuery()
                while (resultSet.next()){
                    val imageName = resultSet.getString("image_name")
                    val imagePath = "app/src/main/resources/images_tile/$imageName"
                    val imageBase64 = File(imagePath).takeIf { it.exists() }?.readBytes()?.let {
                        Base64.getEncoder().encodeToString(it)
                    }
                    productsInWarehouses.add(
                        AllProductsInWarehouses(
                            name_products = resultSet.getString("name_products"),
                            colInWarehouses = resultSet.getInt("col_in_warehouses"),
                            status = resultSet.getString("status_"),
                            article = resultSet.getString("article"),
                            size = resultSet.getString("size"),
                            image_name = resultSet.getString("image_name"),
                            image = imageBase64,
                        )
                    )
                }
            }
            productsInWarehouses
        }catch (e: SQLException){
            println("ERROR: $e")
            emptyList()
        }
    }

    fun getGoodsDeliveryByDate(date: String?): GoodsDelivery?{
        val sql = """
            SELECT p.name_products, p.article, p.image_name, gd.document, c.name_categories, c.description, m.name_manufacturer
            FROM Products$ p
            INNER JOIN Goods_delivery$ gd ON gd.id_products = p.id_products
            INNER JOIN Categories$ c ON p.id_categories = c.id_categories
            INNER JOIN Manufacturer$ m ON m.id_manufacturer = p.id_manufacturer
            WHERE gd.created_at = ?;
        """
        return try {
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, date)
                val resultSet = statement.executeQuery()
                if (resultSet.next()){
                    GoodsDelivery(
                        name_products = resultSet.getString("name_products"),
                        article = resultSet.getString("article"),
                        document = resultSet.getString("document"),
                        date_delivery = resultSet.getString("created_at"),
                        comment = resultSet.getString("comment"),
                        name_manufacturer = resultSet.getString("name_manufacturer")
                    )
                }else{
                    null
                }
            }
        }catch (e: SQLException){
            println("ERROR: $e")
            null
        }
    }

    fun getAllGoodsDeliveryForAdmin(): List<GoodsDelivery>{
        val sql = """
		    SELECT p.name_products, p.article, gd.created_at, gd.document, gd.comment, m.name_manufacturer
            FROM Products$ p
            INNER JOIN Goods_delivery$ gd ON gd.id_products = p.id_products
            INNER JOIN Categories$ c ON p.id_categories = c.id_categories
            INNER JOIN Manufacturer$ m ON m.id_manufacturer = p.id_manufacturer
        """
        val goodsDelivery = mutableListOf<GoodsDelivery>()
        return try {
            connection.prepareStatement(sql).use { statement ->
                val resultSet = statement.executeQuery()
                while (resultSet.next()){
                    goodsDelivery.add(
                        GoodsDelivery(
                            name_products = resultSet.getString("name_products"),
                            article = resultSet.getString("article"),
                            document = resultSet.getString("document"),
                            date_delivery = resultSet.getString("created_at"),
                            comment = resultSet.getString("comment"),
                            name_manufacturer = resultSet.getString("name_manufacturer")
                        )
                    )
                }
            }
            goodsDelivery
        }catch (e: SQLException){
            println("ERROR: $e")
            emptyList()
        }
    }

    fun getCategoriesTile(nameCategories: String): List<AllProductInFilterCategories>{
        val sql = """
            SELECT Products$.name_products, Products$.article, Products$.size, Products$.image_name
            FROM Products$
            LEFT JOIN Categories$ ON Categories$.id_categories = Products$.id_categories
            WHERE Categories$.name_categories = ?
        """
        val allProductInFilterCategories = mutableListOf<AllProductInFilterCategories>()
        return try {
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, nameCategories)
                val resultSet = statement.executeQuery()
                while(resultSet.next()){
                    val imageName = resultSet.getString("image_name")
                    val imagePath = "app/src/main/resources/images_tile/$imageName"
                    val imageBase64 = File(imagePath).takeIf { it.exists() }?.readBytes()?.let {
                        Base64.getEncoder().encodeToString(it)
                    }
                    allProductInFilterCategories.add(
                        AllProductInFilterCategories(
                            name_products = resultSet.getString("name_products"),
                            article = resultSet.getString("article"),
                            size = resultSet.getString("size"),
                            image_name = resultSet.getString("image_name"),
                            image = imageBase64
                        )
                    )
                }
            }
            allProductInFilterCategories
        }catch (e: SQLException){
            println("ERROR: $e")
            emptyList()
        }
    }

    fun getCategoriesTileInWarehouses(nameCategories: String): List<AllProductsInWarehouses>{
        val sql = """
            SELECT p.name_products, p.article, p.size, p.image_name,
            COALESCE(SUM(s.quantity), 0) AS col_in_warehouses,
            CASE WHEN SUM(s.quantity) IS NULL OR SUM(s.quantity) = 0
            THEN 'НЕТ НА СКЛАДЕ'
            ELSE 'ЕСТЬ НА СКЛАДЕ'
            END AS status_
            FROM Products$ p
            LEFT JOIN Categories$ c ON c.id_categories = p.id_categories
            LEFT JOIN Stock_balance$ s ON s.id_products = p.id_products
            WHERE c.name_categories = ?
            AND (s.quantity IS NULL OR s.quantity > 0)
            GROUP BY p.id_products, p.name_products, p.article, p.size, p.image_name
            HAVING COALESCE(SUM(s.quantity), 0) > 0
            ORDER BY p.name_products
        """
        val allProductsFilterCategoriesInWarehouses = mutableListOf<AllProductsInWarehouses>()
        return try {
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, nameCategories)
                val resultSet = statement.executeQuery()

                while (resultSet.next()){
                    val imageName = resultSet.getString("image_name")
                    val imagePath = "app/src/main/resources/images_tile/$imageName"
                    val imageBase64 = File(imagePath).takeIf { it.exists() }?.readBytes()?.let {
                        Base64.getEncoder().encodeToString(it)
                    }
                    allProductsFilterCategoriesInWarehouses.add(
                        AllProductsInWarehouses(
                            name_products = resultSet.getString("name_products"),
                            colInWarehouses = resultSet.getInt("col_in_warehouses"),
                            status = resultSet.getString("status_"),
                            article = resultSet.getString("article"),
                            size = resultSet.getString("size"),
                            image_name = resultSet.getString("image_name"),
                            image = imageBase64
                        )
                    )
                }
            }
            allProductsFilterCategoriesInWarehouses
        }catch (e: SQLException){
            println("ERROR: $e")
            emptyList()
        }
    }

    fun getDetailsProduct(name_product: String?): List<DetailsProduct>{
        val sql = """
            SELECT p.id_products, p.article, p.name_products, p.size, p.unit, p.color, p.image_name, c.name_categories, c.description, s.quantity, m.name_manufacturer,
            COALESCE(SUM(s.quantity), 0) AS col_in_warehouses,
            CASE WHEN SUM(s.quantity) IS NULL OR SUM(s.quantity) = 0
            THEN 'НЕТ НА СКЛАДЕ'
            ELSE 'ЕСТЬ НА СКЛАДЕ'
            END AS status_
            FROM Products$ p
            LEFT JOIN Categories$ c ON c.id_categories = p.id_categories
            LEFT JOIN Stock_balance$ s ON s.id_products = p.id_products
            LEFT JOIN Manufacturer$ m ON m.id_manufacturer = p.id_manufacturer
            WHERE p.name_products = ?
            GROUP BY p.id_products, p.article, p.name_products, p.size, p.unit, p.color, p.image_name, c.name_categories, c.description, s.quantity, m.name_manufacturer
            ORDER BY p.name_products
        """
        val detailsProduct = mutableListOf<DetailsProduct>()
        return try {
            connection.prepareStatement(sql).use { statement ->
                statement.setString(1, name_product)
                val resultSet = statement.executeQuery()
                while (resultSet.next()){
                    val imageName = resultSet.getString("image_name")
                    val imagePath = "app/src/main/resources/images_tile/$imageName"
                    val imageBase64 = File(imagePath).takeIf { it.exists() }?.readBytes()?.let {
                        Base64.getEncoder().encodeToString(it)
                    }
                    detailsProduct.add(
                        DetailsProduct(
                            id_products = resultSet.getInt("id_products"),
                            article = resultSet.getString("article"),
                            name_products = resultSet.getString("name_products"),
                            size = resultSet.getString("size"),
                            unit = resultSet.getString("unit"),
                            color = resultSet.getString("color"),
                            name_categories = resultSet.getString("name_categories"),
                            description = resultSet.getString("description"),
                            quantity = resultSet.getString("quantity"),
                            name_manufacturer = resultSet.getString("name_manufacturer"),
                            image_name = resultSet.getString("image_name"),
                            status_ = resultSet.getString("status_"),
                            image = imageBase64,
                        )
                    )
                }
            }
            detailsProduct
        }catch (e: SQLException){
            println("ERROR: $e")
            emptyList()
        }
    }
}
