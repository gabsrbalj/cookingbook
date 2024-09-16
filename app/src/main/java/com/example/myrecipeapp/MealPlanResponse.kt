package com.example.myrecipeapp

data class MealPlanResponse(
    val meals: List<Meal>,
    val calories: Int
)
