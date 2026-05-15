package com.example.kavyakanaja.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.kavyakanaja.ui.theme.*
import com.example.kavyakanaja.utils.GeminiApi
import kotlinx.coroutines.launch

val localWordMeanings = mapOf(
    "ಮಂಜಿನ" to Pair("Misty / Of the Fog", "Refers to mist or fog. Used poetically to describe mountain fog softly veiling the peaks at dawn."),
    "ಜೊನ್ನಲ" to Pair("Moonlight", "Soft, silver moonlight filtering through the forest canopy at night."),
    "ಬಾನಿನ" to Pair("Of the Sky", "Belonging to the sky or heavens. Poetic reference to the vast open sky."),
    "ತಾರೆ" to Pair("Star", "A star in the night sky. Often used symbolically for hope or guidance."),
    "ಕಣಿವೆ" to Pair("Valley", "A valley between hills or mountains. Represents depth and shelter in Kannada poetry."),
    "ಹೊಂಗನಸು" to Pair("Golden Dream", "A beautiful, aspirational dream. 'Hong' means gold, 'kanasu' means dream.")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordMeaningPopup(word: String, onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val localMeaning = localWordMeanings[word]
    var aiMeaning by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = CreamBackground,
        tonalElevation = 0.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
                .padding(bottom = 40.dp)
        ) {
            Text(
                text = word,
                fontFamily = SerifFamily,
                fontSize = 28.sp,
                color = DeepBrown
            )
            Text(
                text = "KANNADA WORD",
                fontFamily = SansFamily,
                fontSize = 10.sp,
                color = HintText,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(CreamBorder))
            Spacer(modifier = Modifier.height(16.dp))

            if (localMeaning != null) {
                Text(
                    text = localMeaning.first,
                    fontFamily = SerifFamily,
                    fontSize = 18.sp,
                    color = WarmBrown,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = localMeaning.second,
                    fontFamily = SansFamily,
                    fontSize = 13.sp,
                    color = MutedBrown,
                    lineHeight = 22.sp
                )
            } else {
                // Show AI meaning
                when {
                    isLoading -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                color = WarmBrown,
                                strokeWidth = 2.dp
                            )
                            Text(
                                text = "Asking AI...",
                                fontFamily = SansFamily,
                                fontSize = 13.sp,
                                color = MutedText
                            )
                        }
                    }
                    aiMeaning != null -> {
                        Text(
                            text = aiMeaning!!,
                            fontFamily = SansFamily,
                            fontSize = 13.sp,
                            color = MutedBrown,
                            lineHeight = 22.sp
                        )
                    }
                    else -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(CreamSurface)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "✦  Not in local dictionary",
                                fontFamily = SansFamily,
                                fontSize = 12.sp,
                                color = WarmBrown
                            )
                        }
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(WarmBrown)
                                .padding(12.dp)
                                .then(Modifier.clip(RoundedCornerShape(10.dp)))
                            ,
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Ask AI for meaning →",
                                fontFamily = SansFamily,
                                fontSize = 13.sp,
                                color = CreamBackground,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                        // Auto fetch
                        LaunchedEffect(word) {
                            isLoading = true
                            aiMeaning = GeminiApi.getWordMeaning(word)
                            isLoading = false
                        }
                    }
                }
            }
        }
    }
}