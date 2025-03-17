package com.example.cookbook

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mycookingbook.R

class RecipeDetailsFragment : Fragment() {

    private lateinit var recipe: Recipe
    private val comments = mutableListOf<Comment>()
    private lateinit var commentsAdapter: CommentsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_recipe_details, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recipe = arguments?.getSerializable("recipe") as Recipe

        // Wyświetlanie szczegółów przepisu
        view.findViewById<TextView>(R.id.recipe_name).text = recipe.name
        view.findViewById<TextView>(R.id.recipe_ingredients).text = recipe.ingredients
        view.findViewById<TextView>(R.id.recipe_instructions).text = recipe.instructions
        val ratingBar = view.findViewById<RatingBar>(R.id.recipe_rating_bar)
        ratingBar.rating = recipe.rating

        // Ustawienia RecyclerView dla komentarzy
        val recyclerView = view.findViewById<RecyclerView>(R.id.comments_recycler_view)
        commentsAdapter = CommentsAdapter(comments)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = commentsAdapter

        // Obsługa dodawania komentarzy
        val commentInput = view.findViewById<EditText>(R.id.comment_input)
        view.findViewById<Button>(R.id.add_comment_button).setOnClickListener {
            val commentText = commentInput.text.toString()
            if (commentText.isNotEmpty()) {
                comments.add(Comment(commentText))
                commentsAdapter.notifyDataSetChanged()
                commentInput.text.clear()
                saveComments()
            }
        }

        // Ładowanie zapisanych komentarzy
        loadComments()
    }

    private fun loadComments() {
        val sharedPreferences = requireActivity().getSharedPreferences("CookbookPrefs", Context.MODE_PRIVATE)
        val commentsString = sharedPreferences.getString(recipe.name + "_comments", "") ?: ""
        comments.clear()

        if (commentsString.isNotEmpty()) {
            val commentTexts = commentsString.split(";").filter { it.isNotEmpty() }
            for (text in commentTexts) {
                comments.add(Comment(text))
            }
        }

        commentsAdapter.notifyDataSetChanged()
    }

    private fun saveComments() {
        val sharedPreferences = requireActivity().getSharedPreferences("CookbookPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val commentsString = comments.joinToString(";") { it.text }
        editor.putString(recipe.name + "_comments", commentsString)
        editor.apply()
    }

    companion object {
        fun newInstance(recipe: Recipe): RecipeDetailsFragment {
            val fragment = RecipeDetailsFragment()
            val args = Bundle()
            args.putSerializable("recipe", recipe)
            fragment.arguments = args
            return fragment
        }
    }
}
