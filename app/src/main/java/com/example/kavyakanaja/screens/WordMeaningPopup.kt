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

val localWordMeanings = mapOf(
    "ಮಂಜಿನ" to Pair("Misty / Of the Fog", "Refers to mist or fog. Used poetically to describe mountain fog softly veiling the peaks at dawn."),
    "ಜೊನ್ನಲ" to Pair("Moonlight", "Soft silver moonlight filtering through the forest canopy at night."),
    "ಬಾನಿನ" to Pair("Of the Sky", "Belonging to the sky or heavens. A poetic reference to the vast open sky."),
    "ತಾರೆ" to Pair("Star", "A star in the night sky. Often used as a symbol of hope or guidance."),
    "ಕಣಿವೆ" to Pair("Valley", "A valley between hills or mountains. Represents depth and shelter in Kannada poetry."),
    "ಹೊಂಗನಸು" to Pair("Golden Dream", "A beautiful aspirational dream. 'Hong' means gold and 'kanasu' means dream."),
    "ಮಲೆಯ" to Pair("Of the Mountain", "Belonging to the hills or mountains. Commonly used in Western Ghats poetry."),
    "ನದಿಯು" to Pair("The River", "A flowing river. Symbol of life, continuity and passage of time in Kannada poetry."),
    "ಕಾಡಿನ" to Pair("Of the Forest", "Belonging to the forest or jungle. Evokes wildness and natural beauty."),
    "ಬೆಟ್ಟದ" to Pair("Of the Hill", "Belonging to the hills. Often used to describe Karnataka's highland landscape."),
    "ಪ್ರೀತಿ" to Pair("Love", "Deep affection and care. One of the most celebrated themes in Kannada poetry."),
    "ಜೀವನ" to Pair("Life", "The journey of existence. Poets use this word to reflect on life's joys and sorrows."),
    "ಆಕಾಶ" to Pair("Sky", "The vast sky above. Used to symbolize freedom, infinity and divine presence."),
    "ಹಸಿರು" to Pair("Greenery", "The color green and lush vegetation. Represents life, freshness and nature's beauty."),
    "ಮನಸು" to Pair("The Mind / Heart", "The seat of emotions and thoughts. Central to introspective Kannada poetry."),
    "ಸೂರ್ಯ" to Pair("The Sun", "The sun. Symbol of energy, truth and enlightenment in classical poetry."),
    "ಚಂದ್ರ" to Pair("The Moon", "The moon. Associated with beauty, romance and gentle light in Kannada verse."),
    "ಶಾಂತಿ" to Pair("Peace", "Inner and outer peace. A common aspiration expressed in devotional Kannada poetry."),
    "ಭೂಮಿ" to Pair("Earth / Soil", "The earth or ground. Symbolizes the mother, roots and belonging."),
    "ಕನ್ನಡ" to Pair("Kannada", "The Kannada language and culture. A source of deep pride for Karnataka.")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordMeaningPopup(word: String, onDismiss: () -> Unit) {

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val localMeaning = localWordMeanings[word]

    var aiMeaning by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Always fetch AI meaning — for local words it shows extra AI context
    // For unknown words it fetches from scratch
    LaunchedEffect(word) {
        if (localMeaning == null) {
            isLoading = true
            errorMessage = null
            try {
                val result = GeminiApi.getWordMeaning(word)
                aiMeaning = result
            } catch (e: Exception) {
                errorMessage = "Could not load meaning. Check internet."
            }
            isLoading = false
        }
    }

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
                .padding(bottom = 48.dp)
        ) {

            // Word title
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
                letterSpacing = 1.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))
            Box(modifier = Modifier.fillMaxWidth().height(0.5.dp).background(CreamBorder))
            Spacer(modifier = Modifier.height(14.dp))

            if (localMeaning != null) {
                // Local dictionary result
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
                    fontSize = 14.sp,
                    color = MutedBrown,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(CreamSurface)
                        .padding(12.dp)
                ) {
                    Text(
                        text = "✦  From local Kannada dictionary",
                        fontFamily = SansFamily,
                        fontSize = 12.sp,
                        color = WarmBrown
                    )
                }

            } else {
                // AI result
                when {
                    isLoading -> {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.padding(vertical = 16.dp)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = WarmBrown,
                                strokeWidth = 2.dp
                            )
                            Text(
                                text = "Asking Gemini AI...",
                                fontFamily = SansFamily,
                                fontSize = 13.sp,
                                color = MutedText
                            )
                        }
                    }

                    errorMessage != null -> {
                        Text(
                            text = errorMessage!!,
                            fontFamily = SansFamily,
                            fontSize = 13.sp,
                            color = MutedText,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    aiMeaning != null -> {
                        Text(
                            text = aiMeaning!!,
                            fontFamily = SansFamily,
                            fontSize = 14.sp,
                            color = MutedBrown,
                            lineHeight = 22.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(CreamSurface)
                                .padding(12.dp)
                        ) {
                            Text(
                                text = "✦  Powered by Gemini AI",
                                fontFamily = SansFamily,
                                fontSize = 12.sp,
                                color = WarmBrown
                            )
                        }
                    }

                    else -> {
                        Text(
                            text = "Meaning not available.",
                            fontFamily = SansFamily,
                            fontSize = 13.sp,
                            color = MutedText
                        )
                    }
                }
            }
        }
    }
}