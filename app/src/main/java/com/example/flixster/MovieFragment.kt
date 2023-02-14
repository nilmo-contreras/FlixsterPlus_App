package com.example.flixster

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.widget.ContentLoadingProgressBar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONObject

// --------------------------------//
//             API KEY             //
// --------------------------------//
private const val API_KEY = "a07e22bc18f5cb106bfe4cc1f83ad8ed"

class MovieFragment : Fragment() {
    override fun onCreateView (
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_movie_list, container, false)
        val progressBar = view.findViewById<View>(R.id.progress) as ContentLoadingProgressBar
        val recyclerView = view.findViewById<View>(R.id.list) as RecyclerView
        val context = view.context
        recyclerView.layoutManager = GridLayoutManager(context, 1)
        updateAdapter(progressBar, recyclerView)
        return view
    }

    private fun updateAdapter (
        progressBar: ContentLoadingProgressBar,
        recyclerView: RecyclerView
    ) {
        progressBar.show()

        val client = AsyncHttpClient()
        val params = RequestParams()
        params["api_key"] = API_KEY

        client[
                "https://api.themoviedb.org/3/movie/now_playing",
                params,
                object : JsonHttpResponseHandler()
                {
                    override fun onSuccess(
                        statusCode: Int,
                        headers: Headers,
                        json: JsonHttpResponseHandler.JSON
                    ) {
                        // The wait for a response is over
                        progressBar.hide()

                        val resultsJSON = json.jsonObject.getJSONArray("results").toString()
                        val gson = Gson()
                        val arrayBookType = object : TypeToken<List<Movie>>() {}.type
                        val models : List<Movie> = gson.fromJson(resultsJSON, arrayBookType)
                        recyclerView.adapter = MovieRecyclerViewAdapter(models)

                        // Look for this in Logcat:
                        Log.d("raw JSON", resultsJSON.toString())
                        Log.d("MovieFragment", "response successful")
                    }

                    /*
                     * The onFailure function gets called when
                     * HTTP response status is "4XX" (eg. 401, 403, 404)
                     */
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        errorResponse: String,
                        t: Throwable?
                    ) {
                        // The wait for a response is over
                        progressBar.hide()

                        // If the error is not null, log it!
                        t?.message?.let {
                            Log.e("MovieFragment", errorResponse)
                        }
                    }
                }
        ]
    }
}