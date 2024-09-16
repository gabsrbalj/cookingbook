package com.example.myrecipeapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class RecipeAdapter(
    private val recipes: ArrayList<Recipe>,
    private val onRecipeClick: (Recipe) -> Unit // Lambda function to handle recipe click
) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    class RecipeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameTextView: TextView = view.findViewById(R.id.recipeName)
        val ingredientsTextView: TextView = view.findViewById(R.id.recipeIngredients)
        val instructionsTextView: TextView = view.findViewById(R.id.recipeDescription)
        val recipeImageView: ImageView = view.findViewById(R.id.recipeImage) // Updated to ImageView
        val showMoreButton: TextView = view.findViewById(R.id.showMoreButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]
        holder.nameTextView.text = recipe.name

        // Join ingredients list into a single string separated by commas
        holder.ingredientsTextView.text = recipe.ingredients.joinToString(separator = ", ")

        // Show a portion of the description
        holder.instructionsTextView.text = if (recipe.description.length > 100) {
            recipe.description.substring(0, 100) + "..." // Show first 100 characters
        } else {
            recipe.description
        }

        // Load image into ImageView
        Picasso.get().load(recipe.imageUrl).into(holder.recipeImageView)

        // Show More button click listener
        holder.showMoreButton.setOnClickListener {
            // Toggle between showing more or less
            if (holder.instructionsTextView.text.endsWith("...")) {
                holder.instructionsTextView.text = recipe.description
                holder.showMoreButton.text = "Show Less"
            } else {
                holder.instructionsTextView.text = recipe.description.substring(0, 100) + "..."
                holder.showMoreButton.text = "Show More"
            }
        }

        // Set click listener for the entire item view
        holder.itemView.setOnClickListener {
            onRecipeClick(recipe) // Invoke the lambda function
        }
    }

    override fun getItemCount(): Int {
        return recipes.size
    }
}
