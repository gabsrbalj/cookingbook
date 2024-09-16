package com.example.myrecipeapp


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.myrecipeapp.databinding.ActivityFrontPageBinding

class FrontPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFrontPageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrontPageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Home())

        binding.bottomNav.setOnNavigationItemReselectedListener {

            when(it.itemId){
                R.id.home -> replaceFragment(Home())
                R.id.add -> replaceFragment(Add())
                R.id.account -> replaceFragment(Account())
                R.id.mealPlanning -> replaceFragment(MealPlanningApiFragment())
                else ->  {

                }
            }
            true
        }
    }

    private fun replaceFragment (fragment: Fragment){

        val fragmentManager = supportFragmentManager
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment)
            commit()
        }
    }
}