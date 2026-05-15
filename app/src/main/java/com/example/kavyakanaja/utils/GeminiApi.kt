package com.example.kavyakanaja.utils

import com.example.kavyakanaja.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiApi {

    private val API_KEY: String get() = BuildConfig.GEMINI_API_KEY

    private const val BASE_URL =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .build()

    private fun buildBody(prompt: String): okhttp3.RequestBody {
        val json = JSONObject().apply {
            put("contents", JSONArray().apply {
                put(JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply { put("text", prompt) })
                    })
                })
            })
        }
        return json.toString().toRequestBody("application/json".toMediaType())
    }

    private fun parseResponse(body: String): String {
        return JSONObject(body)
            .getJSONArray("candidates")
            .getJSONObject(0)
            .getJSONObject("content")
            .getJSONArray("parts")
            .getJSONObject(0)
            .getString("text")
            .trim()
    }

    suspend fun getWordMeaning(word: String): String = withContext(Dispatchers.IO) {
        try {
            val prompt = """
                The word "$word" is from a classical Kannada poem.
                Explain in 2-3 simple sentences what this word means in English
                and how it is used poetically. No bullet points. No markdown.
            """.trimIndent()

            val request = Request.Builder()
                .url("$BASE_URL?key=$API_KEY")
                .post(buildBody(prompt))
                .addHeader("Content-Type", "application/json")
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: return@withContext "No response."
            if (!response.isSuccessful) return@withContext "Error ${response.code}: Check API key."
            parseResponse(responseBody)
        } catch (e: Exception) {
            "Error: ${e.message}"
        }
    }

    suspend fun getPoemSummary(poemTitle: String, verses: List<String>): String =
        withContext(Dispatchers.IO) {
            try {
                val verseText = verses.filter { it.isNotEmpty() }.joinToString(" / ")
                val prompt = """
                    This classical Kannada poem is titled "$poemTitle": $verseText
                    Write a 3 sentence simple explanation of its meaning and emotion
                    for a college student. No bullet points. No markdown.
                """.trimIndent()

                val request = Request.Builder()
                    .url("$BASE_URL?key=$API_KEY")
                    .post(buildBody(prompt))
                    .addHeader("Content-Type", "application/json")
                    .build()

                val response = client.newCall(request).execute()
                val responseBody = response.body?.string() ?: return@withContext "No response."
                if (!response.isSuccessful) return@withContext "Error ${response.code}: Check API key."
                parseResponse(responseBody)
            } catch (e: Exception) {
                "Error: ${e.message}"
            }
        }
}