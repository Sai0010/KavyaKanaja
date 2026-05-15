package com.example.kavyakanaja.model

data class Poet(
    val id: Int,
    val nameKannada: String,
    val nameEnglish: String,
    val years: String,
    val award: String,
    val bio: String,
    val emoji: String = "📜"
)