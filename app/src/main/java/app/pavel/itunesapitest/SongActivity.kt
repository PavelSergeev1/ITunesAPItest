package app.pavel.itunesapitest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.ArrayList

class SongActivity : AppCompatActivity() {

    companion object {
        var songArrayList = ArrayList<Song>()
    }

    private var listViewAdapter: SongListViewAdapter? = null

    private var progressBar: ProgressBar? = null
    private var albumTitle: TextView? = null
    private var artistName: TextView? = null
    private var primaryGenreName: TextView? = null
    private var priceData: TextView? = null
    private var currencyData: TextView? = null
    private var releaseDateTextView: TextView? = null
    private var copyrightData: TextView? = null
    private var list: ListView? = null

    private var collectionPrice: String? = null
    private var copyright: String? = null
    private var country: String? = null
    private var currency: String? = null
    private var releaseDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        listViewAdapter = SongListViewAdapter(this)

        list = this.findViewById(R.id.songContainer)
        list!!.adapter = listViewAdapter

        albumTitle = findViewById(R.id.albumTitle)
        albumTitle!!.text = intent.getStringExtra(Constants.collectionName)

        artistName = findViewById(R.id.artistName)
        artistName!!.text = intent.getStringExtra(Constants.artistName)

        primaryGenreName = findViewById(R.id.primaryGenreName)
        primaryGenreName!!.text = intent.getStringExtra(Constants.primaryGenreName)

        priceData = findViewById(R.id.priceData)
        currencyData = findViewById(R.id.currencyData)
        releaseDateTextView = findViewById(R.id.releaseDate)
        copyrightData = findViewById(R.id.copyrightData)

        progressBar = findViewById(R.id.progressBarAlbum)

        true.setProgressBarVisibilityFun()

        val collectionId  = intent.getStringExtra("collectionId")

        searchQuery(collectionId!!)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        } else -> {
            super.onOptionsItemSelected(item)
        }
    }

    private fun searchQuery(searchQuery: String) {
        var query = searchQuery.replace(Constants.spaceChar, Constants.plusChar)
        query = Constants.albumSongQueryURLPart1 + query + Constants.albumSongQueryURLPart2

        val client = OkHttpClient()
        val request = Request.Builder().url(query).build()

        client.newCall(request).enqueue(object : Callback {

            override fun onFailure(call: Call, e: IOException) {

                runOnUiThread {
                    false.setProgressBarVisibilityFun()

                    Toast.makeText(
                        this@SongActivity,
                        Constants.failToLoadText,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val strResponse = response.body!!.string()
                    val jsonObject: JSONObject = JSONObject(strResponse)
                    val jsonArray: JSONArray = jsonObject.getJSONArray(Constants.results)

                    val size: Int = jsonArray.length()

                    /**
                     * jsonArray[0] contains information about album
                     */
                    val albumInfo: JSONObject = jsonArray[0] as JSONObject
                    collectionPrice = albumInfo.getString(Constants.collectionPrice)
                    copyright = albumInfo.getString(Constants.copyright)
                    country = albumInfo.getString(Constants.country)
                    currency = albumInfo.getString(Constants.currency)
                    releaseDate = albumInfo.getString(Constants.releaseDate)

                    /**
                     * jsonArray[1..size] contains information about album tracks
                     */
                    for (i in 1 until size) {

                        val songInfo: JSONObject = jsonArray[i] as JSONObject

                        val song: Song = Song()

                        song.trackName = songInfo.getString(Constants.trackName)
                        song.trackExplicitness = songInfo.getString(Constants.trackExplicitness)
                        song.trackNumber = songInfo.getString(Constants.trackNumber)
                        song.trackTimeMillis = songInfo.getString(Constants.trackTimeMillis)

                        songArrayList.add(song)
                    }

                    runOnUiThread {
                        false.setProgressBarVisibilityFun()

                        val listViewAdapter: SongListViewAdapter = SongListViewAdapter(applicationContext)
                        list?.adapter = listViewAdapter

                        listViewAdapter.notifyDataSetChanged()

                        priceData!!.text = collectionPrice
                        currencyData!!.text = currency

                        val date = releaseDate?.substring(0, 10)
                            ?.replace(Constants.minusChar, Constants.dotChar)
                        releaseDateTextView!!.text = date

                        copyrightData!!.text = copyright
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
    }

    private fun Boolean.setProgressBarVisibilityFun() {
        when(this) {
            true -> progressBar!!.visibility = View.VISIBLE
            false -> progressBar!!.visibility = View.GONE
        }
    }

    override fun onStop() {
        super.onStop()

        songArrayList.clear()

        val listViewAdapter: SongListViewAdapter = SongListViewAdapter(applicationContext)
        list?.adapter = listViewAdapter

        listViewAdapter.notifyDataSetChanged()
    }
}