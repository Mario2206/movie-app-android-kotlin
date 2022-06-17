package com.example.moviesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.ScrollView
import androidx.databinding.DataBindingUtil
import coil.load
import com.bellano.themoviedblibrary.network.ApiHelper
import com.example.moviesapp.databinding.ActivityMovieBinding

class MovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setup binding
        val binding = DataBindingUtil.setContentView<ActivityMovieBinding>(this, R.layout.activity_movie)

        // Fetch movie
        val movieId = intent.getStringExtra("MOVIE_ID").toString()
        getMovie(movieId, binding)

        setupListeners(binding)
    }

    private fun setupListeners(binding: ActivityMovieBinding) {
        val shareBtn = binding.shareButton

        shareBtn.setOnClickListener { shareMovie(binding) }
    }

    private fun getMovie(movieId: String, binding: ActivityMovieBinding) {
        val movieResult = ApiHelper.getMovie(movieId)
        binding.movie = movieResult
        val imageView = binding.thumbnail
        imageView.load(ApiHelper.getImageBaseUrl().plus(binding.movie?.poster_path))
    }

    private fun shareMovie(binding: ActivityMovieBinding) {
        val intent = Intent(Intent.ACTION_SEND)
        val content = """
            ${ApiHelper.getImageBaseUrl().plus(binding.movie?.poster_path)}
            
            ${binding.movie?.title} : ${binding.movie?.vote_average}
            
            Description: ${binding.movie?.overview}
        """.trimIndent()
        intent.putExtra(Intent.EXTRA_TEXT, content)
        intent.type = "text/plain"
        startActivity(Intent.createChooser(intent,"Share To:"))
    }
}