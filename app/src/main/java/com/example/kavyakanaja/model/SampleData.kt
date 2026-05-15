package com.example.kavyakanaja.model

import android.content.Context
import org.json.JSONArray

object SampleData {

    private var _poems: List<Poem> = emptyList()
    private var _initialized = false

    fun init(context: Context) {
        if (_initialized) return
        try {
            val json = context.assets.open("poems.json")
                .bufferedReader().use { it.readText() }
            val arr = JSONArray(json)
            val list = mutableListOf<Poem>()
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                val versesArr = obj.getJSONArray("verses")
                val verses = (0 until versesArr.length()).map { versesArr.getString(it) }
                list.add(
                    Poem(
                        id = obj.getInt("id"),
                        titleKannada = obj.getString("titleKannada"),
                        titleEnglish = obj.getString("titleEnglish"),
                        authorKannada = obj.getString("authorKannada"),
                        authorEnglish = obj.getString("authorEnglish"),
                        year = obj.getString("year"),
                        category = obj.getString("category"),
                        verses = verses,
                        poetId = obj.getInt("poetId")
                    )
                )
            }
            _poems = list
            _initialized = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    val poems: List<Poem> get() = _poems

    fun getPoemOfTheDay(): Poem {
        if (_poems.isEmpty()) return Poem(1, "ಹೊಂಗನಸು", "Golden Dream", "ಕುವೆಂಪು", "Kuvempu", "1945", "Classical", emptyList())
        val dayOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        return _poems[dayOfYear % _poems.size]
    }

    val poets = listOf(
        Poet(
            id = 1,
            nameKannada = "ಕುವೆಂಪು",
            nameEnglish = "Kuvempu (K.V. Puttappa)",
            years = "1904 – 1994",
            award = "Jnanpith Award 1967",
            bio = "Known as Rashtrakavi (National Poet) of Karnataka, Kuvempu was a poet, playwright, novelist and critic. His works celebrate nature, humanism and the spirit of Karnataka. He wrote the Karnataka state anthem.",
            emoji = "🖊️"
        ),
        Poet(
            id = 2,
            nameKannada = "ದ.ರಾ. ಬೇಂದ್ರೆ",
            nameEnglish = "D.R. Bendre",
            years = "1896 – 1981",
            award = "Jnanpith Award 1974",
            bio = "Dattatreya Ramachandra Bendre, known as Ambikatanayadatta, was a celebrated Kannada poet from Dharwad. His poetry is deeply rooted in nature, spirituality and Kannada folk traditions.",
            emoji = "📿"
        ),
        Poet(
            id = 3,
            nameKannada = "ಪು.ತಿ. ನರಸಿಂಹಾಚಾರ್",
            nameEnglish = "Pu.Ti. Narasimhachar",
            years = "1905 – 1998",
            award = "Jnanpith Award 1977",
            bio = "Putinapam Tirunarayana Narasimhachar was a Kannada poet, novelist and playwright. His work is known for its philosophical depth and devotional themes.",
            emoji = "🕯️"
        )
    )
}