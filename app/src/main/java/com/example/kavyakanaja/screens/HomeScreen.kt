package com.example.kavyakanaja.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kavyakanaja.model.Poem
import com.example.kavyakanaja.model.SampleData
import com.example.kavyakanaja.navigation.Screen
import com.example.kavyakanaja.ui.theme.*

@Composable
fun HomeScreen(navController: NavController) {

    val poemOfTheDay = SampleData.getPoemOfTheDay()
    val allPoems = SampleData.poems

    Scaffold(
        containerColor = CreamBackground,
        bottomBar = {
            BottomNavBar(
                navController = navController,
                currentRoute = Screen.Home.route
            )
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {

            // Top bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Kavya-Kanaja",
                        fontFamily = SerifFamily,
                        fontSize = 22.sp,
                        color = DeepBrown
                    )
                    Text(
                        text = "☰",
                        fontSize = 20.sp,
                        color = MutedText,
                        modifier = Modifier.clickable {
                            navController.navigate(Screen.Library.route)
                        }
                    )
                }
            }

            // Poem of the Day card
            item {
                PoemOfTheDayCard(
                    poem = poemOfTheDay,
                    onClick = {
                        navController.navigate(Screen.PoemDetail.createRoute(poemOfTheDay.id))
                    }
                )
            }

            // Section label
            item {
                Text(
                    text = "ALL POEMS",
                    fontFamily = SansFamily,
                    fontSize = 10.sp,
                    color = MutedText,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 14.dp)
                )
            }

            // Poem list
            items(allPoems) { poem ->
                PoemRowItem(
                    poem = poem,
                    onClick = {
                        navController.navigate(Screen.PoemDetail.createRoute(poem.id))
                    }
                )
            }
        }
    }
}

// ── Poem of the Day Card ──────────────────────────────────────

@Composable
fun PoemOfTheDayCard(poem: Poem, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(CreamCard)
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(20.dp)) {

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(CreamMuted)
                    .padding(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "✦  POEM OF THE DAY",
                    fontFamily = SansFamily,
                    fontSize = 9.sp,
                    color = MutedBrown,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = poem.titleKannada,
                fontFamily = SerifFamily,
                fontSize = 22.sp,
                color = DeepBrown
            )
            Text(
                text = "${poem.authorKannada}  ·  ${poem.authorEnglish}",
                fontFamily = SansFamily,
                fontSize = 11.sp,
                color = MutedText,
                modifier = Modifier.padding(top = 4.dp, bottom = 14.dp)
            )

            poem.verses.take(2).forEach { line ->
                if (line.isNotEmpty()) {
                    Text(
                        text = line,
                        fontFamily = SerifFamily,
                        fontStyle = FontStyle.Italic,
                        fontSize = 13.sp,
                        color = MutedBrown,
                        lineHeight = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(WarmBrown)
                    .padding(horizontal = 20.dp, vertical = 9.dp)
            ) {
                Text(
                    text = "Read More  →",
                    fontFamily = SansFamily,
                    fontSize = 11.sp,
                    color = CreamBackground
                )
            }
        }
    }
}

// ── Poem Row Item ─────────────────────────────────────────────

@Composable
fun PoemRowItem(poem: Poem, onClick: () -> Unit) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = poem.titleKannada,
                    fontFamily = SerifFamily,
                    fontSize = 15.sp,
                    color = DeepBrown,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${poem.authorKannada}  ·  ${poem.category}",
                    fontFamily = SansFamily,
                    fontSize = 11.sp,
                    color = MutedText,
                    modifier = Modifier.padding(top = 3.dp)
                )
            }
            Text(text = "›", fontSize = 20.sp, color = HintText)
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

// ── Bottom Navigation Bar ─────────────────────────────────────

@Composable
fun BottomNavBar(navController: NavController, currentRoute: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(CreamBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(0.5.dp)
                .background(CreamBorder)
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            BottomNavItem(
                icon = "⌂",
                label = "Home",
                isActive = currentRoute == Screen.Home.route,
                onClick = {
                    if (currentRoute != Screen.Home.route)
                        navController.navigate(Screen.Home.route)
                }
            )
            BottomNavItem(
                icon = "☰",
                label = "Library",
                isActive = currentRoute == Screen.Library.route,
                onClick = {
                    if (currentRoute != Screen.Library.route)
                        navController.navigate(Screen.Library.route)
                }
            )
            BottomNavItem(
                icon = "◯",
                label = "Poets",
                isActive = currentRoute == Screen.PoetsList.route,
                onClick = {
                    if (currentRoute != Screen.PoetsList.route)
                        navController.navigate(Screen.PoetsList.route)
                }
            )
            BottomNavItem(
                icon = "♡",
                label = "Saved",
                isActive = currentRoute == Screen.Favourites.route,
                onClick = {
                    if (currentRoute != Screen.Favourites.route)
                        navController.navigate(Screen.Favourites.route)
                }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: String,
    label: String,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(if (isActive) CreamSurface else CreamBackground)
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = icon,
                fontSize = 16.sp,
                color = if (isActive) WarmBrown else HintText
            )
        }
        Text(
            text = label,
            fontFamily = SansFamily,
            fontSize = 9.sp,
            color = if (isActive) WarmBrown else HintText,
            modifier = Modifier.padding(top = 3.dp)
        )
    }
}