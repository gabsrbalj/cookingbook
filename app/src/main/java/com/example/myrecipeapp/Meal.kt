package com.example.myrecipeapp

data class Meal(
    val id: String,
    val title: String,
    val ingredients: List<String>,
    val summary: String,
    val readyInMinutes: Int,  // Novo polje za vrijeme pripreme
    val image: String? = null ,
    val servings: Int// Opcionalno polje za URL slike
)

