package com.example.starpin


import android.content.Context
import android.content.Context.TELEPHONY_SERVICE
import android.content.Intent
import android.os.Build
import android.telephony.PhoneStateListener
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.example.starpin.common.Services.PlayService
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.track_info_bar.view.*


interface PlayListener {
    fun onStartLoading()
    fun onStopLoading()
}

interface onProgress {

    fun onProgressListener(position: Int, max: Int)
}

//lateinit var media_player: MediaPlayer
var is_prepared: Boolean = false


class MusicManager {
    companion object {
        const val CUSTOM_METADATA_TRACK_SOURCE = "__SOURCE__"
        const val PLAYING = 1
        const val PAUSED = 0
    }

    var currentTrack: Track? = null
    var currentTracksList: MutableList<Track>? = null

    var playingState = PLAYING
    lateinit var onProgress: onProgress
    lateinit var onLoading: PlayListener
    var maxPosition: Int = 0
    var progress: Int = 0

    init {

    }


//        Thread {
//            while (true) {
//                if (::onProgress.isInitialized && playingState == "playing") {
//
//                    onProgress.onProgressListener(getPosition())
//                }
//
//            }
//        }.start()


//    fun loadTrack(url: String, fuc: () -> Unit = {}): String {
//        if (::media_player.isInitialized) {
//
//            media_player.release()
//            is_prepared = false
//        }
//
//        try {
//            media_player = MediaPlayer()
//
//
//            media_player.setAudioAttributes(
//                AudioAttributes.Builder()
//                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
//                    .build()
//            )
//            media_player.setDataSource(url)
//
//            //media_player.isLooping = false
//            media_player.setOnPreparedListener {
//
//                is_prepared = true
//
//
//            }
//            media_player.setOnCompletionListener {
//
//                fuc()
//                //if (getPosition() == getMax()) {
//                //nextTrack()
//                //}
//
//            }
//            media_player.prepare()
//            return "Loaded"
//        } catch (t: Throwable) {
//            return t.message!!
//        }
//    }

    fun play(tracks_list: MutableList<Track>, track: Track) {

        if (track != currentTrack) {
            currentTracksList = tracks_list

            currentTrack = track
            Log.e("tests", "ffff")
            updateBar()


            //Thread {


            try {

                val intent = Intent(Screens.activity, PlayService::class.java)
                //            intent.putExtra("currentTrackIndex", currentTrackIndex)
                //            intent.putExtra("currentTracksList", ArrayList(currentTracksList))


                intent.action = "new"

                //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Screens.activity.startService(intent)

            } catch (e: Exception) {
                Log.e("errro", e.message.toString())
            }


        } else {
            if (playingState == PLAYING) {
                pause()

                Screens.activity.bar.setPlayingState(false)


            } else if (playingState == PAUSED) {
                unpause()
                Screens.activity.bar.setPlayingState(true)

            }

        }

    }

    fun pause() {
        if (is_prepared) {
            //media_player.pause()
            //Screens.activity.stopService(Intent(Screens.activity, PlayService::class.java))
            val intent = Intent(Screens.activity, PlayService::class.java)
//            intent.putExtra("currentTrackIndex", currentTrackIndex)
//            intent.putExtra("currentTracksList", ArrayList(currentTracksList))

            intent.action = "pause"
            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Screens.activity.startService(intent)
            //}
        }
        playingState = PAUSED
        updateBar()

    }

    fun unpause() {
        if (is_prepared) {
            //media_player.start()
            val intent = Intent(Screens.activity, PlayService::class.java)
//            intent.putExtra("currentTrackIndex", currentTrackIndex)
//            intent.putExtra("currentTracksList", ArrayList(currentTracksList))

            intent.action = "unpause"
            Screens.activity.startService(intent)
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
        Screens.activity.startService(intent)
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

    private fun updateBar() {
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