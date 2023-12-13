package com.star_wormwood.bulavka.common.Adapters


import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.star_wormwood.bulavka.R
import com.star_wormwood.bulavka.User
import com.star_wormwood.bulavka.common.Items.Track
import kotlinx.android.synthetic.main.track_item.view.*


class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
interface OnClick {
    fun onClickTrack(tracks_list: List<Track>, track: Track)
    fun onChooseTrack(tracks_list: List<Track>, track: Track)
    fun onDeselectTracks(tracks_list: List<Track>, track: Track)
}

class TrackAdapter(var list: List<Track>, val on_click: OnClick?) :
    RecyclerView.Adapter<TrackViewHolder>() {
    var loading = false
    val selectedPositions = mutableListOf<Int>()
    var showPanel = true

    //    init {
//        if (start + 20 < list.size) {
//            data = list.slice(start..start + step).toMutableList()
//        }else {
//            data = list
//        }
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        Log.e("child", viewType.toString())
        if (viewType == 1) {
            return TrackViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.loading_item, parent, false)
            )

        } else {
            return TrackViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.track_item, parent, false)
            )

        }


    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {


        if (position < list.size) {
            val track = list[position]
            val isSelect = position in selectedPositions

            if (isSelect) {
                holder.itemView.setBackgroundColor(Color.parseColor("#e1a5d2"))

            } else {
                holder.itemView.setBackgroundColor(Color.WHITE)

            }

            if (showPanel) {
                holder.itemView.like.visibility = View.VISIBLE
            } else {
                holder.itemView.like.visibility = View.GONE
            }

            holder.itemView.name.setText(track.name)
            holder.itemView.artist.setText(track.artist)
            holder.itemView.setOnClickListener {
                if (selectedPositions.isNotEmpty()) {
                    if (position in selectedPositions) {
                        deselectItem(holder.itemView, position)
                        if (selectedPositions.isEmpty()) {
                            on_click?.onDeselectTracks(list, track)
                            showItemsPanel()
                        }
                    } else {
                        selectItem(holder.itemView, position)
                    }

                } else {
                    on_click?.onClickTrack(list, track)
                }
            }

            holder.itemView.setOnLongClickListener {
                selectItem(holder.itemView, position)
                hideItemsPanel()
                on_click?.onChooseTrack(list, track)
                return@setOnLongClickListener true
            }
            if ("no-cover" in track.avatar) {
                holder.itemView.avatar.setImageResource(R.drawable.no_avatar)
            } else {
                val options: RequestOptions = RequestOptions().error(R.drawable.no_avatar)
                Log.e("grsfe", track.avatar)
                Glide.with(holder.itemView.context).load(track.avatar).apply(options)
                    .into(holder.itemView.avatar)

            }


            if (track in User.user_manager.getServicePlaylist("Понравившиеся")!!.tracks) {
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
                if (track in User.user_manager.getServicePlaylist("Понравившиеся")!!.tracks) {
                    User.user_manager.removeFromServicePlayList("Понравившиеся", track)
                    holder.itemView.like.setImageDrawable(
                        ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.no_liked
                        )
                    )
                } else {
                    User.user_manager.addToServicePlayList("Понравившиеся", track)
                    holder.itemView.like.setImageDrawable(
                        ContextCompat.getDrawable(
                            holder.itemView.context,
                            R.drawable.liked
                        )
                    )
                    //(holder.itemView.like.drawable as Animatable?)?.start()
                }
                //holder.itemView.like.startAnimation(AnimationUtils.loadAnimation(Screens.activity, R.anim.bonuce))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (list.size == position) { // loading
            return 1
        } else {
            return 0
        }
    }

    fun startLoading() {
        loading = true
        notifyDataSetChanged()
    }

    fun stopLoading() {
        loading = false
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        if (loading) {
            return list.size + 1
        } else {
            return list.size
        }

    }

    fun getSelectedTracks(): MutableList<Track> {
        val out = mutableListOf<Track>()
        for (position in selectedPositions) {
            out.add(list[position])
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


    }

    fun deselectItem(itemView: View, position: Int) {

        selectedPositions.remove(position)
//
        itemView.setBackgroundColor(Color.WHITE)

    }
}