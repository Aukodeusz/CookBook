package com.example.cookbook

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycookingbook.MainActivity
import com.example.mycookingbook.R

class RecipeListFragment : Fragment() {

    val recipeList = mutableListOf<Recipe>()
    private lateinit var recipeAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_recipe_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicjalizacja RecyclerView
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recipeAdapter = RecipeAdapter(recipeList) { recipe ->
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RecipeDetailsFragment.newInstance(recipe))
                .addToBackStack(null)
                .commit()
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = recipeAdapter

        // Odczyt przepisów z pamięci lokalnej
        loadRecipes()

        // Obsługa przycisku "Dodaj Przepis"
        view.findViewById<Button>(R.id.add_recipe_button).setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddRecipeFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun loadRecipes() {
        val sharedPreferences = requireActivity().getSharedPreferences("CookbookPrefs", Context.MODE_PRIVATE)
        val recipesString = sharedPreferences.getString("recipes", "") ?: ""
        recipeList.clear()

        if (recipesString.isNotEmpty()) {
            val recipes = recipesString.split(";").filter { it.isNotEmpty() }
            for (recipeString in recipes) {
                val fields = recipeString.split("|")
                if (fields.size == 4) {
                    val name = fields[0]
                    val ingredients = fields[1]
                    val instructions = fields[2]
                    val rating = fields[3].toFloatOrNull() ?: 0f
                    recipeList.add(Recipe(name, ingredients, instructions, rating))
                }
            }
        }

        recipeAdapter.notifyDataSetChanged()
    }

    fun saveRecipes() {
        val sharedPreferences = requireActivity().getSharedPreferences("CookbookPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val recipesString = recipeList.joinToString(";") { recipe ->
            "${recipe.name}|${recipe.ingredients}|${recipe.instructions}|${recipe.rating}"
        }
        editor.putString("recipes", recipesString)
        editor.apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveRecipes() // Zapis przepisów przy zamknięciu widoku
    }
}
