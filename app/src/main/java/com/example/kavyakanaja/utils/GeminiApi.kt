package com.example.kavyakanaja.utils

import android.util.Log
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

    private val API_KEY: String
        get() = BuildConfig.GEMINI_API_KEY

    private const val BASE_URL =
        "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent"
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

                        put(JSONObject().apply {

                            put("text", prompt)

                        })
                    })
                })
            })
        }

        return json.toString()
            .toRequestBody("application/json".toMediaType())
    }

    private fun parseResponse(body: String): String {

        return try {

            JSONObject(body)
                .getJSONArray("candidates")
                .getJSONObject(0)
                .getJSONObject("content")
                .getJSONArray("parts")
                .getJSONObject(0)
                .getString("text")
                .trim()

        } catch (e: Exception) {

            Log.e("GeminiAPI", "Response parse failed", e)

            "Failed to parse AI response."
        }
    }

    private suspend fun sendPrompt(prompt: String): String =
        withContext(Dispatchers.IO) {

            try {

                if (API_KEY.isBlank()) {
                    return@withContext "Gemini API key missing."
                }

                val request = Request.Builder()
                    .url("$BASE_URL?key=$API_KEY")
                    .post(buildBody(prompt))
                    .addHeader("Content-Type", "application/json")
                    .build()

                Log.d("GeminiAPI", "Sending request...")

                val response = client.newCall(request).execute()

                val responseBody = response.body?.string()

                Log.d("GeminiAPI", "Response code: ${response.code}")
                Log.d("GeminiAPI", "Response body: $responseBody")

                if (!response.isSuccessful) {

                    return@withContext buildString {

                        append("API Error ${response.code}")

                        if (!responseBody.isNullOrBlank()) {
                            append("\n")
                            append(responseBody)
                        }
                    }
                }

                if (responseBody.isNullOrBlank()) {
                    return@withContext "Empty response from Gemini API."
                }

                parseResponse(responseBody)

            } catch (e: Exception) {

                Log.e("GeminiAPI", "Request failed", e)

                "Error: ${e.message}"
            }
        }

    suspend fun getWordMeaning(word: String): String {

        val prompt = """
            The word "$word" is from a classical Kannada poem.
            Explain in 2-3 simple sentences what this word means in English
            and how it is used poetically.
            No bullet points.
            No markdown.
        """.trimIndent()

        return sendPrompt(prompt)
    }

    suspend fun getPoemSummary(
        poemTitle: String,
        verses: List<String>
    ): String {

        val verseText = verses
            .filter { it.isNotBlank() }
            .joinToString(" / ")

        val prompt = """
            This classical Kannada poem is titled "$poemTitle".

            Poem:
            $verseText

            Write a simple 3 sentence explanation of the meaning,
            theme, and emotional tone for a college student.

            No bullet points.
            No markdown.
        """.trimIndent()

        return sendPrompt(prompt)
    }
}