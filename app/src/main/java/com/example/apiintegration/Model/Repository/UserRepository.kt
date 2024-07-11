package com.example.apiintegration.Model.Repository

import com.example.apiintegration.Model.API.ApiService
import com.example.apiintegration.Model.Data.User

class UserRepository(private val apiService: ApiService){
    suspend fun getUsers(): List<User>{
        return apiService.getUsers()
    }
}