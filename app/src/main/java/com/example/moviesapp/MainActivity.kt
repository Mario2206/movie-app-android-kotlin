package com.example.moviesapp
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bellano.themoviedblibrary.network.ApiHelper
import com.bellano.themoviedblibrary.network.models.Movie
import com.example.moviesapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var movies = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setup binding
        val binding: ActivityMainBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_main
        )
        binding.search = ""

        //setup recycler view
        val movieListView = setupRecyclerView()

        //setup listeners
        setupListeners(binding, movieListView)

        // Init fetching
        getPopularMovies(movieListView)
    }

    private fun setupRecyclerView() : RecyclerView {
        val movieListView = findViewById<RecyclerView>(R.id.movieList)
        movieListView.layoutManager = LinearLayoutManager(this)
        movieListView.adapter = MovieListAdapter(movies, MovieListAdapter.OnClickListener {
            val intent = Intent(this, MovieActivity::class.java)
            intent.putExtra("MOVIE_ID", it.id.toString())
            startActivity(intent)
        })
        return movieListView
    }

    private fun setupListeners(binding: ActivityMainBinding, movieListView: RecyclerView) {
        val editSearchView = binding.editSearch

        editSearchView.doAfterTextChanged {
            binding.search = it.toString()
        }

        editSearchView.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            println(binding.search.toString())
            println(actionId)
            println(EditorInfo.IME_ACTION_DONE)
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_NEXT) {
                getMovies(binding, movieListView)
                return@OnEditorActionListener true
            }
            false
        })

        val searchBtn = binding.searchButton

        searchBtn.setOnClickListener {
            getMovies(binding, movieListView)
        }
    }

    private fun getMovies(binding: ActivityMainBinding, movieListView: RecyclerView) {
        if(binding.search.toString().isEmpty()) {
            getPopularMovies(movieListView)
        } else {
            searchMovies(movieListView, binding.search.toString())
        }

        this.hideKeyboard()
    }

    private fun getPopularMovies(movieListView: RecyclerView) {
        val movieResponse = ApiHelper.getPopularMovies()
        if(movieResponse != null) {
            movies.clear()
            movies.addAll(movieResponse.results)
            movieListView.adapter!!.notifyDataSetChanged()
        }

    }

    private fun searchMovies(movieListView: RecyclerView, search: String) {
        val movieResponse = ApiHelper.searchMovie(search)
        if(movieResponse != null) {
            movies.clear()
            movies.addAll(movieResponse.results)
            movieListView.adapter!!.notifyDataSetChanged()
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(this.currentFocus?.windowToken, 0)
    }


}