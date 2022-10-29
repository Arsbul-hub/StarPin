package com.example.starpin


import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import kotlinx.android.synthetic.main.main_activity.*


interface PlayListener {
    fun onStartLoading()
    fun onStopLoading()
}

interface onProgress {

    fun onProgressListener(progress: Int)
}

lateinit var media_player: MediaPlayer
var is_prepared: Boolean = false


class MusicManager {
    var current_track: Track? = null
    var current_tracks_list: MutableList<Track>? = null
    var current_track_index = 0
    var playingState = "playing"
    lateinit var on_progress: onProgress
    lateinit var onLoading: PlayListener

    init {
        Thread {
            while (true) {
                if (::on_progress.isInitialized && playingState == "playing") {

                    on_progress.onProgressListener(getPosition())
                }

            }
        }.start()
    }

    fun load_track(url: String): String {
        if (::media_player.isInitialized) {

            media_player.release()
            is_prepared = false
        }

        try {
            media_player = MediaPlayer()


            media_player.setAudioAttributes(
                AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                    .build()
            )
            media_player.setDataSource(url)

            //media_player.isLooping = false
            media_player.setOnPreparedListener {

                is_prepared = true


            }
            media_player.setOnCompletionListener {


                //if (getPosition() == getMax()) {
                nextTrack()
                //}

            }
            media_player.prepare()
            return "Loaded"
        }
        catch (t: Throwable){
            return t.message!!
        }
    }

    fun play(tracks_list: MutableList<Track>, index: Int) {
        val track = tracks_list[index]

        Screens.activity.bar.open(track)
        if (track != current_track) {
            current_tracks_list = tracks_list
            current_track_index = index
            current_track = track

            if (::media_player.isInitialized && media_player.isPlaying) {
                media_player.stop()
            }



            Thread {
                Screens.activity.runOnUiThread {
                    onLoading.onStartLoading()
                }

                val load_status = load_track(track.url)

                Log.i("Load Status:", load_status)

                if (playingState == "playing") {
                    media_player.start()


                }
                Screens.activity.runOnUiThread {
                    onLoading.onStopLoading()
                }


            }.start()


            ///playingState = "playing"
        } else {
            if (playingState == "playing") {
                pause()
                Screens.activity.bar.setPlayingState(false)


            } else if (playingState == "paused") {
                unpause()
                Screens.activity.bar.setPlayingState(true)

            }

        }

    }

    fun pause() {
        if (is_prepared) {
            media_player.pause()

        }
        playingState = "paused"

    }

    fun unpause() {
        if (is_prepared) {
            media_player.start()

        }
        playingState = "playing"
    }

    fun getPosition(): Int {
        var out: Int = 0

        if (::media_player.isInitialized && is_prepared) {
            try {
                out = media_player.currentPosition
            } catch (e: IllegalStateException) {
                out = 0
            }
        }

        return out
    }

    fun setPosition(sec: Int) {
        if (::media_player.isInitialized && is_prepared) {
            media_player.seekTo(sec)
        }
    }

    fun getMax(): Int {
        var out: Int = 0
        if (::media_player.isInitialized && is_prepared) {
            try {
                out = media_player.duration
            } catch (e: IllegalStateException) {
                out = 0
            }
        }
        return out
    }

    fun nextTrack() {
        if (current_track_index + 1 != current_tracks_list!!.size) {
            play(current_tracks_list!!, current_track_index + 1)
        } else {
            play(current_tracks_list!!, 0)
        }
    }

    fun previousTrack() {
        if (current_track_index - 1 >= 0) {
            play(current_tracks_list!!, current_track_index - 1)
        } else {
            play(current_tracks_list!!, current_tracks_list!!.size - 1)
        }
    }

    fun setOnProgressListener(listener: onProgress) {
        on_progress = listener
    }

}