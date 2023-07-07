package com.star_wormwood.bulavka.common

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.core.view.setMargins
import androidx.core.view.setPadding
import com.star_wormwood.bulavka.EditPlayListScreen.EditPlayListFragment

import com.star_wormwood.bulavka.Managers
import com.star_wormwood.bulavka.R
import com.star_wormwood.bulavka.TracksInPlaylistsScreen.TracksFragment
import com.star_wormwood.bulavka.User
import com.star_wormwood.bulavka.common.Items.PlayList
import kotlinx.android.synthetic.main.create_playlist_item.view.*
import kotlinx.android.synthetic.main.play_list_item.view.*
import kotlinx.android.synthetic.main.play_list_item.view.title

interface PlaylistListener {
    fun onClickAdd()
    fun onClick(playList: PlayList)
}

class PlaylistLoader(val list: MutableList<PlayList>, val listener: PlaylistListener) {
    @SuppressLint("InflateParams")
    fun into(container: ViewGroup) {

        container.removeAllViews()

//        val addPlaylistItem = LayoutInflater.from(container.context)
//            .inflate(R.layout.create_playlist_item, container, false)
//        addPlaylistItem.setOnClickListener{
//            listener.onClickAdd()
//        }
//        val addPlaylistItemParam = addPlaylistItem.layoutParams as ViewGroup.MarginLayoutParams
//        addPlaylistItemParam.setMargins(size = 10)
//        addPlaylistItem.layoutParams = addPlaylistItemParam
//        container.addView(addPlaylistItem)

        for (playlist in list.reversed()) {

            val view = LayoutInflater.from(container.context)
                .inflate(R.layout.play_list_item, container, false)
            view.title.text = playlist.name
            view.tracks_count.text = playlist.tracks.size.toString()
            view.setOnClickListener {listener.onClick(playlist)}



            val param = view.layoutParams as ViewGroup.MarginLayoutParams
            param.setMargins(size = 10)
            view.layoutParams = param
            container.addView(view)
        }

    }
}