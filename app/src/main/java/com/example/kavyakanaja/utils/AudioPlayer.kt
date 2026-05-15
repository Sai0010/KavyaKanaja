package com.example.kavyakanaja.utils

import android.content.Context
import android.media.MediaPlayer
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
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
                val result = tts?.setLanguage(Locale("kn", "IN"))
                if (result == TextToSpeech.LANG_MISSING_DATA ||
                    result == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    tts?.language = Locale.getDefault()
                }
                isTtsReady = true
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) { isPlaying = true }
                    override fun onDone(utteranceId: String?) { isPlaying = false; usingTts = false }
                    override fun onError(utteranceId: String?) { isPlaying = false; usingTts = false }
                })
            }
        }
    }

    fun speakPoem(verses: List<String>) {
        if (!isTtsReady) return
        if (isPlaying && usingTts) {
            tts?.stop()
            isPlaying = false
            usingTts = false
            return
        }
        val text = verses.filter { it.isNotEmpty() }.joinToString(". ")
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "POEM_${System.currentTimeMillis()}")
        isPlaying = true
        usingTts = true
    }

    fun play(rawResId: Int) {
        try {
            usingTts = false
            mediaPlayer?.release()
            mediaPlayer = null
            mediaPlayer = MediaPlayer.create(context, rawResId)
            mediaPlayer?.setOnCompletionListener { isPlaying = false }
            mediaPlayer?.start()
            isPlaying = true
        } catch (e: Exception) {
            isPlaying = false
        }
    }

    fun getCurrentPosition(): Int {
        return try { mediaPlayer?.currentPosition ?: 0 } catch (e: Exception) { 0 }
    }

    fun getTotalDuration(): Int {
        return try { mediaPlayer?.duration ?: 0 } catch (e: Exception) { 0 }
    }

    fun togglePlayPause(rawResId: Int) {
        if (mediaPlayer == null) play(rawResId)
        else if (isPlaying) { mediaPlayer?.pause(); isPlaying = false }
        else { mediaPlayer?.start(); isPlaying = true }
    }

    fun release() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
    }
}