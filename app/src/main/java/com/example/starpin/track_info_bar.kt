package com.example.starpin

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_track_info_bar.view.*


import org.w3c.dom.Attr

var current_track: MutableMap<String, String> =
    mutableMapOf(Pair("name", ""), Pair("artist", ""), Pair("avatar", ""), Pair("url", ""))
var playing_state = false
val MusicPlayer = music_player()

class track_info_bar:
    LinearLayout {
    constructor(context: Context):super(context)
    constructor(context: Context, attributeSet: AttributeSet):super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defstyle: Int):super(context, attributeSet, defstyle)

    init {

        inflate(context, R.layout.activity_track_info_bar, this)
        visibility = View.GONE

    }


    fun open_bar(
        track_name: String,
        track_artist: String,
        track_avatar: String,
        track_url: String

    ) {
        visibility = View.VISIBLE

        play_button.setOnClickListener {

            if (playing_state == true) {
                MusicPlayer.pause()
                playing_state = false
                play_button.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_baseline_play_arrow_24
                    )
                )
            } else {
                playing_state = true
                MusicPlayer.unpause()
                play_button.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.ic_baseline_pause_24
                    )
                )
            }
        }


        name.setText(track_name)
        artist.setText(track_artist)
        Glide.with(this).load(track_avatar).into(avatar)


        current_track["name"] = track_name
        current_track["artist"] = track_artist
        current_track["avatar"] = track_avatar
        current_track["url"] = track_url
    }

}
