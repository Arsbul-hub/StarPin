package com.example.starpin

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.track_item.view.*


class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
interface OnClick {
    fun onClickTrack(tracks_list: MutableList<Track>, track_index: Int, item: View)
}

class TrackAdapter(val data: MutableList<Track>, val on_click: OnClick?) :
    RecyclerView.Adapter<TrackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = data[position]
        holder.itemView.name.setText(track.name)
        holder.itemView.artist.setText(track.artist)
        holder.itemView.setOnClickListener {
            on_click?.onClickTrack(
                data,
                position,
                holder.itemView
            )
        }
        Glide.with(holder.itemView.context).load(track.avatar).into(holder.itemView.avatar)

        if (User.user_manager.play_lists["Понравившиеся"]!!.tracks.contains(track)) {

            holder.itemView.like.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.liked
                )
            )
        } else {

            holder.itemView.like.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.no_liked
                )
            )
        }
        holder.itemView.like.setOnClickListener {
            if (User.user_manager.play_lists["Понравившиеся"]!!.tracks.contains(track)) {
                User.user_manager.deleteFromPlayList("Понравившиеся", track)
                holder.itemView.like.setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.no_liked
                    )
                )
            } else {
                User.user_manager.addToPlayList("Понравившиеся", track)
                holder.itemView.like.setImageDrawable(
                    ContextCompat.getDrawable(
                        holder.itemView.context,
                        R.drawable.liked
                    )
                )
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }
}