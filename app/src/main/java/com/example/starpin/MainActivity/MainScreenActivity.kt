package com.example.starpin


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telephony.PhoneStateListener
import android.telephony.TelephonyCallback
import android.telephony.TelephonyManager
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat

import com.example.starpin.common.Managers.FragmentNavigationManager
import com.example.starpin.common.Services.PlayService


import kotlinx.android.synthetic.main.main_activity.*


object User {

    lateinit var pref: SharedPreferences
    val user_manager = UserManager()
}
object Managers {
    @SuppressLint("StaticFieldLeak")
    val musicManager = MusicManager()
    lateinit var fragmentManager: FragmentNavigationManager
}
@SuppressLint("StaticFieldLeak")
object Screens {
    lateinit var activity: MainScreenActivity
    val main_screen = NavigationScreenFragment()


}


object activityObjects {
    lateinit var intent: Intent
}
class MainScreenActivity : AppCompatActivity() {
    lateinit var old_item: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        Screens.activity = this

        //
        //Log.e("11222", play_lists.toString())
        User.pref = getSharedPreferences("User", Context.MODE_PRIVATE)
        User.user_manager.loadUserData()
        Managers.fragmentManager = FragmentNavigationManager(fragmentView.id, supportFragmentManager)

//        fragment_view.adapter = FragmentAdapter(
//            this, listOf(
//                Screens.main_screen,
//                Screens.search_screen,
//                Screens.list_screen,
//                Screens.playlists_screen,
//                Screens.tracks_screen,
//                Screens.top_screen,
//
////                TopMusicFragment(),
////                UserPlayListsFragment(),
////                PlayListsTracksFragment()
//
//            )
//        )
        Managers.fragmentManager.goToFragment(Screens.main_screen)
        //lateinit var old_item: View

//        bar.on_new_track = object : OnNewTrackPlay {
//            override fun OnPlay(url: String, item: View) {
//                if (::old_item.isInitialized) {
//                    old_item.loader_bar.visibility = View.INVISIBLE
//                }
//                old_item = item
//
//                item.loader_bar.visibility = View.VISIBLE
//
//                //bar.open_bar(data.name, data.artist, data.avatar, data.url)
//                //startService(Intent(this@MainScreenActivity, TrackComplete::class.java))
//                val t = Thread {
//                    //music_player().play(data.url)
//                    bar.MusicPlayer.play(track_url = url)
//
//
//
//                    playing_state = true
//                    runOnUiThread {
//                        item.loader_bar.visibility = View.INVISIBLE
//
//                    }
//                }
//                t.start()
//                t.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { p0, p1 -> }
//            }
//        }



        //fragment_view.setTag(4, "tracks")

        //fragment_view.isUserInputEnabled = false

    }
    fun tr(){
        startService(Intent(this, PlayService::class.java))
    }

}