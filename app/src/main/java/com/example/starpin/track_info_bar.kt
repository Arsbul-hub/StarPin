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
import kotlinx.android.synthetic.main.activity_track_info_bar.view.artist
import kotlinx.android.synthetic.main.activity_track_info_bar.view.avatar
import kotlinx.android.synthetic.main.activity_track_info_bar.view.name



import org.w3c.dom.Attr

var current_track: MutableMap<String, String> =
    mutableMapOf(Pair("name", ""), Pair("artist", ""), Pair("avatar", ""), Pair("url", ""))
var playing_state = false
val MusicPlayer = music_player()

interface OnNewTrackPlay {
    fun OnPlay(url: String, item: View)
}

class track_info_bar :
    LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defstyle: Int) : super(
        context,
        attributeSet,
        defstyle
    )

    lateinit var on_new_track: OnNewTrackPlay


    init {

        inflate(context, R.layout.activity_track_info_bar, this)
        visibility = View.GONE
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

    }


    fun open_bar(
        track_name: String,
        track_artist: String,
        track_avatar: String,
        track_url: String,
        item: View
    ) {
        visibility = View.VISIBLE

        name.setText(track_name)
        artist.setText(track_artist)
        Glide.with(this).load(track_avatar).into(avatar)

        if (track_name != current_track["name"]!!) {
            on_new_track.OnPlay(track_url, item)
            current_track = mutableMapOf(
                Pair("name", track_name),
                Pair("artist", track_artist),
                Pair("avatar", track_avatar),
                Pair("url", track_url)
            )
            play_button.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_baseline_pause_24
                )
            )
        } else if (playing_state == true) {
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


}
