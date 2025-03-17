package com.example.cookbook

import java.io.Serializable

data class Recipe(
    val name: String,
    val ingredients: String,
    val instructions: String,
    val rating: Float
) : Serializable
