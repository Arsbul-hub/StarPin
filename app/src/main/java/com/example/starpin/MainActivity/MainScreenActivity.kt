package com.example.starpin


import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import com.example.starpin.common.Adapters.FragmentAdapter


import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.track_item.view.*


object User {

    lateinit var pref: SharedPreferences
    val user_manager = UserManager()
}
object screens {
    lateinit var activity: Activity
    val main_screen = MainScreenFragment()
    val search_screen = SearchScreenFragment()
    val list_screen = ListFragment()
}
class MainScreenActivity : AppCompatActivity() {
    lateinit var old_item: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        screens.activity = this
        //
        //Log.e("11222", play_lists.toString())
        User.pref = getSharedPreferences("User", Context.MODE_PRIVATE)
        User.user_manager.loadUserData()
        fragment_view.adapter = FragmentAdapter(
            this, listOf(
                screens.main_screen,
                screens.search_screen,
                screens.list_screen

//                TopMusicFragment(),
//                UserPlayListsFragment(),
//                PlayListsTracksFragment()

            )
        )
        //lateinit var old_item: View
        bar.on_new_track = object : OnNewTrackPlay {
            override fun OnPlay(url: String, item: View) {
                if (::old_item.isInitialized) {
                    old_item.loader_bar.visibility = View.INVISIBLE
                }
                old_item = item

                item.loader_bar.visibility = View.VISIBLE

                //bar.open_bar(data.name, data.artist, data.avatar, data.url)
                val t = Thread {
                    //music_player().play(data.url)
                    MusicPlayer.play(track_url = url)



                    playing_state = true
                    runOnUiThread {
                        item.loader_bar.visibility = View.INVISIBLE

                    }
                }
                t.start()
                t.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { p0, p1 -> }
            }
        }



        //fragment_view.setTag(4, "tracks")

        fragment_view.isUserInputEnabled = false
    }

}