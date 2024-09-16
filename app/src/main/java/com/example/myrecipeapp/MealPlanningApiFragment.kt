package com.example.myrecipeapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MealPlanningApiFragment : Fragment() {

    private lateinit var mealPlanRecyclerView: RecyclerView
    private lateinit var mealPlanAdapter: MealPlanAdapter
    private val mealPlans = mutableListOf<Meal>()
    private val apiKey = "404a951f706a466294cefb3f5cb152e0"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meal_planning, container, false)

        mealPlanRecyclerView = view.findViewById(R.id.mealPlanRecyclerView)
        mealPlanRecyclerView.layoutManager = LinearLayoutManager(context)
        mealPlanAdapter = MealPlanAdapter(mealPlans) { meal ->
            val intent = Intent(context, MealPlanDetailActivity::class.java).apply {
                putExtra("MEAL_ID", meal.id)
            }
            startActivity(intent)
        }
        mealPlanRecyclerView.adapter = mealPlanAdapter

        fetchMealPlan()

        return view
    }


    private fun fetchMealPlan() {
        RetrofitInstance.api.getMealPlan(apiKey, "day")
            .enqueue(object : Callback<MealPlanResponse> {
                override fun onResponse(call: Call<MealPlanResponse>, response: Response<MealPlanResponse>) {
                    if (response.isSuccessful) {
                        mealPlans.clear()
                        response.body()?.let {
                            mealPlans.addAll(it.meals) // Ensure this list is populated
                        }
                        mealPlanAdapter.notifyDataSetChanged()
                    }
                }

                override fun onFailure(call: Call<MealPlanResponse>, t: Throwable) {
                    // Handle error
                }
            })
    }

}
