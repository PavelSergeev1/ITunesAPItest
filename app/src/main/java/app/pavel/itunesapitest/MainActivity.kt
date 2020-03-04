package app.pavel.itunesapitest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.widget.SearchView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(),
    SearchView.OnQueryTextListener,
    AdapterView.OnItemClickListener {

    companion object {
        var albumArrayList = ArrayList<Album>()
    }

    private var list: ListView? = null
    private var adapter: ListViewAdapter? = null
    private var editSearch: SearchView? = null
    private var resultCountTextView: TextView? = null
    private var progressBar: ProgressBar? = null

    private var client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        albumArrayList = ArrayList()

        adapter = ListViewAdapter(this)

        resultCountTextView = findViewById(R.id.resultCount)
        showResultsCount(albumArrayList.size.toString())

        list = this.findViewById(R.id.listView)
        list!!.adapter = adapter
        list!!.onItemClickListener = this

        progressBar = findViewById(R.id.progressBar)
        progressBar!!.visibility = View.GONE

        editSearch = findViewById(R.id.searchView)
        editSearch!!.setOnQueryTextListener(this)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Toast.makeText(
            this@MainActivity,
            albumArrayList[position].albumTitle,
            Toast.LENGTH_SHORT).show()

        val intent = Intent(this, AlbumActivity::class.java)
        startActivity(intent)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        adapter!!.filter(query)

        Log.d("LOG Submit", query)

        searchQuery(query)

        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        adapter!!.filter(newText)

        showResultsCount(albumArrayList.size.toString())

        return false
    }

    //////////////////////////
    //////////////////////////

    private fun Boolean.setProgressBarVisibilityFun() {
        when(this) {
            true -> progressBar!!.visibility = View.VISIBLE
            false -> progressBar!!.visibility = View.GONE
        }
    }

    private fun showResultsCount(count: String) {
        val text = Constants.resultCountText + count
        resultCountTextView!!.text = text
    }

    private fun searchQuery(searchQuery: String) {
        // setProgressBarVisibilityFun(true)
        // progressBar!!.visibility = View.VISIBLE

        var query = searchQuery.replace(Constants.spaceChar, Constants.plusChar)

        query = Constants.albumQueryURLPart1 + query + Constants.albumQueryURLPart2

        // Log.d("LOG query", query)

        val request = Request.Builder().url(query).build()

        var resultsCount = "0"

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d("LOG", "Fail to load")
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val strResponse = response.body!!.string()
                    val jsonObject: JSONObject = JSONObject(strResponse)
                    val jsonArray: JSONArray = jsonObject.getJSONArray(Constants.results)

                    resultsCount = jsonObject.getString(Constants.resultCount)

                    val size: Int = jsonArray.length()

                    for (i in 0 until size) {

                        val albumInfo: JSONObject = jsonArray[i] as JSONObject

                        val album: Album = Album()

                        album.albumTitle = albumInfo.getString(Constants.collectionName)
                        album.artistName = albumInfo.getString(Constants.artistName)
                        album.date = albumInfo.getString(Constants.releaseDate).substring(0, 4)
                        album.genre = albumInfo.getString(Constants.primaryGenreName)
                        album.artwork = albumInfo.getString(Constants.artworkUrl100)

                        albumArrayList.add(album)

                        //Log.d("LOG", "collection Id: " + albumInfo.getString("collectionId"))
                    }

                    sortAlbumListAlphabetically()

                    runOnUiThread {
                        showResultsCount(resultsCount)

                        val adapter: ListViewAdapter
                        adapter = ListViewAdapter(applicationContext)
                        list?.adapter = adapter

                        adapter.notifyDataSetChanged()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })

        //setProgressBarVisibilityFun(false)
        //progressBar!!.visibility = View.GONE
    }

    private fun sortAlbumListAlphabetically() {
        albumArrayList.sortBy {
            it.albumTitle
        }
    }

}