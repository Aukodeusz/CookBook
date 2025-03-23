package com.example.mycookingbook

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment

class AddRecipeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_add_recipe, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameInput = view.findViewById<EditText>(R.id.recipe_name_input)
        val ingredientsInput = view.findViewById<EditText>(R.id.recipe_ingredients_input)
        val instructionsInput = view.findViewById<EditText>(R.id.recipe_instructions_input)
        val ratingBar = view.findViewById<RatingBar>(R.id.recipe_rating_bar)

        view.findViewById<Button>(R.id.save_recipe_button).setOnClickListener {
            val name = nameInput.text.toString()
            val ingredients = ingredientsInput.text.toString()
            val instructions = instructionsInput.text.toString()
            val rating = ratingBar.rating

            if (name.isNotEmpty() && ingredients.isNotEmpty() && instructions.isNotEmpty()) {
                val newRecipe = Recipe(name, ingredients, instructions, rating)

                val parentFragment =
                    requireActivity().supportFragmentManager.findFragmentById(R.id.fragment_container)
                if (parentFragment is RecipeListFragment) {
                    parentFragment.recipeList.add(newRecipe)
                    parentFragment.saveRecipes()
                    parentFragment.recipeAdapter.notifyDataSetChanged()
                    Log.d("AddRecipeFragment", "Dodano przepis: ${newRecipe.name}")
                }

                Toast.makeText(context, "Dodano przepis: ${newRecipe.name}", Toast.LENGTH_SHORT).show()
                requireActivity().supportFragmentManager.popBackStack()
            } else {
                Toast.makeText(context, "Wypełnij wszystkie pola!", Toast.LENGTH_SHORT).show()
                Log.w("AddRecipeFragment", "Nie można dodać przepisu - brak danych.")
            }
        }
    }
}
