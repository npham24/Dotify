package com.example.dotify.activity

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ericchee.songdataprovider.Song
import com.ericchee.songdataprovider.SongDataProvider
import com.example.dotify.fragment.NowPlayingFragment
import com.example.dotify.fragment.SongListFragment
import com.example.dotify.model.OnSongClickListener
import com.example.dotify.R
import kotlinx.android.synthetic.main.activity_main_container.*


class MainActivityFrag: AppCompatActivity(), OnSongClickListener {
    private val listOfSongs = SongDataProvider.getAllSongs()
    private var selectedSong: Song? = null

    companion object {
        const val CURR_SONG_KEY = "CURR_SONG_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_container)
        Log.i("lol", "lol")

        if (savedInstanceState != null) {
            savedInstanceState.getParcelable<Song>(CURR_SONG_KEY)?.let { song ->
                selectedSong = song
                showInfoOnMiniPlayer()
            }
        } else {
            showSongListFrag()
        }

        clMiniPlayer.setOnClickListener {
            selectedSong?.let {song ->
                if (!hasNowPlayingFragment()) {
                    showNowPlayingFrag(song)
                }
            }
        }

        btnShuffleList.setOnClickListener {
            val songListFragment = supportFragmentManager.findFragmentByTag(SongListFragment.TAG) as SongListFragment
            songListFragment.shuffleList()
        }

        if (!hasNowPlayingFragment()) {
            clMiniPlayer.visibility = View.GONE
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        supportFragmentManager.addOnBackStackChangedListener {
            val hasBackEntries = supportFragmentManager.backStackEntryCount > 0
            supportActionBar?.setDisplayHomeAsUpEnabled(hasBackEntries)

            if (hasBackEntries) {
                clMiniPlayer.visibility = View.GONE
            } else {
                clMiniPlayer.visibility = View.VISIBLE
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        clMiniPlayer.visibility = View.VISIBLE
        supportFragmentManager.popBackStack()
        return super.onSupportNavigateUp()
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState?.run {
            putParcelable(CURR_SONG_KEY, selectedSong)
        }
    }

    private fun showSongListFrag() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.flFragmentContainer, SongListFragment.newInstance(listOfSongs), SongListFragment.TAG)
            .commit()
    }

    private fun showNowPlayingFrag(song: Song) {
        clMiniPlayer.visibility = View.GONE
        supportFragmentManager.beginTransaction()
            .add(R.id.flFragmentContainer, NowPlayingFragment.newInstance(song), NowPlayingFragment.TAG)
            .addToBackStack(NowPlayingFragment.TAG)
            .commit()
    }

    private fun showInfoOnMiniPlayer() {
        selectedSong?.let { song ->
            val name = song.title
            val artist = song.artist
            tvSongInfo.visibility = View.VISIBLE
            tvSongInfo.text = "$name - $artist"
            Log.i("lol", tvSongInfo.text.toString())
        }
    }

    private fun hasNowPlayingFragment(): Boolean {
        return supportFragmentManager.findFragmentByTag(NowPlayingFragment.TAG) != null
    }

    override fun onSongClicked(song: Song) {
        selectedSong = song
        showInfoOnMiniPlayer()
    }
}