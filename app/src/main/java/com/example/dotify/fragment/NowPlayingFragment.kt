package com.example.dotify.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ericchee.songdataprovider.Song
import com.example.dotify.R
import kotlinx.android.synthetic.main.activity_song_info.*
import kotlin.random.Random

class NowPlayingFragment: Fragment() {
    private lateinit var clickedSong: Song
    private var playCounter: Int = 0
    private lateinit var userName: String

    companion object {
        const val NOW_PLAYING_KEY: String = "NOW_PLAYING_KEY"
        const val USERNAME_KEY = "USERNAME_KEY"
        const val PLAY_COUNTER_KEY = "PLAY_COUNTER_KEY"

        val TAG: String = NowPlayingFragment::class.java.simpleName

        fun getInstance(song: Song): NowPlayingFragment {
            return NowPlayingFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(NOW_PLAYING_KEY, song)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            playCounter = savedInstanceState.getInt(PLAY_COUNTER_KEY)
            savedInstanceState.getString(USERNAME_KEY)?.let { name ->
                userName = name
            }
        } else {
            userName = getString(R.string.user_name)
            playCounter = Random.nextInt(1, 1000000)
        }

        arguments?.let { args ->
            with (args) {
                getParcelable<Song>(NOW_PLAYING_KEY)?.let { song ->
                    clickedSong = song
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.activity_song_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateUsername()
        populateViewCount()
        populatePlayer()

        btnChangeUser.setOnClickListener {
                v: View -> fnChangeUser(v)
        }
        btnPrev.setOnClickListener {
                v: View -> fnPrevSong(v)
        }
        btnPlay.setOnClickListener {
                v: View -> fnPlaySong(v)
        }
        btnNext.setOnClickListener {
                v: View -> fnNextSong(v)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState?.run {
            putInt(PLAY_COUNTER_KEY, playCounter)
            putString(USERNAME_KEY, userName)
        }
        super.onSaveInstanceState(outState)
    }

    private fun populatePlayer() {
        ivAlbumCover.setImageResource(clickedSong.largeImageID)
        tvSongTitle.text = clickedSong.title
        tvSongArtist.text = clickedSong.artist
    }

    private fun fnPlaySong(view: View) {
        playCounter++
        populateViewCount()
    }

    private fun fnPrevSong(view: View) {
        makeToast("Skipping to previous track")
    }

    private fun fnNextSong(view: View) {
        makeToast("Skipping to next track")
    }

    private fun fnChangeUser(view: View) {
        if (btnChangeUser.text == getString(R.string.change_user)) {
                btnChangeUser.text = getString(R.string.apply)
                tvUserName.visibility = View.INVISIBLE
                editUserName.visibility = View.VISIBLE
        } else {
            userName = editUserName.text.toString()
            populateUsername()
            btnChangeUser.text = getString(R.string.change_user)
            tvUserName.visibility = View.VISIBLE
            editUserName.visibility = View.INVISIBLE
        }
    }

    private fun populateViewCount() {
        tvPlayCounter.text = "$playCounter plays"
    }

    private fun populateUsername() {
        tvUserName.text = userName
    }

    private fun makeToast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}