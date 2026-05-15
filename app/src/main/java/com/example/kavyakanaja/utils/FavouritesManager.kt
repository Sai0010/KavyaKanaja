package com.example.kavyakanaja.utils

import android.content.Context

object FavouritesManager {

    private const val PREFS_NAME = "kavya_prefs"
    private const val KEY_FAVOURITES = "favourite_poem_ids"

    fun getFavouriteIds(context: Context): Set<Int> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val raw = prefs.getStringSet(KEY_FAVOURITES, emptySet()) ?: emptySet()
        return raw.map { it.toInt() }.toSet()
    }

    fun toggleFavourite(context: Context, poemId: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val current = getFavouriteIds(context).toMutableSet()
        if (current.contains(poemId)) current.remove(poemId)
        else current.add(poemId)
        prefs.edit().putStringSet(KEY_FAVOURITES, current.map { it.toString() }.toSet()).apply()
    }

    fun isFavourite(context: Context, poemId: Int): Boolean {
        return getFavouriteIds(context).contains(poemId)
    }
}