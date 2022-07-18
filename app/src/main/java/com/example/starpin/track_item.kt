package com.example.starpin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.music_item.view.*


class music_item(context: Context, track_name: String, artist_name: String, track_url: String) :
    LinearLayout(context) {




    init {

        inflate(context, R.layout.music_item, this)
        name.setText(track_name)
        artist.setText(artist_name)
        Glide.with(context).load(track_url).into(track_avatar)


    }



}


