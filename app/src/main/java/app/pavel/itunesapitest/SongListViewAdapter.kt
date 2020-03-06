package app.pavel.itunesapitest

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import java.util.ArrayList

class SongListViewAdapter(context: Context) : BaseAdapter() {

    private var inflater: LayoutInflater = LayoutInflater.from(context)
    private var arrayList: ArrayList<Song> = ArrayList()

    init {
        this.arrayList.addAll(SongActivity.songArrayList)
    }

    inner class ViewHolder {
        internal var trackNumber: TextView? = null
        internal var trackName: TextView? = null
        internal var trackExplicitness: TextView? = null
        internal var trackTimeMillis: TextView? = null
    }

    override fun getView(position: Int, viewParameter: View?, parent: ViewGroup): View? {
        var view = viewParameter
        val holder: ViewHolder
        if (view == null) {
            holder = ViewHolder()
            view = inflater.inflate(R.layout.item_song, null)

            holder.trackNumber = view.findViewById(R.id.trackNumber)
            holder.trackName = view.findViewById(R.id.trackName)
            holder.trackExplicitness = view.findViewById(R.id.trackExplicitness)
            holder.trackTimeMillis = view.findViewById(R.id.trackTimeMillis)

            view.tag = holder
        } else {
            holder = view.tag as ViewHolder
        }

        // Set the results into TextViews
        holder.trackNumber!!.text = SongActivity.songArrayList[position].trackNumber
        holder.trackName!!.text = SongActivity.songArrayList[position].trackName

        // If song is marked as "explicit" add "E" after song name
        if (SongActivity.songArrayList[position].trackExplicitness
            == Constants.explicit) {
            holder.trackExplicitness!!.text = Constants.letterE
            holder.trackExplicitness!!.setBackgroundColor(Color.GRAY)
            holder.trackExplicitness!!.setTextColor(Color.WHITE)
        }

        // convert trackTimeMills to normal format
        val mills = SongActivity.songArrayList[position].trackTimeMillis.toInt()
        var seconds = ((mills / 1000) % 60).toString()
        if (seconds.toInt() < 10)
            seconds = Constants.zero + seconds
        val minutes = (mills / (1000*60)) % 60
        val duration = "$minutes:$seconds"
        holder.trackTimeMillis!!.text = duration

        return view
    }

    override fun getItem(position: Int): Any {
        return SongActivity.songArrayList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return SongActivity.songArrayList.size
    }
}