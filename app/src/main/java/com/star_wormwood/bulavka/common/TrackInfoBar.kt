@file:OptIn(ExperimentalTime::class)

package com.star_wormwood.bulavka.common

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.star_wormwood.bulavka.*
import com.star_wormwood.bulavka.common.Items.Track
import com.star_wormwood.bulavka.common.Managers.MediaPlayerCallback
import com.star_wormwood.bulavka.common.Managers.MusicManager
import com.star_wormwood.bulavka.common.Managers.PlayListener
import com.star_wormwood.bulavka.common.Managers.onProgress


import kotlinx.android.synthetic.main.track_info_bar.view.*
import kotlinx.android.synthetic.main.track_info_bar.view.artist
import kotlinx.android.synthetic.main.track_info_bar.view.avatar
import kotlinx.android.synthetic.main.track_info_bar.view.name
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import kotlin.time.ExperimentalTime
import kotlin.time.minutes

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
    val old_state = false

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

            Managers.musicManager.switchState()
        }
        Managers.musicManager.setMediaCallback(object : MediaPlayerCallback {
            override fun onSeekTo() {
                is_choosing_prrogress = false
            }

            override fun onError() {

            }

        })
        Managers.musicManager.onLoading = object : PlayListener {
            override fun onStartLoading() {

                progress_bar.progress = 0
                loading.visibility = View.VISIBLE

            }

            override fun onStopLoading() {
                loading.visibility = View.GONE
            }
        }
        Managers.musicManager.setOnProgressListener(object : onProgress {
            @SuppressLint("SetTextI18n")
            override fun onProgressListener(position: Int, max: Int) {


                if (position > 0 && max > 0 && !is_choosing_prrogress) {
                    //Log.e(pos.toString(), max.toString())

                    val prosents = position / (max / 100)
                    Log.e("ppr", prosents.toString())
                    progress_bar.progress = prosents
                    val maxSeconds = max.milliseconds.inWholeSeconds
                    val maxMinutes = max.milliseconds.inWholeMinutes

                    val positionSeconds = position.milliseconds.inWholeSeconds
                    val positionMinutes = position.milliseconds.inWholeMinutes
                    Screens.activity.runOnUiThread {

                        val displayPositionMinutes = MusicManager.human(positionMinutes)
                        val displayPositionSeconds =
                            MusicManager.human((positionSeconds.seconds - positionMinutes.minutes).inWholeSeconds)
                        val displayDurationMinutes = MusicManager.human(maxMinutes)
                        val displayDurationSeconds =
                            MusicManager.human((maxSeconds.seconds - maxMinutes.minutes).inWholeSeconds)

                        play_position.text = "$displayPositionMinutes:$displayPositionSeconds"
                        duration.text = "$displayDurationMinutes:$displayDurationSeconds"
                    }
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

                Managers.musicManager.setPosition(progress_bar.progress * (Managers.musicManager.maxPosition / 100))


                //}


            }
        })

    }

    fun open(track: Track) {

        if (visibility == View.GONE) {
            visibility = View.VISIBLE
            startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_in))
        }


        connection_error.visibility = View.INVISIBLE
        name.text = track.name
        artist.text = track.artist

        if ("no-cover" in track.avatar) {
            avatar.setImageResource(R.drawable.no_avatar)
        } else {
            val options: RequestOptions = RequestOptions().error(R.drawable.no_avatar)
            Glide.with(context).load(track.avatar).apply(options)
                .into(avatar)

        }


    }

    fun disable_all() {

        play_button.isClickable = false
        play_button.isEnabled = false
        progress_bar.progress = 0
        progress_bar.isClickable = false
        progress_bar.isEnabled = false
    }

    fun enable_all() {
        Screens.activity.runOnUiThread {
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
        } else {
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
