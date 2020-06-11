package com.example.dotify.activity

import android.os.Bundle
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
    private var listOfSongs = SongDataProvider.getAllSongs()
    private var savedSong: Song? = null

    companion object {
        const val SAVED_SONG_KEY = "SAVED_SONG_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_container)

        val hasSongListFragment: Boolean = supportFragmentManager.findFragmentByTag(SongListFragment.TAG) != null
        val hasNowPlayingFragment: Boolean = supportFragmentManager.findFragmentByTag(NowPlayingFragment.TAG) != null

        if (savedInstanceState != null) {
            savedSong = savedInstanceState.getParcelable(SAVED_SONG_KEY)
            showInfoOnMiniPlayer()
        }


        if (!hasSongListFragment && !hasNowPlayingFragment) {
            val songListFragment = SongListFragment.getInstance(listOfSongs)

            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, songListFragment, SongListFragment.TAG)
                .addToBackStack(SongListFragment.TAG)
                .commit()
        } else if (hasSongListFragment && !hasNowPlayingFragment) {
            val songListFragment = SongListFragment.getInstance(listOfSongs)

            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainer, songListFragment, SongListFragment.TAG)
                .commit()
        } else if (hasNowPlayingFragment) {
            clMiniPlayer.visibility = View.GONE
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        supportFragmentManager.addOnBackStackChangedListener {
            val hasBackStack = (supportFragmentManager.backStackEntryCount > 1)
            if (hasBackStack) {
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            } else {
                supportActionBar?.setDisplayHomeAsUpEnabled(false)
            }
        }

        btnShuffleList.setOnClickListener {
            val songListFragment = supportFragmentManager.findFragmentByTag(SongListFragment.TAG) as SongListFragment
            songListFragment.shuffleMusicList()
        }

        tvSongInfo.setOnClickListener {
            showMusicPlayerFragment()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        clMiniPlayer.visibility = View.VISIBLE
        supportFragmentManager.popBackStack()
        return super.onSupportNavigateUp()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run {
            putParcelable(SAVED_SONG_KEY, savedSong)
        }
        super.onSaveInstanceState(outState)
    }

    private fun showMusicPlayerFragment() {
        clMiniPlayer.visibility = View.GONE
        savedSong?.let { song ->
            val nowPlayingFragment = NowPlayingFragment.getInstance(song)

            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, nowPlayingFragment, NowPlayingFragment.TAG)
                .addToBackStack(NowPlayingFragment.TAG)
                .commit()
        }
    }

    private fun showInfoOnMiniPlayer() {
        savedSong?.let { song ->
            tvSongInfo.visibility = View.VISIBLE
            tvSongInfo.text = "%s - %s".format(song.title, song.artist)
        }
    }

    override fun onSongClicked(song: Song) {
        savedSong = song
        showInfoOnMiniPlayer()
    }
}