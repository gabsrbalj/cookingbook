package com.example.myrecipeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AccountAdapter(
    private val recipes: List<Recipe>,
    private val onEditClick: (Recipe) -> Unit,
    private val onDeleteClick: (Recipe) -> Unit
) : RecyclerView.Adapter<AccountAdapter.RecipeAccountViewHolder>() {

    class RecipeAccountViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.recipeName)
        val ingredientsTextView: TextView = view.findViewById(R.id.recipeIngredients)
        val instructionsTextView: TextView = view.findViewById(R.id.recipeDescription)
        val editButton: Button = view.findViewById(R.id.editButton)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
        val imageOfRecipe : ImageView = view.findViewById(R.id.recipeImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeAccountViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe_account, parent, false)
        return RecipeAccountViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeAccountViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.nameTextView.text = recipe.name
        holder.ingredientsTextView.text = recipe.ingredients.joinToString(", ")
        holder.instructionsTextView.text = recipe.description
        Glide.with(holder.itemView.context)
            .load(recipe.imageUrl) // Make sure 'imageUrl' points to a valid image URL
            .placeholder(R.drawable.img) // Optional placeholder image
            .into(holder.imageOfRecipe)
        holder.editButton.setOnClickListener { onEditClick(recipe) }
        holder.deleteButton.setOnClickListener { onDeleteClick(recipe) }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}
