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
    private lateinit var songListAdapter: SongListAdapter
    private lateinit var listOfSongs: MutableList<Song>
    private lateinit var onSongClickListener: OnSongClickListener

    companion object {
        const val SONG_LIST_FRAGMENT_KEY: String = "song_list_key"
        const val LIST_ORDER_KEY = "list_order_key"
        val TAG = SongListFragment::class.java.simpleName

        fun getInstance(listOfSongs: List<Song>): SongListFragment {
            return SongListFragment().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(SONG_LIST_FRAGMENT_KEY, ArrayList(listOfSongs))
                }
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnSongClickListener) {
            onSongClickListener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState != null) {
            savedInstanceState.getParcelableArrayList<Song>(LIST_ORDER_KEY)?.let { list ->
                listOfSongs = list.toMutableList()
            }
        } else {
            arguments?.let { args ->
                with (args) {
                    getParcelableArrayList<Song>(SONG_LIST_FRAGMENT_KEY)?.let { list ->
                        listOfSongs = list.toMutableList()
                    }
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return layoutInflater.inflate(R.layout.activity_song_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        songListAdapter = SongListAdapter(listOfSongs)
        rvSongList.adapter = songListAdapter

        songListAdapter.onSongClickListener = {song ->
            onSongClickListener.onSongClicked(song)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelableArrayList(LIST_ORDER_KEY, ArrayList(listOfSongs))
        super.onSaveInstanceState(outState)
    }

    fun shuffleMusicList() {
        listOfSongs = listOfSongs.apply { shuffle() }
        songListAdapter.change(listOfSongs)
    }
}