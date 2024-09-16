package com.example.myrecipeapp




import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myrecipeapp.databinding.FragmentAddBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.util.UUID


class Add : Fragment() {

    private var _binding: FragmentAddBinding? = null // Make binding nullable
    private val binding get() = _binding!! // Safe access to the binding
    private lateinit var databaseReference: DatabaseReference // Firebase database reference
    private lateinit var auth: FirebaseAuth
    private lateinit var storageReference: StorageReference // Firebase Storage reference
    private val ingredientsList = mutableListOf<String>() // Mutable list to hold ingredients
    private var imageUri: Uri? = null // URI for selected image
    private val REQUEST_IMAGE_PICK = 123 // Request code for image pick

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Storage
        storageReference = FirebaseStorage.getInstance().getReference("Recipe Images")

        // Check if the user is logged in
        if (auth.currentUser == null) {
            // Redirect to the login screen if no user is logged in
            Toast.makeText(requireContext(), "User not logged in. Redirecting to login.", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        } else {
            // User is logged in, initialize Firebase database reference
            Toast.makeText(requireContext(), "Logged in as: ${auth.currentUser?.email}", Toast.LENGTH_SHORT).show()

            // Initialize the Firebase Realtime Database reference
            databaseReference = FirebaseDatabase.getInstance("https://recipeapp-80c42-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Recipe information")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Initialize view binding
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Check if databaseReference is initialized properly
        if (!::databaseReference.isInitialized) {
            Toast.makeText(requireContext(), "Database not initialized", Toast.LENGTH_SHORT).show()
            return
        }

        // Choose image button click listener
        binding.btnChooseImage.setOnClickListener {
            chooseImageFromGallery()
        }

        // Add ingredient button click listener
        binding.btnAddIngredient.setOnClickListener {
            val ingredient = binding.etIngredient.text.toString()
            if (ingredient.isNotEmpty()) {
                ingredientsList.add(ingredient)
                binding.etIngredient.text.clear()
                Toast.makeText(requireContext(), "Ingredient added", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "Please enter an ingredient", Toast.LENGTH_SHORT).show()
            }
        }

        // Create new recipe button click listener
        binding.btnCreateNew.setOnClickListener {
            uploadRecipe()
        }
    }

    private fun chooseImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, REQUEST_IMAGE_PICK)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK) {
            imageUri = data?.data
            binding.ivRecipeImage.setImageURI(imageUri) // Display selected image in ImageView
        }
    }

    private fun uploadRecipe() {
        val recipeName = binding.etName.text.toString()
        val recipeDescription = binding.etDescription.text.toString()

        if (recipeName.isEmpty() || recipeDescription.isEmpty() || imageUri == null) {
            Toast.makeText(requireContext(), "Please fill all fields and choose an image", Toast.LENGTH_SHORT).show()
            return
        }

        val recipeID = databaseReference.push().key ?: UUID.randomUUID().toString()
        val userId = auth.currentUser?.uid ?: "Anonymous"
        val imageRef = storageReference.child("$recipeID.jpg")

        // Upload image to Firebase Storage
        imageUri?.let { uri ->
            imageRef.putFile(uri).addOnSuccessListener {
                // Get the image download URL
                imageRef.downloadUrl.addOnSuccessListener { downloadUrl ->
                    val recipe = Recipe(
                        id = recipeID,
                        name = recipeName,
                        description = recipeDescription,
                        ingredients = ingredientsList,
                        imageUrl = downloadUrl.toString(), // Store the image URL
                        userId = userId
                    )

                    // Save the recipe to Firebase Realtime Database
                    databaseReference.child(recipeID).setValue(recipe).addOnSuccessListener {
                        // Clear text fields and image
                        binding.etName.text.clear()
                        binding.etDescription.text.clear()
                        ingredientsList.clear()
                        binding.etIngredient.text.clear()
                        binding.ivRecipeImage.setImageURI(null) // Clear the image

                        // Show success message
                        Toast.makeText(requireContext(), "Recipe Saved", Toast.LENGTH_SHORT).show()

                        // Navigate to a new activity
                        val intent = Intent(requireContext(), FrontPageActivity::class.java)
                        startActivity(intent)

                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), "Failed to save recipe: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to upload image: ${it.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Nullify the binding to avoid memory leaks
    }

    companion object {
        @JvmStatic
        fun newInstance() = Add()
    }
}


