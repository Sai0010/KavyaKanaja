package com.example.kavyakanaja.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kavyakanaja.model.SampleData
import com.example.kavyakanaja.navigation.Screen
import com.example.kavyakanaja.ui.theme.*

@Composable
fun PoetProfileScreen(navController: NavController, poetId: Int) {

    val poet = SampleData.poets.find { it.id == poetId } ?: SampleData.poets.first()
    val poetPoems = SampleData.poems.filter { it.poetId == poetId }

    Scaffold(containerColor = CreamBackground) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            // Back button
            item {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .background(CreamSurface)
                        .clickable { navController.popBackStack() }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "← Back",
                        fontFamily = SansFamily,
                        fontSize = 12.sp,
                        color = MutedBrown
                    )
                }
            }

            // Avatar + Name
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(50))
                            .background(CreamSurface),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = poet.emoji, fontSize = 36.sp)
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = poet.nameKannada,
                        fontFamily = SerifFamily,
                        fontSize = 26.sp,
                        color = DeepBrown
                    )
                    Text(
                        text = poet.nameEnglish,
                        fontFamily = SansFamily,
                        fontSize = 13.sp,
                        color = MutedText,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = poet.years,
                        fontFamily = SansFamily,
                        fontSize = 12.sp,
                        color = HintText,
                        modifier = Modifier.padding(top = 2.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (poet.award.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(CreamCard)
                                .padding(horizontal = 16.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "★  ${poet.award}",
                                fontFamily = SansFamily,
                                fontSize = 11.sp,
                                color = WarmBrown,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Bio
            item {
                Column(
                    modifier = Modifier.padding(
                        horizontal = 24.dp,
                        vertical = 16.dp
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(0.5.dp)
                            .background(CreamBorder)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = poet.bio,
                        fontFamily = SansFamily,
                        fontSize = 13.sp,
                        color = MutedBrown,
                        lineHeight = 22.sp
                    )
                }
            }

            // Poems by this poet label
            item {
                Text(
                    text = "POEMS IN COLLECTION",
                    fontFamily = SansFamily,
                    fontSize = 10.sp,
                    color = MutedText,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(
                        horizontal = 24.dp,
                        vertical = 8.dp
                    )
                )
            }

            if (poetPoems.isEmpty()) {
                item {
                    Text(
                        text = "No poems added yet.",
                        fontFamily = SansFamily,
                        fontSize = 13.sp,
                        color = HintText,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                }
            } else {
                items(poetPoems) { poem ->
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(
                                        Screen.PoemDetail.createRoute(poem.id)
                                    )
                                }
                                .padding(horizontal = 24.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = poem.titleKannada,
                                    fontFamily = SerifFamily,
                                    fontSize = 15.sp,
                                    color = DeepBrown
                                )
                                Text(
                                    text = "${poem.titleEnglish}  ·  ${poem.year}",
                                    fontFamily = SansFamily,
                                    fontSize = 11.sp,
                                    color = MutedText,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                            Text(
                                text = "›",
                                fontSize = 20.sp,
                                color = MutedText
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                                .height(0.5.dp)
                                .background(CreamBorder)
                        )
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}