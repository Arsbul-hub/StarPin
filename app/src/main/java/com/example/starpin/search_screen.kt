package com.example.starpin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.children
import kotlinx.android.synthetic.main.music_item.*
import kotlinx.android.synthetic.main.music_item.view.*
import kotlinx.android.synthetic.main.search_screen.*

class search_screen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_screen)
        //search_filed.setOnClickListener({print("ff")})
        search_field.setOnKeyListener({ view, keycode, keyevent ->
            if (keyevent.keyCode == KeyEvent.KEYCODE_ENTER) {
                box.removeAllViews()
                load_search_results()
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        })

    }

    fun load_search_results() {
        Thread {
            val h = search_music(search_field.text.toString())
            for (i in h) {
                // Log.e("fff", i["avatar"]!!)
                runOnUiThread {
                    val b = music_item(
                        this,
                        track_name = i["name"]!!,
                        artist_name = i["artist"]!!,
                        track_url = i["avatar"]!!
                    )
                    //b.setText(i)
                    //b.isAllCaps = false
                    b.setOnClickListener({
                        Thread({
                            runOnUiThread({b.loader_bar.visibility = View.VISIBLE})
                            val f = play(i["url"]!!, b)
                            if (f == true) {
                                b.play_button.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        this,
                                        R.drawable.ic_baseline_pause_24
                                    )
                                )
                            } else {
                                b.play_button.setImageDrawable(
                                    ContextCompat.getDrawable(
                                        this,
                                        R.drawable.ic_baseline_play_arrow_24
                                    )
                                )
                            }
                            runOnUiThread({b.loader_bar.visibility = View.INVISIBLE})
                        }).start()
                    })
                    box.addView(b)
                }
                //Log.e("names", i)


            }

        }.start()

    }
}