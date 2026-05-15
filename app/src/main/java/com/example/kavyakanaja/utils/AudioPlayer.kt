package com.example.kavyakanaja.utils

import android.content.Context
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.widget.Toast
import java.util.Locale

class AudioPlayerManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var tts: TextToSpeech? = null

    private var isTtsReady = false
    private var usingTts = false

    var isPlaying: Boolean = false

    init {

        tts = TextToSpeech(context) { status ->

            if (status == TextToSpeech.SUCCESS) {

                Log.d("TTS", "TTS initialized")

                val result = tts?.setLanguage(Locale("kn", "IN"))

                if (
                    result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {

                    Log.e("TTS", "Kannada not supported")

                    Toast.makeText(
                        context,
                        "Kannada voice not installed on this device",
                        Toast.LENGTH_LONG
                    ).show()

                    tts?.language = Locale.getDefault()
                }

                isTtsReady = true

                tts?.setOnUtteranceProgressListener(
                    object : UtteranceProgressListener() {

                        override fun onStart(utteranceId: String?) {

                            isPlaying = true

                            Log.d("TTS", "Speech started")
                        }

                        override fun onDone(utteranceId: String?) {

                            isPlaying = false
                            usingTts = false

                            Log.d("TTS", "Speech completed")
                        }

                        override fun onError(utteranceId: String?) {

                            isPlaying = false
                            usingTts = false

                            Log.e("TTS", "Speech error")
                        }
                    }
                )

            } else {

                Log.e("TTS", "TTS initialization failed")

                Toast.makeText(
                    context,
                    "TTS initialization failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    fun speakPoem(verses: List<String>) {

        if (!isTtsReady) {

            Toast.makeText(
                context,
                "TTS not ready yet",
                Toast.LENGTH_SHORT
            ).show()

            Log.e("TTS", "Speak called before initialization")

            return
        }

        if (isPlaying && usingTts) {

            Log.d("TTS", "Stopping current speech")

            tts?.stop()

            isPlaying = false
            usingTts = false

            return
        }

        try {

            val text = verses
                .filter { it.isNotBlank() }
                .joinToString(". ")

            if (text.isBlank()) {

                Toast.makeText(
                    context,
                    "Poem text is empty",
                    Toast.LENGTH_SHORT
                ).show()

                return
            }

            val utteranceId = "POEM_${System.currentTimeMillis()}"

            val result = tts?.speak(
                text,
                TextToSpeech.QUEUE_FLUSH,
                null,
                utteranceId
            )

            if (result == TextToSpeech.ERROR) {

                Log.e("TTS", "Speak failed")

                Toast.makeText(
                    context,
                    "Failed to play audio",
                    Toast.LENGTH_SHORT
                ).show()

                return
            }

            usingTts = true
            isPlaying = true

            Log.d("TTS", "Speech request sent")

        } catch (e: Exception) {

            isPlaying = false
            usingTts = false

            Log.e("TTS", "Speech crashed", e)

            Toast.makeText(
                context,
                "Audio crashed: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun play(rawResId: Int) {

        try {

            usingTts = false

            mediaPlayer?.release()
            mediaPlayer = null

            mediaPlayer = MediaPlayer.create(context, rawResId)

            mediaPlayer?.setOnCompletionListener {

                isPlaying = false

                Log.d("MediaPlayer", "Audio completed")
            }

            mediaPlayer?.start()

            isPlaying = true

            Log.d("MediaPlayer", "Audio playback started")

        } catch (e: Exception) {

            isPlaying = false

            Log.e("MediaPlayer", "Playback failed", e)
        }
    }

    fun getCurrentPosition(): Int {

        return try {

            mediaPlayer?.currentPosition ?: 0

        } catch (e: Exception) {

            Log.e("MediaPlayer", "Position fetch failed", e)

            0
        }
    }

    fun getTotalDuration(): Int {

        return try {

            mediaPlayer?.duration ?: 0

        } catch (e: Exception) {

            Log.e("MediaPlayer", "Duration fetch failed", e)

            0
        }
    }

    fun togglePlayPause(rawResId: Int) {

        try {

            if (mediaPlayer == null) {

                play(rawResId)

            } else if (isPlaying) {

                mediaPlayer?.pause()

                isPlaying = false

                Log.d("MediaPlayer", "Paused")

            } else {

                mediaPlayer?.start()

                isPlaying = true

                Log.d("MediaPlayer", "Resumed")
            }

        } catch (e: Exception) {

            Log.e("MediaPlayer", "Toggle failed", e)
        }
    }

    fun release() {

        try {

            tts?.stop()
            tts?.shutdown()
            tts = null

            mediaPlayer?.release()
            mediaPlayer = null

            isPlaying = false

            Log.d("AudioPlayer", "Resources released")

        } catch (e: Exception) {

            Log.e("AudioPlayer", "Release failed", e)
        }
    }
}