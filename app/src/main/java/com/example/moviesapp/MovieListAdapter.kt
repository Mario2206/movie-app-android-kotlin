package com.example.moviesapp

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bellano.themoviedblibrary.network.ApiHelper
import com.bellano.themoviedblibrary.network.models.Movie
import java.net.URI

class MovieListAdapter(val data: List<Movie>, private val onClickListener: OnClickListener) : RecyclerView.Adapter<MovieListAdapter.MyViewHolder>() {
    class MyViewHolder(row: View) : RecyclerView.ViewHolder(row) {
        val titleView = row.findViewById<TextView>(R.id.movieTitle)
        val descriptionView = row.findViewById<TextView>(R.id.movieDescription)
        val thumbnailView = row.findViewById<ImageView>(R.id.movieThumbnail)
        val voteAvgView = row.findViewById<TextView>(R.id.movieVoteAvg)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(R.layout.movie_item_view,
            parent, false)

        return MyViewHolder(layout)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = data.get(position)
        holder.titleView.text = item.title
        holder.descriptionView.text = if (item.overview != null && item.overview!!.length > 100 )  item.overview!!.slice(IntRange(0, 100)).plus("...") else item.overview
        holder.voteAvgView.text = item.vote_average.toString()
        holder.thumbnailView.load(ApiHelper.getImageBaseUrl().plus(item.poster_path))

        holder.itemView.setOnClickListener {
            onClickListener.onClick(item)
        }
    }
    override fun getItemCount(): Int = data.size


    class OnClickListener(val clickListener: (movie: Movie) -> Unit) {
        fun onClick(movie: Movie) = clickListener(movie)
    }

}