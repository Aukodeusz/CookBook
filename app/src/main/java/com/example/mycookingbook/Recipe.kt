package com.example.cookbook

import com.google.gson.Gson

data class Recipe(
    val name: String,
    val ingredients: String,
    val instructions: String,
    val rating: Float
) {
    fun toJson(): String = Gson().toJson(this)
    companion object {
        fun fromJson(json: String): Recipe = Gson().fromJson(json, Recipe::class.java)
    }
}
