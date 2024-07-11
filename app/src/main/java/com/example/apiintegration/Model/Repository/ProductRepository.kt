package com.example.apiintegration.Model.Repository

import com.example.apiintegration.Model.API.ApiService
import com.example.apiintegration.Model.Data.Product

class ProductRepository(private val apiService: ApiService){
    suspend fun getProducts(): List<Product>{
        return apiService.getProducts()
    }
}