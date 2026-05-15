# ಕಾವ್ಯ ಕಣಜ — Kavya-Kanaja

A Kannada Literary Revival Android App built using Jetpack Compose and GenAI.

## Problem Statement
Karnataka has a rich literary history, but the younger generation (Gen-Z) finds it
difficult to access classical Kannada poems or understand Old Kannada meanings.
Literary pride is fading — Kavya-Kanaja is built to reverse that trend.

## Features
- 📜 **Poem of the Day** — Auto-changes based on current date
- 🎧 **Listen & Learn** — Audio recitations using Android MediaPlayer
- 💡 **Bhavartha** — Tap any word to get its meaning (powered by Gemini AI)
- 👤 **Poet's Corner** — Biographies of Jnanpith awardees
- ♡ **Favourites** — Save poems locally using SharedPreferences
- 🔍 **Library** — Browse and search all 50+ poems by category

## Tech Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose
- **Architecture:** MVVM
- **Audio:** Android MediaPlayer
- **Data:** Local JSON asset file (50+ poems)
- **GenAI:** Gemini API (word meanings and poem summaries)
- **Storage:** SharedPreferences (favourites)
- **Navigation:** Jetpack Navigation Compose

## Project Structure

app/
├── src/main/
│   ├── assets/
│   │   └── poems.json          # 50 Kannada poems dataset
│   ├── java/com/example/kavyakanaja/
│   │   ├── model/              # Data classes (Poem, Poet, SampleData)
│   │   ├── navigation/         # NavGraph and Screen routes
│   │   ├── screens/            # All UI screens
│   │   │   ├── HomeScreen.kt
│   │   │   ├── LibraryScreen.kt
│   │   │   ├── PoemDetailScreen.kt
│   │   │   ├── WordMeaningPopup.kt
│   │   │   ├── PoetsListScreen.kt
│   │   │   ├── PoetProfileScreen.kt
│   │   │   ├── FavouritesScreen.kt
│   │   │   └── SplashScreen.kt
│   │   ├── ui/theme/           # Colors, Typography, Theme
│   │   └── utils/              # AudioPlayerManager, GeminiApi, FavouritesManager
│   └── res/
│       └── raw/                # Audio files for recitations

## Setup Instructions

### Prerequisites
- Android Studio Hedgehog or later
- Android SDK API 24+
- Internet connection (for Gemini AI features)

### Installation
1. Clone the repository
```bash
   git clone https://github.com/YOUR_USERNAME/KavyaKanaja.git
```
2. Open in Android Studio
3. Add your Gemini API key in `utils/GeminiApi.kt`
```kotlin
   private const val API_KEY = "YOUR_GEMINI_API_KEY_HERE"
```
4. Click **Run** or press `Shift+F10`

### Get Gemini API Key (Free)
- Visit https://aistudio.google.com/app/apikey
- Create a free API key
- Paste it in `GeminiApi.kt`

## Screenshots
<!-- Add your screenshots here -->
| Splash | Home | Poem Detail |
|--------|------|-------------|
| ![Splash](screenshots/splash.png) | ![Home](screenshots/home.png) | ![Detail](screenshots/detail.png) |

## GenAI Integration
- **Word Meanings:** Tap any word in a poem → Gemini API returns a simple explanation
- **Poem Summary:** Expand Bhavartha section → AI generates a modern language summary

## Internship Details
- **Company:** MindMatrix.io, Bengaluru
- **Domain:** Android Development using GenAI
- **Project:** #75 — National Pride (Kavya-Kanaja)
- **Duration:** 15 Weeks

## Future Enhancements
- Add more poets and 200+ poems
- Offline AI using on-device models
- User-generated poem submissions
- Share poem as image card