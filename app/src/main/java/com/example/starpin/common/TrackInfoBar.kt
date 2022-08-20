package com.example.starpin

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.track_info_bar.view.*

var current_track: Track = Track(name = "", artist = "", avatar = "", url = "")
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
    var complete_track_index = 0

    init {

        inflate(context, R.layout.track_info_bar, this)
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


    fun OpenBar(tracks_list: MutableList<Track>, track_index: Int, item: View) {
        val track = tracks_list[track_index]

        MusicPlayer.on_complete = object : NextTrack {
            override fun OnComplete() {
                if (track_index + 1 < tracks_list.size) {
                    OpenBar(tracks_list, track_index + 1, item)

                } else {
                    OpenBar(tracks_list, 0, item)
                }

            }
        }

        visibility = View.VISIBLE
        name.setText(track.name)
        artist.setText(track.artist)
        Glide.with(this).load(track.avatar).into(avatar)


        if (track != current_track) {

            on_new_track.OnPlay(track.url, item)

            current_track = track
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
