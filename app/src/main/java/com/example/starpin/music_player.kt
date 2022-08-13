package com.example.starpin


import android.media.AudioAttributes
import android.media.MediaPlayer



lateinit var media_player: MediaPlayer
var is_prepared: Boolean = false
class music_player {


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

}