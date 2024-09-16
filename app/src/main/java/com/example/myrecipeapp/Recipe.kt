package com.example.myrecipeapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recipe(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val ingredients: List<String> = listOf(),
    val imageUrl: String = "",  // Added imageUrl field
    val userId: String = ""
) : Parcelable


