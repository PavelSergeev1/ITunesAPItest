package app.pavel.itunesapitest

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import java.util.Locale
import java.util.ArrayList

/**
 * ListView Adapter for list of albums for search query
 */

class AlbumListViewAdapter(context: Context) : BaseAdapter() {

    private var inflater: LayoutInflater = LayoutInflater.from(context)
    private var arrayList: ArrayList<Album> = ArrayList()

    init {
        this.arrayList.addAll(MainActivity.albumArrayList)
    }

    inner class ViewHolder {
        internal var title: TextView? = null
        internal var artist: TextView? = null
        internal var year: TextView? = null
        internal var genre: TextView? = null
        internal lateinit var artwork: ImageView
    }

    override fun getView(position: Int, viewParameter: View?, parent: ViewGroup): View {
        var view = viewParameter
        val holder: ViewHolder
        if (view == null) {
            holder = ViewHolder()
            view = inflater.inflate(R.layout.item, null)

            holder.title = view.findViewById(R.id.title)
            holder.artist = view.findViewById(R.id.artist)
            holder.year = view.findViewById(R.id.year)
            holder.genre = view.findViewById(R.id.genre)
            holder.artwork = view.findViewById(R.id.artwork)

            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        // Set the results into TextViews
        holder.title!!.text = MainActivity.albumArrayList[position].albumTitle
        holder.artist!!.text = MainActivity.albumArrayList[position].artistName
        holder.year!!.text = MainActivity.albumArrayList[position].date
        holder.genre!!.text = MainActivity.albumArrayList[position].genre

        // Set the album artwork into ImageView
        Glide
            .with(view!!)
            .load(MainActivity.albumArrayList[position].artwork)
            .into(holder.artwork)

        if (!arrayList.contains(MainActivity.albumArrayList[position]))
            arrayList.add(MainActivity.albumArrayList[position])

        return view
    }

    override fun getItem(position: Int): Any {
        return MainActivity.albumArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return MainActivity.albumArrayList.size
    }

    fun filter(text: String) {
        var text = text
        text = text.toLowerCase(Locale.getDefault())
        MainActivity.albumArrayList.clear()
        if (text.isEmpty())
            MainActivity.albumArrayList.addAll(arrayList)
        else {
            for (i in arrayList) {
                if (i.albumTitle.toLowerCase(Locale.getDefault())
                        .contains(text)) {
                    Log.d("LOG", i.toString())
                    MainActivity.albumArrayList.add(i)
                }
            }
        }

        notifyDataSetChanged()
    }
}