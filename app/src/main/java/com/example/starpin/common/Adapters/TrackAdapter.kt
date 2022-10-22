package com.example.starpin

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.track_item.view.*


class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
interface OnClick {
    fun onClickTrack(tracks_list: MutableList<Track>, track_index: Int)
    fun onChooseTrack(tracks_list: MutableList<Track>, track_index: Int)
    fun onDeselctTracks(tracks_list: MutableList<Track>, track_index: Int)
}

class TrackAdapter(val data: MutableList<Track>, val on_click: OnClick?) :
    RecyclerView.Adapter<TrackViewHolder>() {

    val selectedPositions = mutableListOf<Int>()
    var showPanel = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = data[position]
        val isSelect = position in selectedPositions

        if (isSelect) {
            holder.itemView.setBackgroundColor(Color.parseColor("#e1a5d2"))

            holder.itemView.selected.visibility = View.VISIBLE
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE)
            holder.itemView.selected.visibility = View.GONE
        }

        if (showPanel) {
            holder.itemView.panel.visibility = View.VISIBLE
        } else {
            holder.itemView.panel.visibility = View.GONE
        }

        holder.itemView.name.setText(track.name)
        holder.itemView.artist.setText(track.artist)
        holder.itemView.setOnClickListener {
            if (selectedPositions.isNotEmpty()) {
                if (position in selectedPositions) {
                    deselectItem(holder.itemView, position)
                    if (selectedPositions.isEmpty()) {
                        on_click?.onDeselctTracks(data, position)
                        showItemsPanel()
                    }
                } else {
                    selectItem(holder.itemView, position)
                }

            } else {
                on_click?.onClickTrack(data, position)
            }
        }

        holder.itemView.setOnLongClickListener {
            selectItem(holder.itemView, position)
            hideItemsPanel()
            on_click?.onChooseTrack(data, position)
            return@setOnLongClickListener true
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

    fun getSelectedItems(): MutableList<Track> {
        val out = mutableListOf<Track>()
        for (position in selectedPositions) {
            out.add(data[position])
        }
        return out
    }

    fun deselectAll() {
        selectedPositions.clear()
        showItemsPanel()

    }

    fun showItemsPanel() {
        showPanel = true

        notifyDataSetChanged()
    }

    fun hideItemsPanel() {
        showPanel = false
        notifyDataSetChanged()
    }

    fun selectItem(itemView: View, position: Int) {
        selectedPositions.add(position)
        itemView.setBackgroundColor(Color.parseColor("#e1a5d2"))

        itemView.selected.visibility = View.VISIBLE

    }

    fun deselectItem(itemView: View, position: Int) {

        selectedPositions.remove(position)

        itemView.setBackgroundColor(Color.WHITE)
        itemView.selected.visibility = View.GONE
    }
}