
    package com.example.myrecipeapp

    import android.os.Bundle
    import android.widget.ImageView
    import android.widget.TextView
    import androidx.appcompat.app.AppCompatActivity
    import com.bumptech.glide.Glide
    import retrofit2.Call
    import retrofit2.Callback
    import retrofit2.Response

    class MealPlanDetailActivity : AppCompatActivity() {

        private val apiKey = "404a951f706a466294cefb3f5cb152e0"

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_meal_plan_detail)

            val recipeId = intent.getStringExtra("MEAL_ID") ?: return

            fetchRecipeDetails(recipeId)
        }

        private fun fetchRecipeDetails(recipeId: String) {
            RetrofitInstance.api.getMealDetails(recipeId, apiKey)
                .enqueue(object : Callback<Meal> {
                    override fun onResponse(call: Call<Meal>, response: Response<Meal>) {
                        if (response.isSuccessful) {
                            val meal = response.body()
                            displayMealDetails(meal)
                        }
                    }

                    override fun onFailure(call: Call<Meal>, t: Throwable) {
                        // Handle error
                    }
                })
        }

        private fun displayMealDetails(meal: Meal?) {
            if (meal == null) return

            findViewById<TextView>(R.id.recipeTitle).text = meal.title
            findViewById<TextView>(R.id.recipeDetails).text = meal.summary

            Glide.with(this)
                .load(meal.image)
                .placeholder(R.drawable.img)
                .into(findViewById<ImageView>(R.id.recipeImage))
        }
    }

