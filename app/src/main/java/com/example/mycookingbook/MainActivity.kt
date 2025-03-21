package com.example.mycookingbook

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.cookbook.RecipeListFragment
import com.example.mycookingbook.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RecipeListFragment())
                .commit()
        }
    }
}
