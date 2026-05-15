package com.example.kavyakanaja.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.kavyakanaja.model.Poem
import com.example.kavyakanaja.model.SampleData
import com.example.kavyakanaja.utils.AudioPlayerManager
import com.example.kavyakanaja.utils.FavouritesManager
import com.example.kavyakanaja.utils.GeminiApi
import com.example.kavyakanaja.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun PoemDetailScreen(navController: NavController, poemId: Int) {

    val poem = SampleData.poems.find { it.id == poemId } ?: SampleData.poems.first()
    val context = LocalContext.current
    val audioManager = remember { AudioPlayerManager(context) }

    var showMeaning by remember { mutableStateOf(false) }
    var selectedWord by remember { mutableStateOf<String?>(null) }
    var isFavourite by remember {
        mutableStateOf(FavouritesManager.isFavourite(context, poemId))
    }
    var currentPosition by remember { mutableIntStateOf(0) }
    var totalDuration by remember { mutableIntStateOf(0) }

    // Timer — updates every second while playing
    LaunchedEffect(audioManager.isPlaying) {
        while (audioManager.isPlaying) {
            currentPosition = audioManager.getCurrentPosition()
            totalDuration = audioManager.getTotalDuration()
            delay(1000)
        }
    }

    DisposableEffect(Unit) {
        onDispose { audioManager.release() }
    }

    if (selectedWord != null) {
        WordMeaningPopup(
            word = selectedWord!!,
            onDismiss = { selectedWord = null }
        )
    }

    Scaffold(containerColor = CreamBackground) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(CreamSurface)
                        .clickable { navController.popBackStack() }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text("← Back", fontFamily = SansFamily, fontSize = 12.sp, color = MutedBrown)
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(if (isFavourite) CreamCard else CreamSurface)
                        .clickable {
                            FavouritesManager.toggleFavourite(context, poem.id)
                            isFavourite = !isFavourite
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = if (isFavourite) "♥ Saved" else "♡ Save",
                        fontFamily = SansFamily,
                        fontSize = 12.sp,
                        color = if (isFavourite) WarmBrown else MutedText
                    )
                }
            }

            // Poem header
            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(
                    poem.titleKannada,
                    fontFamily = SerifFamily,
                    fontSize = 28.sp,
                    color = DeepBrown,
                    lineHeight = 36.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "${poem.authorKannada}  ·  ${poem.year}",
                    fontFamily = SansFamily,
                    fontSize = 12.sp,
                    color = MutedText
                )
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(CreamBorder)
                )
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Poem verses — each word tappable
            Column(modifier = Modifier.padding(horizontal = 28.dp)) {
                poem.verses.forEach { line ->
                    if (line.isEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            val words = line.split(" ")
                            words.forEachIndexed { index, word ->
                                Text(
                                    text = if (index < words.size - 1) "$word " else word,
                                    fontFamily = SerifFamily,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 17.sp,
                                    color = MutedBrown,
                                    lineHeight = 28.sp,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(3.dp))
                                        .clickable { selectedWord = word }
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Tap any word for its meaning",
                fontFamily = SansFamily,
                fontSize = 10.sp,
                color = HintText,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 20.dp)
            )

            // Audio player
            AudioPlayerBar(
                audioManager = audioManager,
                currentPosition = currentPosition,
                totalDuration = totalDuration,
                verses = poem.verses
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bhavartha section
            BhavarthaSection(
                isExpanded = showMeaning,
                poem = poem,
                onToggle = { showMeaning = !showMeaning }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────

fun formatTime(ms: Int): String {
    if (ms <= 0) return "0:00"
    val secs = (ms / 1000) % 60
    val mins = ms / 1000 / 60
    return "$mins:${secs.toString().padStart(2, '0')}"
}

// ── Audio Player Bar ──────────────────────────────────────────

@Composable
fun AudioPlayerBar(
    audioManager: AudioPlayerManager,
    currentPosition: Int,
    totalDuration: Int,
    verses: List<String> = emptyList()
) {
    val progress = if (totalDuration > 0) currentPosition.toFloat() / totalDuration else 0f

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(CreamSurface)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(50))
                .background(WarmBrown)
                .clickable { audioManager.speakPoem(verses) },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (audioManager.isPlaying) "⏸" else "▶",
                fontSize = 14.sp,
                color = CreamBackground
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (audioManager.isPlaying) "Reading poem aloud..." else "Listen to poem",
                fontFamily = SansFamily,
                fontSize = 11.sp,
                color = MutedBrown
            )
            Spacer(modifier = Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(CreamBorder)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(progress.coerceIn(0f, 1f))
                        .fillMaxHeight()
                        .background(WarmBrown)
                )
            }
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = formatTime(currentPosition),
                fontFamily = SansFamily,
                fontSize = 10.sp,
                color = MutedBrown
            )
            Text(
                text = formatTime(totalDuration),
                fontFamily = SansFamily,
                fontSize = 9.sp,
                color = HintText
            )
        }
    }
}

// ── Bhavartha Section ─────────────────────────────────────────

@Composable
fun BhavarthaSection(
    isExpanded: Boolean,
    poem: Poem,
    onToggle: () -> Unit
) {
    var aiSummary by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var hasFetched by remember { mutableStateOf(false) }

    LaunchedEffect(isExpanded) {
        if (isExpanded && !hasFetched) {
            hasFetched = true
            isLoading = true
            errorMsg = null
            try {
                aiSummary = GeminiApi.getPoemSummary(poem.titleEnglish, poem.verses)
            } catch (e: Exception) {
                errorMsg = "Could not load. Check internet."
            }
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(CreamCard)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onToggle() }
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "📖  Bhavartha (Meaning)",
                fontFamily = SansFamily,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = WarmBrown
            )
            Text(
                text = if (isExpanded) "∧" else "∨",
                fontSize = 14.sp,
                color = MutedText
            )
        }

        AnimatedVisibility(visible = isExpanded) {
            Column(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(0.5.dp)
                        .background(CreamBorder)
                )
                Spacer(modifier = Modifier.height(12.dp))

                when {
                    isLoading -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = WarmBrown,
                                strokeWidth = 2.dp
                            )
                            Text(
                                text = "Generating AI explanation...",
                                fontFamily = SansFamily,
                                fontSize = 13.sp,
                                color = MutedText
                            )
                        }
                    }
                    errorMsg != null -> {
                        Text(
                            text = errorMsg!!,
                            fontFamily = SansFamily,
                            fontSize = 13.sp,
                            color = MutedText
                        )
                    }
                    aiSummary != null -> {
                        Text(
                            text = aiSummary!!,
                            fontFamily = SansFamily,
                            fontSize = 13.sp,
                            color = MutedBrown,
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(CreamSurface)
                                .padding(10.dp)
                        ) {
                            Text(
                                text = "✦  Powered by Gemini AI",
                                fontFamily = SansFamily,
                                fontSize = 11.sp,
                                color = WarmBrown
                            )
                        }
                    }
                }
            }
        }
    }
}