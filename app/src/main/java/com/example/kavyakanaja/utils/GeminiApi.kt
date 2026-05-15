package com.example.kavyakanaja.utils

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject

object GeminiApi {

    // Get your free API key from https://aistudio.google.com/app/apikey
    private const val API_KEY = "AIzaSyA2a-XEoYTsdbEdbUKiST6KO9Gkx65v7ww"
    private const val URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=$API_KEY"

    private val client = OkHttpClient()

    suspend fun getWordMeaning(word: String): String = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                The word "$word" is from a classical Kannada poem.
                Give a simple, short explanation in 2-3 sentences:
                1. What the word means in English
                2. How it is used poetically
                Keep it simple enough for a student to understand.
                Do not use markdown formatting.
            """.trimIndent()

            val json = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                })
            }

            val body = json.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder().url(URL).post(body).build()
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: return@withContext "Meaning not available."

            val result = JSONObject(responseBody)
            result
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")

        } catch (e: Exception) {
            "Could not fetch meaning. Please check your connection."
        }
    }

    suspend fun getPoemSummary(poemTitle: String, verses: List<String>): String = withContext(Dispatchers.IO) {
        try {
            val verseText = verses.joinToString("\n")
            val prompt = """
                This is a classical Kannada poem titled "$poemTitle":
                $verseText
                
                Write a simple 3-4 sentence explanation of what this poem means.
                Explain the imagery and emotion in simple modern language.
                Write for a student aged 18-22.
                Do not use markdown formatting.
            """.trimIndent()

            val json = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply {
                                put("text", prompt)
                            })
                        })
                    })
                })
            }

            val body = json.toString().toRequestBody("application/json".toMediaType())
            val request = Request.Builder().url(URL).post(body).build()
            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: return@withContext "Summary not available."

            val result = JSONObject(responseBody)
            result
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")

        } catch (e: Exception) {
            "Could not fetch summary. Please check your connection."
        }
    }
}