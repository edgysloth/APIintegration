package com.example.apiintegration.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.apiintegration.Model.Data.User
import com.example.apiintegration.ViewModel.UserVieModel

@Composable
fun userScreen(userVM: UserVieModel) {
    val users by userVM.users.observeAsState(emptyList())
    Column(modifier = Modifier.background(Color.Black)){
        Text(text = "UserList",color=Color.White,fontSize = 50.sp, fontWeight = FontWeight.ExtraBold)

        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(users) {
                userItem(user = it)
            }
        }
    }
}

@Composable
fun userItem(user: User) {
    Column(modifier = Modifier
        .background(Color.Black)
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){
        Card(
            modifier = Modifier
                .padding(8.dp)
                .width(360.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.cardColors(containerColor = Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(5.dp),

                verticalArrangement = Arrangement.Center
            ) {
                Text("id: ${user.id}")
                Spacer(modifier = Modifier.height(3.dp))
                Row{
                    Text("EmailId:")
                    Text("${user.email}", color = Color.Blue, textDecoration = TextDecoration.Underline)
                }
                Spacer(modifier = Modifier.height(3.dp))
                Text("Username: ${user.username}")
                Spacer(modifier = Modifier.height(3.dp))
                Text("Password: ${user.password}")
                Spacer(modifier = Modifier.height(3.dp))
                Text("Name: ${user.name.firstname} ${user.name.lastname}")



            }

        }
    }
}