package com.example.kavyakanaja.model

import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object SampleData {

    lateinit var poems: List<Poem>

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
            bio = "Putinapam Tirunarayana Narasimhachar was a Kannada poet, novelist and playwright. His work is known for its philosophical depth and devotional themes rooted in Vaishnava tradition.",
            emoji = "🕯️"
        ),
        Poet(
            id = 4,
            nameKannada = "ಶಿವರಾಮ ಕಾರಂತ",
            nameEnglish = "Shivarama Karanth",
            years = "1902 – 1997",
            award = "Jnanpith Award 1977",
            bio = "K. Shivaram Karanth was a multifaceted Kannada writer, activist and thinker from coastal Karnataka. He authored over 300 works spanning novels, plays, science writing and children's literature.",
            emoji = "📚"
        ),
        Poet(
            id = 5,
            nameKannada = "ಮಾಸ್ತಿ ವೆಂಕಟೇಶ ಅಯ್ಯಂಗಾರ್",
            nameEnglish = "Masti Venkatesha Iyengar",
            years = "1891 – 1986",
            award = "Jnanpith Award 1983",
            bio = "Masti Venkatesha Iyengar, fondly called Srigandhada Kavi, was a doyen of Kannada literature known for his short stories, poems and plays reflecting everyday human life.",
            emoji = "✍️"
        ),
        Poet(
            id = 6,
            nameKannada = "ವಿ.ಕೃ. ಗೋಕಾಕ",
            nameEnglish = "Vinayaka Krishna Gokak",
            years = "1909 – 1992",
            award = "Jnanpith Award 1990",
            bio = "V.K. Gokak was a Kannada poet, novelist and critic. He led the Gokak Movement that fought for Kannada as the official language of Karnataka. His epic poem Bharata Sindhu Rashmi brought him national recognition.",
            emoji = "🏆"
        ),
        Poet(
            id = 7,
            nameKannada = "ಯು.ಆರ್. ಅನಂತಮೂರ್ತಿ",
            nameEnglish = "U.R. Ananthamurthy",
            years = "1932 – 2014",
            award = "Jnanpith Award 1994",
            bio = "U.R. Ananthamurthy was one of the most influential Kannada writers of the 20th century. His novel Samskara is considered a landmark in Indian literature. He was a leading voice of the Navya literary movement.",
            emoji = "🌟"
        ),
        Poet(
            id = 8,
            nameKannada = "ಗಿರೀಶ್ ಕಾರ್ನಾಡ್",
            nameEnglish = "Girish Karnad",
            years = "1938 – 2019",
            award = "Jnanpith Award 1998",
            bio = "Girish Karnad was a celebrated Kannada playwright, poet, actor and film director. His plays Tughlaq, Hayavadana and Yayati drew from Indian mythology and history to comment on modern life.",
            emoji = "🎭"
        ),
        Poet(
            id = 9,
            nameKannada = "ಚಂದ್ರಶೇಖರ ಕಂಬಾರ",
            nameEnglish = "Chandrashekhara Kambara",
            years = "1937 – present",
            award = "Jnanpith Award 2010",
            bio = "Chandrashekhara Kambara is a Kannada poet, playwright and folklorist from North Karnataka. His works are deeply rooted in folk tradition and rural life. He was the first Vice-Chancellor of Kannada University.",
            emoji = "🪘"
        ),
        Poet(
            id = 10,
            nameKannada = "ಎಸ್.ಎಲ್. ಭೈರಪ್ಪ",
            nameEnglish = "S.L. Bhyrappa",
            years = "1931 – present",
            award = "Saraswati Samman 2010",
            bio = "S.L. Bhyrappa is one of the most widely read Kannada novelists. His works explore complex philosophical, social and historical themes. Vamshavruksha, Parva and Aavarana are among his most celebrated novels.",
            emoji = "📖"
        )
    )

    fun init(context: Context) {
        val json = context.assets.open("poems.json")
            .bufferedReader()
            .use { it.readText() }

        val type = object : TypeToken<List<Poem>>() {}.type

        poems = Gson().fromJson(json, type)
    }

    fun getPoemOfTheDay(): Poem {
        return poems.random()
    }
}