package com.example.apiintegration.Views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.apiintegration.Model.Data.Product
import com.example.apiintegration.R
import com.example.apiintegration.ViewModel.ProductVieModel

@Composable
fun productScreen(productVM: ProductVieModel) {
    val products by productVM.products.observeAsState(emptyList())
    Column(){
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(50.dp) // Adjust height as needed
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFE91E63), // Starting color
                        Color(0xFFFFC107)  // Ending color
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(1000f, 1000f)
                )
            )
        ) {
            BasicText(
                text = "Subham's",
                style = TextStyle(
                    fontFamily =FontFamily(Font(R.font.greatvibes_regular)) ,
                    fontWeight = FontWeight.Bold,
                    fontSize = 40.sp, 
                    color = Color.White,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(5.dp))
        Text(text = "Sale", fontSize = 50.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.padding(5.dp))
        Text(
            "Super Saver Sale",
            style = TextStyle(
                color = Color.Gray.copy(alpha = 0.5f), 
                fontSize = 16.sp
            ), modifier = Modifier.padding(5.dp)
        )
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            items(products) {
                productItem(product = it)
            }
        }
    }
}

@Composable
fun productItem(product: Product) {
    Column {
        Card(
            modifier = Modifier
                .padding(8.dp)
                .width(400.dp)
                .height(600.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(5.dp),

                verticalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = product.image,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(5.dp),
                    contentScale = ContentScale.Crop,
                    alignment = Alignment.Center
                )
                Spacer(modifier = Modifier.height(10.dp))
                RatingBarX(rating = product.rating.rate)
                Text(text = "${product.rating.rate}(${product.rating.count})",modifier=Modifier.padding(5.dp))
                Text(text = product.title, modifier = Modifier.padding(5.dp), fontFamily = FontFamily.Serif, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(5.dp))

            }
            Text(text = "$ ${product.price}",color=Color.Red, modifier = Modifier.padding(5.dp))
        }
    }
}

@Composable
fun RatingBarX(rating: Double) {
    val colored = rating.toInt()
    val halfColored = if (rating - colored >= 0.5) 1 else 0
    val uncolored = 5 - colored - halfColored

    Row {
        repeat(colored) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color.Yellow)
        }
        repeat(halfColored) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color.Yellow)
        }
        repeat(uncolored) {
            Icon(Icons.Default.Star, contentDescription = null, tint = Color.Gray)
        }
    }
}
