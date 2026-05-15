// model/Poem.kt

package com.example.kavyakanaja.model

data class Poem(
    val id: Int,
    val titleKannada: String,
    val titleEnglish: String,
    val authorKannada: String,
    val authorEnglish: String,
    val year: String,
    val category: String,
    val verses: List<String>,      // each string = one line of the poem
    val audioFile: String = "",    // filename in assets, empty for now
    val poetId: Int = 0,
    val isFavourite: Boolean = false
)