package com.example.flixster

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class MovieRecyclerViewAdapter (
    private val movies: List<Movie>
        ) : RecyclerView.Adapter<MovieRecyclerViewAdapter.MovieViewHolder>()
{
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_movie, parent, false)
        return MovieViewHolder(view)
    }

    inner class MovieViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
        var mItem: Movie? = null
        val mTitle: TextView = mView.findViewById<View>(R.id.movie_title) as TextView
        val mOverview: TextView = mView.findViewById<View>(R.id.movie_overview) as TextView
        val mImage: ImageView = mView.findViewById<View>(R.id.movie_image) as ImageView

        override fun toString(): String {
            return mTitle.toString() + " '" + mOverview.text + "'"
        }
    }

    override fun onBindViewHolder(holder: MovieRecyclerViewAdapter.MovieViewHolder, position: Int) {
        val movie = movies[position]

        holder.mItem = movie
        holder.mTitle.text = movie.title
        holder.mOverview.text = movie.overview

        Glide.with(holder.mView)
            .load("https://image.tmdb.org/t/p/w500/${movie.imageUrl}")
            .placeholder(R.drawable.my_project__1_)
            .error(R.drawable.my_project__1_)
            .centerInside()
            .into(holder.mImage)
    }

    override fun getItemCount(): Int {
       return movies.size
    }
}