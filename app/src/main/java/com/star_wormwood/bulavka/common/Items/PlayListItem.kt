package com.star_wormwood.bulavka.common.Items

import android.annotation.SuppressLint
import android.content.Context

import android.widget.LinearLayout
import com.star_wormwood.bulavka.R

data class PlayList(
    var name: String = "",
    var avatar: String = "",
    var tracks: MutableList<Track> = mutableListOf()
) {
    operator fun contains(track: Track): Boolean {
        if (track in tracks) {
            return true
        }
        return false
    }
    fun add(track: Track) {
        if (track !in tracks) {
            tracks.add(track)
        }
    }
}

@SuppressLint("ViewConstructor")
class PlayListItem(context: Context, playlist: PlayList) :
    LinearLayout(context) {




    init {

        inflate(context, R.layout.play_list_item, this)
        //play_list_name.text = playlist.name

    }

}


