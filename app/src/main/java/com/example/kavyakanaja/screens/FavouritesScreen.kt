package com.example.kavyakanaja.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kavyakanaja.model.SampleData
import com.example.kavyakanaja.navigation.Screen
import com.example.kavyakanaja.ui.theme.*
import com.example.kavyakanaja.utils.FavouritesManager

@Composable
fun FavouritesScreen(navController: NavController) {
    val context = LocalContext.current
    var favouriteIds by remember {
        mutableStateOf(FavouritesManager.getFavouriteIds(context))
    }
    val favouritePoems = SampleData.poems.filter { it.id in favouriteIds }

    Scaffold(
        containerColor = CreamBackground,
        bottomBar = {
            BottomNavBar(
                navController = navController,
                currentRoute = Screen.Favourites.route
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                Text(
                    text = "Favourites",
                    fontFamily = SerifFamily,
                    fontSize = 24.sp,
                    color = DeepBrown,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
                )
            }

            if (favouritePoems.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "♡", fontSize = 52.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No saved poems yet",
                            fontFamily = SerifFamily,
                            fontSize = 16.sp,
                            color = MutedText
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tap ♡ Save on any poem to save it here",
                            fontFamily = SansFamily,
                            fontSize = 12.sp,
                            color = HintText
                        )
                    }
                }
            } else {
                items(favouritePoems) { poem ->
                    PoemRowItem(
                        poem = poem,
                        onClick = {
                            navController.navigate(Screen.PoemDetail.createRoute(poem.id))
                        }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}