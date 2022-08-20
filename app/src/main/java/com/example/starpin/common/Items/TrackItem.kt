package com.example.starpin

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log

import android.widget.LinearLayout
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.track_item.view.*

data class Track(val name: String, val artist: String, val avatar: String, val url: String)

@SuppressLint("ViewConstructor")
class TrackItem(context: Context, track: Track) :
    LinearLayout(context) {




    init {

        inflate(context, R.layout.track_item, this)
        name.setText(track.name)
        artist.setText(track.artist)
        Glide.with(context).load(track.avatar).into(avatar)
        like.setOnClickListener {
            User.user_manager.addToPlayList("Понравившиеся", track)
            //Log.e("Play Lists", play_lists.toString())
        }

    }



}


