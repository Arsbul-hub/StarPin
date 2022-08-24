package com.example.starpin

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.starpin.Loader.TracksInPlayLists
import com.example.starpin.Loader.UserPlayLists
import com.example.starpin.common.PlayList
import kotlinx.android.synthetic.main.play_list_item.view.*
import kotlinx.android.synthetic.main.track_item.view.*


class PlayListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
interface OnClickList {
    fun onOpen(playList: PlayList)
}
class PlayListAdapter(val data: MutableList<PlayList>, val on_click: OnClickList?): RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.play_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val list = data[position]
        holder.itemView.play_list_name.text = list.name
        holder.itemView.play_list_name.setOnClickListener{

            on_click!!.onOpen(list)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}