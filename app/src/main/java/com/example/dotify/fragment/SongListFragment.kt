package com.example.dotify.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.ericchee.songdataprovider.Song
import com.example.dotify.R
import com.example.dotify.model.OnSongClickListener
import com.example.dotify.model.SongListAdapter
import kotlinx.android.synthetic.main.activity_song_list.*

class SongListFragment: Fragment() {
    private lateinit var onSongClickedListener: OnSongClickListener
    private lateinit var listOfSongs: MutableList<Song>
    private lateinit var songListAdapter: SongListAdapter

    companion object {
        val TAG = SongListFragment::class.java.simpleName
        const val SONG_LIST_FRAGMENT_KEY = "SONG_LIST_FRAGMENT_KEY"
        fun newInstance(listOfSongs: List<Song>): SongListFragment {
            return SongListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(SONG_LIST_FRAGMENT_KEY, ArrayList(listOfSongs))
                }
            }
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if (context is OnSongClickListener) {
            onSongClickedListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { arg ->
            with (arg) {
                getParcelableArrayList<Song>(SONG_LIST_FRAGMENT_KEY)?.let { list ->
                    listOfSongs = list
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_song_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songListAdapter = SongListAdapter(listOfSongs)
        rvSongList.adapter = songListAdapter

        songListAdapter.onSongClickListener = {song ->
            Log.i("lol", "lil")
            onSongClickedListener.onSongClicked(song)
        }
    }

    fun shuffleList() {
        listOfSongs = listOfSongs.apply {
            shuffle()
        }
        songListAdapter.change(listOfSongs)
    }
}