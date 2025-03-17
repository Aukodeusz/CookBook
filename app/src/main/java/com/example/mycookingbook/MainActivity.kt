package com.example.mycookingbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Load the RecipeListFragment as the default screen
        if (savedInstanceState == null) {
            replaceFragment(RecipeListFragment())
        }
    }

    /**
     * Replaces the current fragment with the specified one.
     * Adds the transaction to the back stack for navigation.
     */
    fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}
