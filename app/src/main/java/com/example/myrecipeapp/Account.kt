package com.example.myrecipeapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class Account : Fragment() {

    private lateinit var accountRecyclerView: RecyclerView
    private lateinit var accountAdapter: AccountAdapter
    private val recipeList = mutableListOf<Recipe>()
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String
    private lateinit var auth: FirebaseAuth
    private lateinit var logoutButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        // Initialize Firebase Database
        databaseReference = FirebaseDatabase.getInstance("https://recipeapp-80c42-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("Recipe information")

        // Get the logged-in user's ID
        userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        if (userId.isEmpty()) {
            Log.e("AccountFragment", "User is not logged in or user ID is empty")
            return view
        }

        // Initialize RecyclerView
        accountRecyclerView = view.findViewById(R.id.accountRecyclerView)
        accountRecyclerView.layoutManager = LinearLayoutManager(context)
        accountAdapter = AccountAdapter(recipeList, ::onEditClick, ::onDeleteClick)
        accountRecyclerView.adapter = accountAdapter

        fetchUserRecipes()


        auth = FirebaseAuth.getInstance()
        logoutButton = view.findViewById(R.id.btnLogout)

        logoutButton.setOnClickListener {
            performLogout()
        }


        return view
    }
    private fun performLogout() {
        auth.signOut()
        Toast.makeText(activity, "Logged out successfully", Toast.LENGTH_SHORT).show()

        // Redirect to login activity
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish() // Optionally finish the current activity
    }

    private fun fetchUserRecipes() {
        databaseReference.orderByChild("userId").equalTo(userId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                recipeList.clear()
                for (snapshot in dataSnapshot.children) {
                    val recipe = snapshot.getValue(Recipe::class.java)
                    if (recipe != null) {
                        recipeList.add(recipe)
                    }
                }
                accountAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("AccountFragment", "Error fetching recipes: ${databaseError.message}")
            }
        })
    }

    private fun onEditClick(recipe: Recipe) {
        val intent = Intent(activity, EditRecipeActivity::class.java)
        intent.putExtra("RECIPE_ID", recipe.id)
        startActivity(intent)
    }

    private fun onDeleteClick(recipe: Recipe) {
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Recipe")
            .setMessage("Are you sure you want to delete this recipe?")
            .setPositiveButton("Yes") { _, _ ->
                databaseReference.child(recipe.id).removeValue()
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            // Recipe successfully deleted
                        } else {
                            Log.e("AccountFragment", "Error deleting recipe: ${it.exception?.message}")
                        }
                    }
            }
            .setNegativeButton("No", null)
            .show()
    }
}
