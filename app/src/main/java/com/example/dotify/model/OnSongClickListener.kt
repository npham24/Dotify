package com.example.dotify.model

import com.ericchee.songdataprovider.Song

interface OnSongClickListener {
    fun onSongClicked(song: Song)
}