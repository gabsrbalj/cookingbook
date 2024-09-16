package com.example.myrecipeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MealPlanAdapter(
    private val meals: List<Meal>,
    private val onMealClick: (Meal) -> Unit
) : RecyclerView.Adapter<MealPlanAdapter.MealViewHolder>() {

    inner class MealViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mealTitle: TextView = view.findViewById(R.id.mealTitle)
        val mealImage: ImageView = view.findViewById(R.id.mealImage)
        val mealReadyInMinutes: TextView = view.findViewById(R.id.mealReadyInMinutes)
        val mealServings: TextView = view.findViewById(R.id.mealServings)
        val cardView: CardView = view.findViewById(R.id.cardView) // Dodaj CardView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_meal_plan, parent, false)
        return MealViewHolder(view)
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val meal = meals[position]
        holder.mealTitle.text = meal.title
        holder.mealReadyInMinutes.text = "Ready in: ${meal.readyInMinutes} minutes"
        holder.mealServings.text = "Servings: ${meal.servings}"

        Glide.with(holder.itemView.context)
            .load(meal.image)
            .placeholder(R.drawable.img)
            .into(holder.mealImage)

        holder.cardView.setOnClickListener {
            onMealClick(meal)
        }
    }

    override fun getItemCount(): Int {
        return meals.size
    }
}
