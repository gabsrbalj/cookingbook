import com.example.myrecipeapp.Meal
import com.example.myrecipeapp.MealPlanResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MealPlanningApiService {
    @GET("mealplanner/generate")
    fun getMealPlan(
        @Query("apiKey") apiKey: String,
        @Query("timeFrame") timeFrame: String
    ): Call<MealPlanResponse>

    @GET("recipes/{id}")
    fun getMealDetails(
        @Path("id") id: String,
        @Query("apiKey") apiKey: String
    ): Call<Meal>
}
