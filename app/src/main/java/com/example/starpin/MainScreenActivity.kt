package com.example.starpin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.main_screen.*
import kotlinx.android.synthetic.main.music_item.view.*

class MainScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)

        val f = Intent(this, search_screen::class.java)
        to_search.setOnClickListener({ startActivity(f) })

        //Intent(this, NextActivity::class.java)
//        //b.setText("ttt")
//        for (n in 1..1000) {
//            val b = Button(this)
//            b.setOnClickListener({MyDialog(text="hello", context=this).show()})
//            scr.addView(b)
//
//        }
        //val bar = track_info_bar(all.context, this)
//        ( search_tracks_view.adapter as TrackAdapter).data.clear()
        var old_item: View? = null
        Thread {
            runOnUiThread { top_loading.visibility = View.VISIBLE }
            val p = get_top().toMutableList()
            runOnUiThread {
                tracks_view.adapter = TrackAdapter(p, object : OnClick {
                    override fun onClickTrack(data: Data, item: View) {
                        if (old_item != null) {
                            old_item!!.loader_bar.visibility = View.INVISIBLE
                        }

                        old_item = item
                        item.loader_bar.visibility = View.VISIBLE
                        Thread {
                            music_player().play(data.name, data.artist, data.url)
                            runOnUiThread { item.loader_bar.visibility = View.INVISIBLE }
                        }.start()
                    }
                })
                tracks_view.layoutManager =
                    LinearLayoutManager(tracks_view.context, LinearLayoutManager.VERTICAL, false)
                runOnUiThread { top_loading.visibility = View.INVISIBLE }
            }

        }.start()
//        track_bar.visibility = View.GONE
//
//
//
//        //all.addView(bar)
//        if (current_track["name"] != "") {
//            track_bar.visibility = View.VISIBLE
//            track_bar.set_track_info_bar(current_track["name"]!!, current_track["artist"]!!, current_track["avatar"]!!, state)
//        }

        val sh = getSharedPreferences("data", Context.MODE_PRIVATE)
        sh.edit().putInt("1", 1).apply()

        Log.e("value", sh.getInt("1", 0).toString())
        //Snackbar.make(findViewById(R.id.g), "Hello", Toast.LENGTH_SHORT).show()


        //print(message = ht.html())
//        val rt = Retrofit.Builder().baseUrl("http://jsonplaceholder.typicode.com/").addConverterFactory(
//            GsonConverterFactory.create()).build()
//        val rq = rt.create(hi::class.java)
//        rq.get().enqueue(object: Callback<List<test>>{
//            override fun onResponse(call: Call<List<test>>, response: Response<List<test>>) {
//                if (response.body() != null){
//                    print(response.body().toString())
//                }
//            }
//
//            override fun onFailure(call: Call<List<test>>, t: Throwable) {
//                TODO("Not yet implemented")
//            }
//        })
    }

}