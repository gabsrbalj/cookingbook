package com.example.myrecipeapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class Home : Fragment() {

    private lateinit var recipeRecyclerView: RecyclerView
    private lateinit var recipeAdapter: RecipeAdapter
    private val recipeList = ArrayList<Recipe>()
    private lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance("https://recipeapp-80c42-default-rtdb.europe-west1.firebasedatabase.app/").getReference("Recipe information")

        // Initialize RecyclerView
        recipeRecyclerView = view.findViewById(R.id.recipeRecyclerView)
        recipeRecyclerView.layoutManager = LinearLayoutManager(context)

        // Initialize RecipeAdapter with a click listener
        recipeAdapter = RecipeAdapter(recipeList) { recipe ->
            // Handle recipe click
            showRecipeDetail(recipe)
        }
        recipeRecyclerView.adapter = recipeAdapter

        // Fetch data from Firebase
        fetchRecipes()

        return view
    }

    private fun fetchRecipes() {
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                recipeList.clear() // Clear the list before adding new data
                for (snapshot in dataSnapshot.children) {
                    val recipe = snapshot.getValue(Recipe::class.java)
                    if (recipe != null) {
                        recipeList.add(recipe)
                    }
                }
                recipeAdapter.notifyDataSetChanged() // Notify the adapter that data has changed
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle possible errors
            }
        })
    }

    private fun showRecipeDetail(recipe: Recipe) {
        // Here you can start an activity or display the recipe details in a new fragment.
        // For this example, we'll just show a Toast message.
        Toast.makeText(context, "Clicked on ${recipe.name}", Toast.LENGTH_SHORT).show()
    }
}





