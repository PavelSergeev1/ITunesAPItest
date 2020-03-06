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

/**
 * Launch Activity for album search
 */

class MainActivity : AppCompatActivity(),
    SearchView.OnQueryTextListener,
    AdapterView.OnItemClickListener {

    companion object {
        var albumArrayList = ArrayList<Album>()
    }

    private var list: ListView? = null
    private var adapterAlbum: AlbumListViewAdapter? = null
    private var editSearch: SearchView? = null
    private var resultCountTextView: TextView? = null
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        albumArrayList = ArrayList()

        adapterAlbum = AlbumListViewAdapter(this)

        list = this.findViewById(R.id.listView)
        list!!.adapter = adapterAlbum
        list!!.onItemClickListener = this

        resultCountTextView = findViewById(R.id.resultCount)
        showResultsCount(albumArrayList.size.toString())

        progressBar = findViewById(R.id.progressBar)
        progressBar!!.visibility = View.GONE

        editSearch = findViewById(R.id.searchView)
        editSearch!!.setOnQueryTextListener(this)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val intent = Intent(this, SongActivity::class.java)
        intent.putExtra(Constants.collectionId, albumArrayList[position].collectionId)
        intent.putExtra(Constants.collectionName, albumArrayList[position].albumTitle)
        intent.putExtra(Constants.artistName, albumArrayList[position].artistName)
        intent.putExtra(Constants.primaryGenreName, albumArrayList[position].genre)
        startActivity(intent)
    }

    override fun onQueryTextSubmit(query: String): Boolean {
        adapterAlbum!!.filter(query)

        searchQuery(query)

        true.setProgressBarVisibilityFun()

        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        adapterAlbum!!.filter(newText)

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
        var query = searchQuery.replace(Constants.spaceChar, Constants.plusChar)
        query = Constants.albumQueryURLPart1 + query + Constants.albumQueryURLPart2

        var resultsCount = "0"

        val client = OkHttpClient()
        val request = Request.Builder().url(query).build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {

                Log.d("LOG", "Fail to load")

                runOnUiThread {
                    false.setProgressBarVisibilityFun()

                    Toast.makeText(this@MainActivity,
                        Constants.failToLoadText,
                        Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val strResponse = response.body!!.string()
                    val jsonObject = JSONObject(strResponse)
                    val jsonArray: JSONArray = jsonObject.getJSONArray(Constants.results)

                    resultsCount = jsonObject.getString(Constants.resultCount)

                    val size: Int = jsonArray.length()

                    for (i in 0 until size) {

                        val albumInfo: JSONObject = jsonArray[i] as JSONObject

                        val album = Album()

                        album.albumTitle = albumInfo.getString(Constants.collectionName)
                        album.artistName = albumInfo.getString(Constants.artistName)
                        album.date = albumInfo.getString(Constants.releaseDate).substring(0, 4)
                        album.genre = albumInfo.getString(Constants.primaryGenreName)
                        album.artwork = albumInfo.getString(Constants.artworkUrl100)
                        album.collectionId = albumInfo.getString(Constants.collectionId)

                        albumArrayList.add(album)
                    }

                    sortAlbumListAlphabetically()

                    runOnUiThread {
                        showResultsCount(resultsCount)

                        false.setProgressBarVisibilityFun()

                        val adapterAlbum = AlbumListViewAdapter(applicationContext)
                        list?.adapter = adapterAlbum

                        adapterAlbum.notifyDataSetChanged()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }
        })

    }

    private fun sortAlbumListAlphabetically() {
        albumArrayList.sortBy {
            it.albumTitle
        }
    }

}