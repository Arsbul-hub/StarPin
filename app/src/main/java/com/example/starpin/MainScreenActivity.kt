package com.example.starpin

import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.main_screen.*
import kotlinx.android.synthetic.main.music_item.*
import kotlinx.android.synthetic.main.music_item.view.*

class MainScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_screen)
        val scr: LinearLayout = findViewById(R.id.box)




        val f = Intent(this, search_screen::class.java)
        to_search.setOnClickListener({startActivity(f)})

        //Intent(this, NextActivity::class.java)
//        //b.setText("ttt")
//        for (n in 1..1000) {
//            val b = Button(this)
//            b.setOnClickListener({MyDialog(text="hello", context=this).show()})
//            scr.addView(b)
//
//        }
        val sh = getSharedPreferences("data", Context.MODE_PRIVATE)
        sh.edit().putInt("1", 1).apply()

        Log.e("value", sh.getInt("1", 0).toString())
        Snackbar.make(findViewById(R.id.g), "Hello", Toast.LENGTH_SHORT).show()


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