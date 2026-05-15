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
import com.example.kavyakanaja.model.Poet
import com.example.kavyakanaja.model.SampleData
import com.example.kavyakanaja.navigation.Screen
import com.example.kavyakanaja.ui.theme.*

@Composable
fun PoetsListScreen(navController: NavController) {
    Scaffold(
        containerColor = CreamBackground,
        bottomBar = {
            BottomNavBar(navController = navController, currentRoute = Screen.PoetsList.route)
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            item {
                Text(
                    text = "Poet's Corner",
                    fontFamily = SerifFamily,
                    fontSize = 24.sp,
                    color = DeepBrown,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)
                )
            }

            item {
                Text(
                    text = "Jnanpith Awardees & celebrated voices of Karnataka",
                    fontFamily = SansFamily,
                    fontSize = 12.sp,
                    color = MutedText,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp)
                )
            }

            items(SampleData.poets) { poet ->
                PoetCard(poet = poet, onClick = {
                    navController.navigate(Screen.PoetProfile.createRoute(poet.id))
                })
                Spacer(modifier = Modifier.height(12.dp))
            }

            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun PoetCard(poet: Poet, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(CreamCard)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Avatar circle
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(50))
                .background(CreamSurface),
            contentAlignment = Alignment.Center
        ) {
            Text(text = poet.emoji, fontSize = 26.sp)
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = poet.nameKannada,
                fontFamily = SerifFamily,
                fontSize = 17.sp,
                color = DeepBrown
            )
            Text(
                text = poet.nameEnglish,
                fontFamily = SansFamily,
                fontSize = 11.sp,
                color = MutedText,
                modifier = Modifier.padding(top = 2.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            if (poet.award.isNotEmpty()) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(CreamMuted)
                        .padding(horizontal = 8.dp, vertical = 3.dp)
                ) {
                    Text(
                        text = "★  ${poet.award}",
                        fontFamily = SansFamily,
                        fontSize = 9.sp,
                        color = WarmBrown
                    )
                }
            }
        }

        Text(text = "›", fontSize = 22.sp, color = MutedText)
    }
}