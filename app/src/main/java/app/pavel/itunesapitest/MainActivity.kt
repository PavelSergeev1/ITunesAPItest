package app.pavel.itunesapitest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {

    companion object {
        var albumArrayList = ArrayList<Album>()
    }

    private var list: ListView? = null
    private var adapter: ListViewAdapter? = null
    private var editSearch: SearchView? = null
    private var movieList: Array<Album>? = null

    private var client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        movieList = arrayOf(
            Album(albumTitle = "Album title 1",
                artistName = "Artist 1",
                artwork = "",
                genre = "Rock",
                date = "1999-01-10"),
            Album(albumTitle = "Album title 2",
                artistName = "Artist 2",
                artwork = "",
                genre = "Hip Hop",
                date = "1999-01-10")
        )

        list = findViewById(R.id.listView)

        albumArrayList = ArrayList()

        for (i in movieList!!.indices) {
            val albumTitle = movieList!![i]
            //movieList!![i].getAlbumTitle()
            albumArrayList.add(albumTitle)
        }

        adapter = ListViewAdapter(this, albumArrayList)

        list!!.adapter = adapter

        editSearch = findViewById(R.id.searchView)
        editSearch!!.setOnQueryTextListener(this)

        list!!.onItemClickListener = AdapterView.OnItemClickListener {
            parent, view, position, id ->
                Toast.makeText(
                        this@MainActivity,
                        albumArrayList[position].albumTitle,
                        Toast.LENGTH_SHORT).show()
        }
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        // adapter!!.filter(query)
        Log.d("LOG Submit", query)

        searchQuery(query)

        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        adapter!!.filter(newText)
        // Log.d("LOG", newText)
        return false
    }

    private fun searchQuery(query: String) {
        var query = query.replace(" ", "+")
        query = "https://itunes.apple.com/search?term=" +
                query + "&entity=album&attribute=albumTerm"
        Log.d("LOG query", query)

        val request = Request.Builder().url(query).build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("LOG", "Fail to load")
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    var strResponse = response.body!!.string()
                    val jsonObject: JSONObject = JSONObject(strResponse)
                    var jsonArray: JSONArray = jsonObject.getJSONArray("results")

                    var resultCount = jsonObject.getString("resultCount")

                    //showResultsCount(resultCount)
                    // Log.d("LOG", resultCount)

                    var i: Int = 0
                    var size: Int = jsonArray.length()

                    for (i in 0..size - 1) {
                        // Log.d("LOG", jsonArray[i].toString())

                        val albumInfo: JSONObject = jsonArray[i] as JSONObject

                        var album: Album = Album()

                        album.albumTitle = albumInfo.getString("collectionName")
                        album.artistName = albumInfo.getString("artistName")
                        album.date = albumInfo.getString("releaseDate")
                        album.genre = albumInfo.getString("primaryGenreName")
                        album.artwork = albumInfo.getString("artworkUrl100")

                        albumArrayList.add(album)
                        // Log.d("LOG", album.albumTitle)
                    }

                    runOnUiThread {
                        val adapter: ListViewAdapter
                        adapter = ListViewAdapter(applicationContext, albumArrayList)
                        list?.adapter = adapter
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

        })
    }

    fun showResultsCount(count: String) {
        Toast.makeText(this@MainActivity,
            "Results founded: $count",
            Toast.LENGTH_SHORT).show()
    }
}