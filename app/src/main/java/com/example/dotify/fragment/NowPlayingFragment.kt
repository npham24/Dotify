package com.example.dotify.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.ericchee.songdataprovider.Song
import com.example.dotify.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

class NowPlayingFragment: Fragment() {
    private var playCount = 0
    private var username = ""
    private lateinit var selectedSong: Song


    companion object {
        val TAG = NowPlayingFragment::class.java.simpleName
        const val USER_KEY = "USER_KEY"
        const val PLAY_KEY = "PLAY_KEY"
        const val NOW_PLAYING_FRAGMENT_KEY = "NOW_PLAYING_FRAGMENT_KEY"
        const val DEFAULT_USERNAME = "npham24"

        fun newInstance(song: Song): NowPlayingFragment {
            return NowPlayingFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(NOW_PLAYING_FRAGMENT_KEY, song)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            playCount = savedInstanceState.getInt(PLAY_KEY)
            savedInstanceState.getString(USER_KEY)?.let {
                username = it
            }
        } else {
            playCount = Random.nextInt(1, 100000)
            username = DEFAULT_USERNAME
        }

        arguments?.let {arg ->
            with(arg) {
                getParcelable<Song>(NOW_PLAYING_FRAGMENT_KEY)?.let {song ->
                    selectedSong = song
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        displaySongInfo()
        displayUserName()
        displayPlayCount()

        btnPrev.setOnClickListener {
            makeToast("Skipping to previous track")
        }

        btnNext.setOnClickListener {
            makeToast("Skipping to next track")
        }

        btnPlay.setOnClickListener {
            playCount++;
            displayPlayCount()
        }

        btnChangeUser.setOnClickListener {
            changeUser()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState?.run {
            putInt(PLAY_KEY, playCount)
            putString(USER_KEY, username)
        }
    }

    private fun makeToast(toast: String) {
        Toast.makeText(context, toast, Toast.LENGTH_SHORT).show()
    }

    private fun displaySongInfo() {
        ivAlbumCover.setImageResource(selectedSong.largeImageID)
        tvSongTitle.text = selectedSong.title
        tvSongArtist.text = selectedSong.artist
    }

    private fun displayUserName() {
        tvUserName.text = username
    }

    private fun displayPlayCount() {
        var play = " play";
        if (playCount > 1) {
            play = " plays";
        }

        tvPlayCounter.text = "${playCount} ${play}"
    }

    private fun changeUser() {
        if (btnChangeUser.text == getString(R.string.change_user)) {
            tvUserName.visibility = View.INVISIBLE
            editUserName.visibility = View.VISIBLE
            btnChangeUser.text = getString(R.string.apply)
        } else {
            username = editUserName.text.toString()

            editUserName.visibility = View.INVISIBLE
            tvUserName.visibility = View.VISIBLE
            btnChangeUser.text = getString(R.string.change_user)
            displayUserName()
        }
    }
}