package com.example.apiintegration

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
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
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {
    //private lateinit var navController: NavHostController
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
                    composable("loginPhone"){ loginScreenph(navController)}
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
    var storedVerificationId: String? = null
    lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            // This callback will be invoked in two situations:
            // 1 - Instant verification. In some cases the phone number can be instantly
            //     verified without needing to send or enter a verification code.
            // 2 - Auto-retrieval. On some devices Google Play services can automatically
            //     detect the incoming verification SMS and perform verification without
            //     user action.
            Log.d(TAG, "onVerificationCompleted:$credential")
            //signInWithPhoneAuthCredential(credential)
        }

        override fun onVerificationFailed(e: FirebaseException) {
            // This callback is invoked in an invalid request for verification is made,
            // for instance if the the phone number format is not valid.
            Log.w(TAG, "onVerificationFailed", e)

            if (e is FirebaseAuthInvalidCredentialsException) {
                println("Invalid request")
            } else if (e is FirebaseTooManyRequestsException) {
                println("The SMS quota for the project has been exceeded")
            } else if (e is FirebaseAuthMissingActivityForRecaptchaException) {
                println("reCAPTCHA verification attempted with null Activity")
            }

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            // The SMS verification code has been sent to the provided phone number, we
            // now need to ask the user to enter the code and then construct a credential
            // by combining the code with a verification ID.
            Log.d(TAG, "onCodeSent:$verificationId")

            // Save verification ID and resending token so we can use them later
            storedVerificationId = verificationId
            resendToken = token
        }
    }
    fun startPhoneNumberVerification(phoneNumber: String) {
        val options = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber(phoneNumber) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    fun verifyOTP(otp: String,navController: NavHostController) {
        val credential = PhoneAuthProvider.getCredential(storedVerificationId!!, otp)
        signInWithPhoneAuthCredential(credential,navController)
    }
    fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential,navController: NavHostController) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    navController.navigate("products")

                    val user = task.result?.user
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                    }
                    // Update UI
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
                        Spacer(modifier=Modifier.height(10.dp))
                        Button(onClick = { navController.navigate("loginPhone") }) {
                            Text("Login with Phone Number")
                        }
                    }

                }
            }
        }
    }
    @Composable
    fun loginScreenph(navController: NavHostController) {
        var phoneNumber = remember {
            mutableStateOf("")
        }
        var otp = remember {
            mutableStateOf("")
        }
        Scaffold {
                innerPadding ->
            Column(modifier = Modifier
                .background(Color.Black)
                .fillMaxSize()
                .padding(innerPadding)
                .padding(50.dp)

            ) {
                Text("Login with phone", color = Color.White, fontWeight = FontWeight.ExtraBold, modifier = Modifier.align(Alignment.CenterHorizontally))
                Spacer(modifier = Modifier.height(5.dp))
                Card(modifier = Modifier
                    .height(300.dp)
                    .width(700.dp)) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        TextField(
                            value = phoneNumber.value,
                            onValueChange = { phoneNumber.value = it },
                            label = { Text(text = "Phone Number") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            startPhoneNumberVerification(phoneNumber.value)
                        }) {
                            Text(text = "Send OTP")
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        TextField(
                            value = otp.value,
                            onValueChange = { otp.value = it },
                            label = { Text(text = "Enter Otp") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            verifyOTP(otp.value,navController)
                        }) {
                            Text(text = "Validate OTP")
                        }
                    }
                }
            }
        }
    }
}