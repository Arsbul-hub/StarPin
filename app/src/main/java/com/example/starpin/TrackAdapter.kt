package com.example.starpin

import android.net.LinkAddress
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.music_item.view.*

data class Data(val name: String, val artist: String, val url: String, val avatar: String)
class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
interface OnClick {
    fun onClickTrack(data: Data, item: View)
}
class TrackAdapter(val data: MutableList<Data>, val on_click: OnClick): RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.music_item, parent, false))
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = data[position]
        holder.itemView.name.setText(track.name)
        holder.itemView.artist.setText(track.artist)
        holder.itemView.setOnClickListener{on_click.onClickTrack(track, holder.itemView)}
        Glide.with(holder.itemView.context).load(track.avatar).into(holder.itemView.avatar)
    }

    override fun getItemCount(): Int {
        return data.size
    }
}