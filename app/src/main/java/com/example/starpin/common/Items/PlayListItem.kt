package com.example.starpin.common

import android.annotation.SuppressLint
import android.content.Context

import android.util.Log
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.example.starpin.R
import com.example.starpin.Track
import com.example.starpin.UserManager

import kotlinx.android.synthetic.main.play_list_item.view.*
import kotlinx.android.synthetic.main.track_item.view.*

data class PlayList(var name: String = "", var tracks: MutableList<Track> = mutableListOf())

@SuppressLint("ViewConstructor")
class PlayListItem(context: Context, playlist: PlayList) :
    LinearLayout(context) {




    init {

        inflate(context, R.layout.play_list_item, this)
        play_list_name.text = playlist.name

    }

}


