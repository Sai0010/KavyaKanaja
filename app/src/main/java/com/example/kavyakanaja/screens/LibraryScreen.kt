package com.example.kavyakanaja.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kavyakanaja.model.Poem
import com.example.kavyakanaja.model.SampleData
import com.example.kavyakanaja.navigation.Screen
import com.example.kavyakanaja.ui.theme.*

@Composable
fun LibraryScreen(navController: NavController) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf("All", "Classical", "Patriotic", "Nature", "Philosophical", "Devotional")

    val filtered = SampleData.poems.filter { poem ->
        val matchesSearch = searchQuery.isEmpty() ||
                poem.titleKannada.contains(searchQuery, ignoreCase = true) ||
                poem.titleEnglish.contains(searchQuery, ignoreCase = true) ||
                poem.authorEnglish.contains(searchQuery, ignoreCase = true)
        val matchesCategory = selectedCategory == "All" || poem.category == selectedCategory
        matchesSearch && matchesCategory
    }

    Scaffold(
        containerColor = CreamBackground,
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = Screen.Library.route)
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Library",
                        fontFamily = SerifFamily,
                        fontSize = 24.sp,
                        color = DeepBrown
                    )
                    Text(
                        text = "${filtered.size} poems",
                        fontFamily = SansFamily,
                        fontSize = 12.sp,
                        color = MutedText
                    )
                }
            }

            // Search bar
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(CreamSurface)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    if (searchQuery.isEmpty()) {
                        Text(
                            text = "🔍  Search poems or poets...",
                            fontFamily = SansFamily,
                            fontSize = 13.sp,
                            color = MutedText
                        )
                    }
                    BasicTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        textStyle = TextStyle(
                            fontFamily = SansFamily,
                            fontSize = 13.sp,
                            color = DeepBrown
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            // Category filter chips
            item {
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        val isSelected = category == selectedCategory
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (isSelected) WarmBrown else CreamSurface)
                                .clickable { selectedCategory = category }
                                .padding(horizontal = 16.dp, vertical = 7.dp)
                        ) {
                            Text(
                                text = category,
                                fontFamily = SansFamily,
                                fontSize = 11.sp,
                                color = if (isSelected) CreamBackground else MutedText
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Poem rows
            items(filtered) { poem ->
                LibraryPoemRow(poem = poem, onClick = {
                    navController.navigate(Screen.PoemDetail.createRoute(poem.id))
                })
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun LibraryPoemRow(poem: Poem, onClick: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {

                // Left color accent bar
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height(36.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(WarmBrown)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = poem.titleKannada,
                        fontFamily = SerifFamily,
                        fontSize = 15.sp,
                        color = DeepBrown,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(3.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = poem.authorKannada,
                            fontFamily = SansFamily,
                            fontSize = 11.sp,
                            color = MutedText
                        )
                        Text(text = "·", fontSize = 11.sp, color = HintText)
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(CreamSurface)
                                .padding(horizontal = 6.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = poem.category,
                                fontFamily = SansFamily,
                                fontSize = 9.sp,
                                color = MutedBrown
                            )
                        }
                    }
                }
            }
            Text(text = "›", fontSize = 20.sp, color = MutedText)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .height(0.5.dp)
                .background(CreamBorder)
        )
    }
}