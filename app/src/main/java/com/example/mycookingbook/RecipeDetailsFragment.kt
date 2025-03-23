package com.example.mycookingbook

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import android.widget.LinearLayout

class RecipeDetailsFragment : Fragment() {

    companion object {
        private const val ARG_RECIPE = "recipe"

        fun newInstance(recipe: Recipe): RecipeDetailsFragment {
            val fragment = RecipeDetailsFragment()
            val args = Bundle()
            args.putSerializable(ARG_RECIPE, recipe)
            fragment.arguments = args
            return fragment
        }
    }

    private val commentsList = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_recipe_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recipe = arguments?.getSerializable(ARG_RECIPE) as? Recipe
        recipe?.let {
            view.findViewById<TextView>(R.id.recipe_name).text = it.name
            view.findViewById<TextView>(R.id.recipe_ingredients).text = it.ingredients
            view.findViewById<TextView>(R.id.recipe_instructions).text = it.instructions
            view.findViewById<TextView>(R.id.recipe_rating).text = "Ocena: ${it.rating}"

            loadComments(it.name, view.findViewById(R.id.comments_container))
        }

        val commentsContainer = view.findViewById<LinearLayout>(R.id.comments_container)
        val commentInput = view.findViewById<EditText>(R.id.comment_input)
        view.findViewById<Button>(R.id.add_comment_button).setOnClickListener {
            val comment = commentInput.text.toString()
            if (comment.isNotEmpty() && recipe != null) {
                commentsList.add(comment)
                addCommentToView(commentsContainer, comment)
                saveComments(recipe.name)
                commentInput.text.clear()
                Toast.makeText(context, "Dodano komentarz!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Komentarz nie może być pusty!", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<Button>(R.id.back_button).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    private fun addCommentToView(container: LinearLayout, comment: String) {
        val textView = TextView(requireContext()).apply {
            text = comment
            textSize = 16f
            setPadding(8, 8, 8, 8)
        }
        container.addView(textView)
    }

    private fun saveComments(recipeName: String) {
        val sharedPreferences = requireActivity().getSharedPreferences("RecipeComments", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val commentsString = commentsList.joinToString(";")
        editor.putString(recipeName, commentsString)
        editor.apply()
    }

    private fun loadComments(recipeName: String, container: LinearLayout) {
        val sharedPreferences = requireActivity().getSharedPreferences("RecipeComments", Context.MODE_PRIVATE)
        val commentsString = sharedPreferences.getString(recipeName, "") ?: ""
        commentsList.clear()
        if (commentsString.isNotEmpty()) {
            commentsList.addAll(commentsString.split(";").filter { it.isNotEmpty() })
            commentsList.forEach { comment ->
                addCommentToView(container, comment)
            }
        }
    }
}
