package com.example.starpin


import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import android.view.View
import java.lang.IllegalStateException


interface NextTrack {
    fun OnComplete()
}

lateinit var media_player: MediaPlayer
var is_prepared: Boolean = false

class music_player {

    lateinit var on_complete: NextTrack
    fun play(track_url: String) {

        if (::media_player.isInitialized) {
            media_player.release()
            is_prepared = false
        }



        media_player = MediaPlayer()


        media_player.setAudioAttributes(
            AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()
        )
        media_player.setDataSource(track_url)

        media_player.setOnPreparedListener {

            is_prepared = true

            media_player.start()

        }
        media_player.setOnCompletionListener {
            Log.e("Media player", "Completed!")

            on_complete.OnComplete()
        }


        media_player.prepare()
    }

    fun pause() {
        if (is_prepared) {
            media_player.pause()
        }
    }

    fun unpause() {
        if (is_prepared) {
            media_player.start()
        }
    }
    fun getPosition() : Int {
        var out: Int = 0

        if (::media_player.isInitialized && is_prepared){
            try {
                out = media_player.currentPosition / 1000
            } catch (e: IllegalStateException) {
                out = 0
            }
        }

        return out
    }
    fun getMax(): Int{
        var out: Int = 0
        if (::media_player.isInitialized && is_prepared){
            try {
                out = media_player.duration / 1000
            } catch (e: IllegalStateException) {
                out = 0
            }
        }
        return out
    }

}