package com.example.myrecipeapp

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class EditRecipeActivity : AppCompatActivity() {

    private lateinit var databaseReference: DatabaseReference
    private lateinit var recipeId: String
    private lateinit var nameEditText: EditText
    private lateinit var ingredientsEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var saveButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_recipe)

        recipeId = intent.getStringExtra("RECIPE_ID") ?: ""
        if (recipeId.isEmpty()) {
            Log.e("EditRecipeActivity", "No Recipe ID found")
            finish()
            return
        }

        databaseReference = FirebaseDatabase.getInstance("https://recipeapp-80c42-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Recipe information").child(recipeId)

        nameEditText = findViewById(R.id.editRecipeName)
        ingredientsEditText = findViewById(R.id.editRecipeIngredients)
        descriptionEditText = findViewById(R.id.editRecipeDescription)
        saveButton = findViewById(R.id.saveButton)

        loadRecipe()

        saveButton.setOnClickListener {
            saveRecipe()
        }
    }

    private fun loadRecipe() {
        databaseReference.get().addOnSuccessListener { snapshot ->
            val recipe = snapshot.getValue(Recipe::class.java)
            if (recipe != null) {
                nameEditText.setText(recipe.name)
                ingredientsEditText.setText(recipe.ingredients.joinToString(", "))
                descriptionEditText.setText(recipe.description)
            } else {
                Log.e("EditRecipeActivity", "Recipe not found")
                finish()
            }
        }.addOnFailureListener {
            Log.e("EditRecipeActivity", "Error loading recipe: ${it.message}")
        }
    }

    private fun saveRecipe() {
        val name = nameEditText.text.toString()
        val ingredients = ingredientsEditText.text.toString().split(", ").toList()
        val description = descriptionEditText.text.toString()

        val updatedRecipe = Recipe(
            id = recipeId,
            name = name,
            description = description,
            ingredients = ingredients
        )

        databaseReference.setValue(updatedRecipe).addOnCompleteListener {
            if (it.isSuccessful) {
                finish()
            } else {
                Log.e("EditRecipeActivity", "Error saving recipe: ${it.exception?.message}")
            }
        }
    }
}
