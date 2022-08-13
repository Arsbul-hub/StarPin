package com.example.starpin

import android.content.Context
import android.util.Log

import android.widget.LinearLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.music_item.view.*

data class Track(val name: String, val artist: String, val avatar: String, val url: String)

class music_item(context: Context, track: Track) :
    LinearLayout(context) {




    init {

        inflate(context, R.layout.music_item, this)
        name.setText(track.name)
        artist.setText(track.artist)
        Glide.with(context).load(track.avatar).into(avatar)
        like.setOnClickListener {
            UserManager().addToPlayList("Понравившиеся", track)
            Log.e("Play Lists", play_lists.toString())
        }

    }



}


