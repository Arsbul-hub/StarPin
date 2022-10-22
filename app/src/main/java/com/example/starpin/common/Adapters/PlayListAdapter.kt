package com.example.starpin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.starpin.common.PlayList
import kotlinx.android.synthetic.main.play_list_item.view.*


class PlayListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
interface onClickList {
    fun onClick(playList: PlayList)
}
class PlayListAdapter(val data: MutableList<PlayList>, val on_click: onClickList?): RecyclerView.Adapter<PlayListViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        return PlayListViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.play_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        val list = data[position]
        holder.itemView.play_list_name.text = list.name
        holder.itemView.play_list_name.setOnClickListener{

            on_click!!.onClick(list)
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}