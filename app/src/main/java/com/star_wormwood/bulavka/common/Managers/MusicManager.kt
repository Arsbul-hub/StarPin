package com.star_wormwood.bulavka.common.Managers


import android.content.Intent
import android.os.Build
import android.util.Log
import com.star_wormwood.bulavka.Managers
import com.star_wormwood.bulavka.R
import com.star_wormwood.bulavka.Screens
import com.star_wormwood.bulavka.common.Items.Track

import com.star_wormwood.bulavka.common.Services.PlayService
import com.star_wormwood.bulavka.common.Services.Test


import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.track_info_bar.view.*
import kotlin.time.Duration.Companion.seconds
import kotlin.time.minutes

interface PlayListener {
    fun onStartLoading()
    fun onStopLoading()
}

interface onProgress {

    fun onProgressListener(position: Int, max: Int)
}

interface MediaPlayerCallback {
    fun onSeekTo()
    fun onError()

}


var is_prepared: Boolean = false


class MusicManager {
    companion object {
        const val CUSTOM_METADATA_TRACK_SOURCE = "__SOURCE__"
        const val PLAYING = 2
        const val NOT_PREPARED = 0
        const val PAUSED = 1
        fun human(value: Long): String {
            var outputhuman = "00"


            if (value > 9) {
                outputhuman = value.toString()
            } else {
                outputhuman = "0$value"
            }
            return outputhuman
        }
    }

    var oldPlayingState = 0
    var currentTrack: Track? = null
    var currentTracksList: List<Track>? = null
    var mediaPlayerCallback: MediaPlayerCallback? = null
    var playingState = NOT_PREPARED
    var onProgress: onProgress? = null
    var onLoading: PlayListener? = null
    var maxPosition: Int = 0
    var progress: Int = 0


    fun setMediaCallback(callback: MediaPlayerCallback) {
        mediaPlayerCallback = callback
    }

    fun play(tracks_list: List<Track>, track: Track) {

        if (track != currentTrack) {
            progress = 0
            currentTracksList = tracks_list
            currentTrack = track
            updateBar()


            try {

                val intent = Intent(Screens.activity, PlayService::class.java)
                intent.action = "new"
                startPlayService(intent)
                if (playingState == NOT_PREPARED) {
                    playingState = PLAYING
                }

            } catch (e: Exception) {
                Log.e("errro", e.message.toString())
            }


        } else {
            if (playingState == PLAYING) {
                pause()




            } else if (playingState == PAUSED) {
                unpause()


            }

        }
        updateBar()
    }

    fun pause() {
        if (is_prepared) {

            val intent = Intent(Screens.activity, PlayService::class.java)

            intent.action = "pause"

            startPlayService(intent)
            //}
        }
        playingState = PAUSED
        updateBar()

    }

    fun unpause() {
        if (is_prepared) {
            val intent = Intent(Screens.activity, PlayService::class.java)
            intent.action = "unpause"
            startPlayService(intent)
        }
        playingState = PLAYING
        updateBar()
    }

    fun switchState() {
        if (playingState == PLAYING) {
            pause()

            Screens.activity.bar.setPlayingState(false)


        } else if (playingState == PAUSED) {
            unpause()
            Screens.activity.bar.setPlayingState(true)

        }

    }

//    fun getPosition(): Int {
//        var out: Int = 0
//
//        if (::media_player.isInitialized && is_prepared) {
//            try {
//                out = media_player.currentPosition
//            } catch (e: IllegalStateException) {
//                out = 0
//            }
//        }
//
//        return out
//    }

    fun setPosition(msec: Int) {

        val intent = Intent(Screens.activity, PlayService::class.java)
        intent.putExtra("position", msec)
        intent.action = "seekTo"

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startPlayService(intent)
        //}
    }


    fun nextTrack(): Track {

        if (getCurrentTrackPosition() + 1 != currentTracksList!!.size) {
            play(currentTracksList!!, currentTracksList!![getCurrentTrackPosition() + 1])
            return currentTrack!!
        } else {
            play(currentTracksList!!, currentTracksList!!.first())
            return currentTrack!!
        }

    }

    fun previousTrack(): Track {

        if (getCurrentTrackPosition() - 1 >= 0) {
            play(currentTracksList!!, currentTracksList!![getCurrentTrackPosition() - 1])
            return currentTrack!!
        } else {
            play(currentTracksList!!, currentTracksList!![currentTracksList!!.size - 1])
            return currentTrack!!
        }

    }

    fun setOnProgressListener(listener: onProgress) {
        onProgress = listener
    }

    fun getCurrentTrackPosition(): Int {
        return currentTracksList!!.indexOf(currentTrack)
    }

    private fun startPlayService(intent: Intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {


            Screens.activity.startForegroundService(intent)
        } else {

            Screens.activity.startService(intent)
        }
    }

    fun updateBar() {
        try {
            Screens.activity.bar.open(currentTrack!!)
            if (playingState == PLAYING) {
                Screens.activity.bar.play_button.setImageResource(R.drawable.ic_baseline_pause_24)
            } else {
                Screens.activity.bar.play_button.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }

        } catch (e: Exception) {

        }
    }
}