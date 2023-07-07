package com.star_wormwood.bulavka.common.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.star_wormwood.bulavka.R
import com.star_wormwood.bulavka.User
import com.star_wormwood.bulavka.common.Items.PlayList
import kotlinx.android.synthetic.main.play_list_item.view.*


class PlayListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
interface onClickList {
    fun onClick(playList: PlayList)
}



class PlayListAdapter(val data: MutableList<PlayList>, val on_click: onClickList?, reverse: Boolean = false) :
    RecyclerView.Adapter<PlayListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        return PlayListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.play_list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        val list = data.reversed()[position]
        holder.itemView.title.text = list.name
        holder.itemView.setOnClickListener {

            on_click!!.onClick(list)
        }
        holder.itemView.tracks_count.text =
            User.user_manager.getCreatedPlaylist(list.name)!!.tracks.size.toString()

    }

    override fun getItemCount(): Int {
        return data.size
    }
}