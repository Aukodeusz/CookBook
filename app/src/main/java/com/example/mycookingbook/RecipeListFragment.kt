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

class RecipeListFragment : Fragment() {

    private lateinit var recipeAdapter: RecipeAdapter
    private val recipeList = mutableListOf<Recipe>()

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
            (activity as MainActivity).replaceFragment(RecipeDetailsFragment.newInstance(recipe))
        }
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = recipeAdapter

        view.findViewById<Button>(R.id.add_recipe_button).setOnClickListener {
            (activity as MainActivity).replaceFragment(AddRecipeFragment())
        }

        loadRecipes()
    }

    private fun loadRecipes() {
        val sharedPreferences = requireActivity().getSharedPreferences("CookbookPrefs", Context.MODE_PRIVATE)
        val savedRecipes = sharedPreferences.getStringSet("recipes", emptySet()) ?: emptySet()
        recipeList.clear()
        recipeList.addAll(savedRecipes.map { Recipe.fromJson(it) })
        recipeAdapter.notifyDataSetChanged()
    }
}
