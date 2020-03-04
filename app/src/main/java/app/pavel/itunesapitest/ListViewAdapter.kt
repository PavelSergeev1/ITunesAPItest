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

class ListViewAdapter(context: Context, arrayList: ArrayList<Album>) : BaseAdapter() {

    internal var context: Context
    internal var inflater: LayoutInflater
    private var arrayList: ArrayList<Album>

    init {
        this.context = context
        inflater = LayoutInflater.from(context)
        this.arrayList = ArrayList()
        this.arrayList.addAll(MainActivity.albumArrayList)
    }

    inner class ViewHolder {
        internal var title: TextView? = null
        internal var artist: TextView? = null
        internal var year: TextView? = null
        internal var genre: TextView? = null
        internal lateinit var artwork: ImageView
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var view = view
        val holder: ViewHolder
        if (view == null) {
            holder = ViewHolder()
            view = inflater.inflate(R.layout.item, null)

            holder.title = view!!.findViewById(R.id.title)
            holder.artist = view!!.findViewById(R.id.artist)
            holder.year = view!!.findViewById(R.id.year)
            holder.genre = view!!.findViewById(R.id.genre)
            holder.artwork = view!!.findViewById(R.id.artwork)

            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        // Set the results into TextView
        holder.title!!.setText(MainActivity.albumArrayList[position].albumTitle)
        holder.artist!!.setText(MainActivity.albumArrayList[position].artistName)
        holder.year!!.setText(MainActivity.albumArrayList[position].date)
        holder.genre!!.setText(MainActivity.albumArrayList[position].genre)

        Glide
            .with(view)
            .load(MainActivity.albumArrayList[position].artwork)
            .into(holder.artwork)

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
        if (text.length == 0)
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