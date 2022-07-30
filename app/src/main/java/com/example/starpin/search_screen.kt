package com.example.starpin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.main_screen.*
import kotlinx.android.synthetic.main.music_item.*
import kotlinx.android.synthetic.main.music_item.view.*
import kotlinx.android.synthetic.main.search_screen.*

class search_screen : AppCompatActivity() {
    var old_item: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_screen)
        //search_filed.setOnClickListener({print("ff")})


        search_tracks_view.adapter = TrackAdapter(
            mutableListOf<Data>(),
            object : OnClick {
                override fun onClickTrack(data: Data, item: View) {
                    Log.e("", "")
                }
            }
        )
        search_tracks_view.layoutManager =
            LinearLayoutManager(
                search_tracks_view.context,
                LinearLayoutManager.VERTICAL,
                false
            )
        search_button.setOnClickListener {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(search_field.windowToken, 0)

            search_field.clearFocus()
            search()
        }
        search_field.setOnKeyListener { view, keycode, keyevent ->
            if (keyevent.keyCode == KeyEvent.KEYCODE_ENTER) {

                search()



                val imm: InputMethodManager =
                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(search_field.windowToken, 0)

                search_field.clearFocus()
                return@setOnKeyListener true
            }
            runOnUiThread { found_status.visibility = View.INVISIBLE }
            return@setOnKeyListener false
        }

    }

    fun search() {
        if ((search_tracks_view.adapter as TrackAdapter).data.size != 0) {
            (search_tracks_view.adapter as TrackAdapter).data.clear()
        }
        Thread {
            runOnUiThread {
                search_loading.visibility = View.VISIBLE
                found_status.visibility = View.INVISIBLE
            }
            val h = search_music(search_field.text.toString()).toMutableList()
            if (h.size != 0) {
                runOnUiThread {
                    search_tracks_view.adapter = TrackAdapter(
                        h,
                        object : OnClick {
                            override fun onClickTrack(data: Data, item: View) {
                                if (old_item != null) {
                                    old_item!!.loader_bar.visibility = View.INVISIBLE
                                }

                                old_item = item
                                item.loader_bar.visibility = View.VISIBLE
                                Thread {
                                    music_player().play(data.name)
                                    runOnUiThread {item.loader_bar.visibility = View.INVISIBLE}
                                }.start()

                            }
                        }
                    )


                }
            } else {
                runOnUiThread { found_status.visibility = View.VISIBLE }
            }
            runOnUiThread { search_loading.visibility = View.INVISIBLE }
        }.start()

    }
}