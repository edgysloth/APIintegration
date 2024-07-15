package com.example.apiintegration

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.apiintegration.ViewModel.ProductVieModel
import com.example.apiintegration.ViewModel.UserVieModel
import com.example.apiintegration.Views.productScreen
import com.example.apiintegration.ui.theme.APIintegrationTheme
import com.google.firebase.auth.FirebaseAuth


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            val productVM: ProductVieModel by viewModels()
            val userVM: UserVieModel by viewModels()
            APIintegrationTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {
                    composable("login") { loginScreen(navController) }
                    composable("products") { productScreen(productVM) }
                }
            }
        }
    }
    val auth=FirebaseAuth.getInstance()
    fun signIn(email:String,password:String,navController: NavHostController){
        auth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener { task ->
                if(task.user!=null){
                    println("User logged in")
                    var user=auth.currentUser
                    println(user?.uid)
                    navController.navigate("products")
                }
            }
    }
    fun signUp(email:String , password: String,navController: NavHostController){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnSuccessListener { task->
                if(task.user!=null){
                    println("User created")
                    var user=auth.currentUser
                    println(user?.uid)
                    navController.navigate("login")
                }
            }
    }
    @Composable
    fun loginScreen(navController: NavHostController){
        var email= remember {
            mutableStateOf("")
        }
        var password= remember {
            mutableStateOf("")
        }
        Scaffold{
            innerPadding->
            Column(modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
                .padding(innerPadding)
                .padding(50.dp))
            {
                Text(
                    text = "Login/Register",
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(16.dp),
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(20.dp))
                Card(modifier = Modifier
                    .height(300.dp)
                    .width(700.dp)
                    .align(Alignment.CenterHorizontally)){
                    Column(modifier=Modifier.padding(10.dp)){
                        TextField(value = email.value,
                            onValueChange = { email.value = it },
                            label={ Text(text="Email")},
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(CircleShape)
                        )
                        Spacer(modifier=Modifier.height(10.dp))
                        TextField(value = password.value,
                            onValueChange = { password.value = it },
                            label={ Text(text="Password")},
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(CircleShape),
                            visualTransformation = PasswordVisualTransformation()
                        )
                        Spacer(modifier=Modifier.height(16.dp))
                        Row {
                            Button(onClick = { signUp(email.value,password.value,navController) }) {
                                Text("Register")
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Button(onClick = { signIn(email.value,password.value,navController) }) {
                                Text(text = "Login")
                            }
                        }
                    }

                }
            }
        }
    }
}




