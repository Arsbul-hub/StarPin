package com.star_wormwood.bulavka.common.Items

import android.annotation.SuppressLint
import android.content.Context

import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.star_wormwood.bulavka.R
import com.star_wormwood.bulavka.User
import kotlinx.android.synthetic.main.track_item.view.*

data class Track(
    var name: String = "",
    var artist: String = "",
    var avatar: String = "",
    var url: String = ""
) {
    fun isFull(): Boolean {
        if (name.isNotBlank() && artist.isNotBlank() && avatar.isNotBlank() && url.isNotBlank()) {
            return true
        }
        return false
    }
}

@SuppressLint("ViewConstructor")
class TrackItem(context: Context, track: Track) :
    LinearLayout(context) {




    init {

        inflate(context, R.layout.track_item, this)
        name.setText(track.name)
        artist.setText(track.artist)
        Glide.with(context).load(track.avatar).into(avatar)
        like.setOnClickListener {
            User.user_manager.addToPlayList("Понравившиеся", track)
            //Log.e("Play Lists", play_lists.toString())
        }

    }


}


