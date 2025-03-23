package com.example.mycookingbook

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecipeListFragment : Fragment() {

    val recipeList = mutableListOf<Recipe>()
    lateinit var recipeAdapter: RecipeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_recipe_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view)
        recipeAdapter = RecipeAdapter(recipeList) { recipe ->
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RecipeDetailsFragment.newInstance(recipe))
                .addToBackStack(null)
                .commit()
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = recipeAdapter

        view.findViewById<Button>(R.id.add_recipe_button).setOnClickListener {
            (activity as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddRecipeFragment())
                .addToBackStack(null)
                .commit()
        }

        // Wczytywanie przepisów
        loadRecipes()
    }

    fun loadRecipes() {
        val sharedPreferences = requireActivity().getSharedPreferences("CookbookPrefs", Context.MODE_PRIVATE)
        val recipesString = sharedPreferences.getString("recipes", "") ?: ""
        recipeList.clear()

        if (recipesString.isNotEmpty()) {
            try {
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
                Log.d("RecipeListFragment", "Załadowano przepisy: ${recipeList.size}")
            } catch (e: Exception) {
                Log.e("RecipeListFragment", "Błąd podczas wczytywania przepisów: ${e.message}")
            }
        } else {
            Log.d("RecipeListFragment", "Brak zapisanych przepisów, dodano przykładowe.")
            recipeList.add(Recipe("Spaghetti Carbonara", "Makaron, jajka, boczek, parmezan", "Ugotuj makaron i wymieszaj z sosem.", 4.5f))
            recipeList.add(Recipe("Tiramisu", "Biszkopty, kawa, mascarpone", "Przygotuj warstwowy deser i schłódź.", 5.0f))
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

        Log.d("RecipeListFragment", "Zapisano przepisy: ${recipeList.size}")
    }
}
