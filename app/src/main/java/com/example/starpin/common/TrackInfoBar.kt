package com.example.starpin

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide


import kotlinx.android.synthetic.main.track_info_bar.view.*


var current_track: Track = Track(name = "", artist = "", avatar = "", url = "")
var playing_state = false


interface OnNewTrackPlay {
    fun OnPlay(url: String, item: View)
}

class TrackInfoBar :
    LinearLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet)
    constructor(context: Context, attributeSet: AttributeSet, defstyle: Int) : super(
        context,
        attributeSet,
        defstyle
    )

    var progress = true



    var is_choosing_prrogress = false

    init {

        inflate(context, R.layout.track_info_bar, this)
        //visibility = View.GONE
        next_track.setOnClickListener {
            Managers.musicManager.nextTrack()
        }
        previous_track.setOnClickListener {
            Managers.musicManager.previousTrack()
        }
        play_button.setOnClickListener {

            Managers.musicManager.play(Managers.musicManager.current_tracks_list!!, Managers.musicManager.current_track_index)
        }
        Managers.musicManager.onLoading = object: PlayListener {
            override fun onStartLoading() {

                progress_bar.progress = 0
                loading.visibility = View.VISIBLE

            }

            override fun onStopLoading() {
                loading.visibility = View.GONE
            }
        }
        Managers.musicManager.setOnProgressListener(object: onProgress {
            override fun onProgressListener(progress: Int) {
                val pos = Managers.musicManager.getPosition()
                val max = Managers.musicManager.getMax()

                if (pos > 0 && max > 0 && !is_choosing_prrogress) {
                    //Log.e(pos.toString(), max.toString())
                    val prosents = pos / (max / 100)
                    progress_bar.progress = prosents
                }
            }
        })

        progress_bar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                //Log.e("111", progress.toString())

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

                is_choosing_prrogress = true

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (Managers.musicManager.getMax() > 0) {
                   Managers.musicManager.setPosition(progress_bar.progress * (Managers.musicManager.getMax() / 100).toInt())
                } else {
                    Managers.musicManager.setPosition(0)
                }

                //}
                is_choosing_prrogress = false
            }
        })

    }

    fun open(track: Track) {







        visibility = View.VISIBLE
        name.setText(track.name)
        artist.setText(track.artist)
        Glide.with(this).load(track.avatar).into(avatar)


    }
    fun disable_all() {

        play_button.isClickable = false
        play_button.isEnabled = false
        progress_bar.progress = 0
        progress_bar.isClickable = false
        progress_bar.isEnabled = false
    }
    fun enable_all() {
        Screens.activity.runOnUiThread{
            play_button.isClickable = true
            play_button.isEnabled = true

            progress_bar.isClickable = true
            progress_bar.isEnabled = true
        }
    }
    fun setPlayingState(is_playing: Boolean) {
        if (is_playing) {

            play_button.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_baseline_pause_24
                )
            )
        }
        else {
            Log.e("is_playing", "false")
            play_button.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_baseline_play_arrow_24
                )
            )
        }
//        } else {
//
//            play_button.setImageDrawable(
//                ContextCompat.getDrawable(
//                    context,
//                    R.drawable.ic_baseline_play_arrow_24
//                )
//            )
//
//        }
    }
//
//    fun setProgress() {
//
//
//
//        //Log.e("position", MusicPlayer.getPosition().toString())
////            delay(400)
//
//
//    }
}
