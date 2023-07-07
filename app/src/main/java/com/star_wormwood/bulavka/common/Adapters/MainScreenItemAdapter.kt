package com.star_wormwood.bulavka.common.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.star_wormwood.bulavka.R
import kotlinx.android.synthetic.main.main_screen_item.view.*


class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

data class ScreenItem(val image: Int, val onClick: () -> Unit)
class MainScreenAdapter(val data: ArrayList<ScreenItem>) :
    RecyclerView.Adapter<ItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.main_screen_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val itemData = data[position]
        holder.itemView.image.setBackgroundResource(itemData.image)
        holder.itemView.setOnClickListener {
            itemData.onClick()
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }


}