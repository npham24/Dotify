package com.example.dotify

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.ericchee.songdataprovider.Song
import com.ericchee.songdataprovider.SongDataProvider
import com.example.dotify.MainActivity.Companion.ALBUM_COVER
import com.example.dotify.MainActivity.Companion.ARTIST_NAME
import com.example.dotify.MainActivity.Companion.SONG_NAME
import com.example.dotify.model.SongListAdapter
import kotlinx.android.synthetic.main.activity_song_list.*

class SongListActivity : AppCompatActivity() {
    private val listOfSongs = SongDataProvider.getAllSongs()
    private val songListAdapter =
        SongListAdapter(listOfSongs)
    private var selectedSong: Song? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song_list)

        rvSongList.adapter = songListAdapter

        songListAdapter.onSongClickListener = { song ->
            selectedSong = song
            showInfoOnMiniPlayer()
        }

        btnShuffleList.setOnClickListener {
            shuffle()
        }

        tvSongInfo.setOnClickListener {
            selectedSong?.let { song ->
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra(SONG_NAME, song.title)
                intent.putExtra(ARTIST_NAME, song.artist)
                intent.putExtra(ALBUM_COVER, song.largeImageID)
                startActivity(intent)
            }
        }
    }

    private fun showInfoOnMiniPlayer() {
        selectedSong?.let { song ->
            val name = song.title
            val artist = song.artist
            tvSongInfo.visibility = View.VISIBLE
            tvSongInfo.text = "$name - $artist"
        }
    }

    private fun shuffle() {
        tvSongInfo.visibility = View.INVISIBLE
        val shuffledMusicList = listOfSongs.toMutableList().apply { shuffle() }
        songListAdapter.change(shuffledMusicList)
    }
}
