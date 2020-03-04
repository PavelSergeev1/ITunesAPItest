package app.pavel.itunesapitest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem

class AlbumActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_album)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        android.R.id.home -> {
            finish()
            true
        } else -> {
            super.onOptionsItemSelected(item)
        }
    }

}
