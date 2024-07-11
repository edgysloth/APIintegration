package com.example.apiintegration

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.apiintegration.ViewModel.ProductVieModel
import com.example.apiintegration.ViewModel.UserVieModel
import com.example.apiintegration.Views.userScreen
import com.example.apiintegration.ui.theme.APIintegrationTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            val productVM: ProductVieModel by viewModels()
            val userVM: UserVieModel by viewModels()
            APIintegrationTheme {
               //productScreen(productVM = productVM)
                userScreen(userVM = userVM)
            }
        }
    }
}




