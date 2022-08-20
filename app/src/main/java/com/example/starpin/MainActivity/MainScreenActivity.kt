package com.example.starpin


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

import com.example.starpin.common.Adapters.FragmentAdapter


import kotlinx.android.synthetic.main.main_activity.*


object User {
    val user_manager = UserManager()

}
object screens {
    val main_screen = MainScreenFragment()
    val search_screen = SearchScreenFragment()
    val list_screen = ListFragment()
}
class MainScreenActivity : AppCompatActivity() {
    lateinit var old_item: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        //
        //Log.e("11222", play_lists.toString())
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



        //fragment_view.setTag(4, "tracks")

        fragment_view.isUserInputEnabled = false
    }

}