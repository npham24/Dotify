package com.example.dotify

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ericchee.songdataprovider.Song

class SongListAdapter(private var listOfSongs: List<Song>): RecyclerView.Adapter<SongListAdapter.SongViewHolder>() {
    var onSongClickListener: ((song: Song) -> Unit)? = null

    inner class SongViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val tvSongTitle by lazy { itemView.findViewById<TextView>(R.id.tvSongTitle) }
        private val tvSongArtist by lazy { itemView.findViewById<TextView>(R.id.tvSongArtist) }
        private val ivAlbumCover by lazy { itemView.findViewById<ImageView>(R.id.ivAlbumCover) }
        fun bind(song: Song) {
            tvSongTitle.text = song.title
            tvSongArtist.text = song.artist
            ivAlbumCover.setImageResource(song.smallImageID)
            ivAlbumCover.contentDescription = "Album cover for the song ${song.title}"

            itemView.setOnClickListener{
                onSongClickListener?.invoke(song)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.song_list_item, parent, false)
        return SongViewHolder(view)
    }

    override fun getItemCount(): Int = listOfSongs.size

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val song = listOfSongs[position]
        holder.bind(song)
    }

    fun change(newListOfSongs: List<Song>) {
        listOfSongs = newListOfSongs
        notifyDataSetChanged()
    }
}