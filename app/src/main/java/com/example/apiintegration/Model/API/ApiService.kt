package com.example.apiintegration.Model.API

import com.example.apiintegration.Model.Data.Product
import com.example.apiintegration.Model.Data.User
import retrofit2.http.GET

interface ApiService{
    @GET("Products")
    suspend fun getProducts(): List<Product>
    @GET("Users")
    suspend fun getUsers():List<User>
}