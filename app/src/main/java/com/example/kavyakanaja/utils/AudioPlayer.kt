package com.example.kavyakanaja.utils

import android.content.Context
import android.media.MediaPlayer

class AudioPlayerManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    var isPlaying: Boolean = false
    var progress: Float = 0f

    fun play(rawResId: Int) {
        try {
            mediaPlayer?.release()
            mediaPlayer = null
            mediaPlayer = MediaPlayer.create(context, rawResId)
            mediaPlayer?.setOnCompletionListener {
                isPlaying = false
                progress = 0f
            }
            mediaPlayer?.start()
            isPlaying = true
        } catch (e: Exception) {
            isPlaying = false
        }
    }

    fun pause() {
        mediaPlayer?.pause()
        isPlaying = false
    }

    fun resume() {
        mediaPlayer?.start()
        isPlaying = true
    }

    fun togglePlayPause(rawResId: Int) {
        if (mediaPlayer == null) {
            play(rawResId)
        } else if (isPlaying) {
            pause()
        } else {
            resume()
        }
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
        isPlaying = false
    }

    fun getDuration(): String {
        val ms = mediaPlayer?.duration ?: 0
        val secs = (ms / 1000) % 60
        val mins = ms / 1000 / 60
        return "$mins:${secs.toString().padStart(2, '0')}"
    }
}