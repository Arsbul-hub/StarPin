package com.star_wormwood.bulavka.MainScreen

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.star_wormwood.bulavka.*
import com.star_wormwood.bulavka.SearchScreen.SearchScreenFragment
import com.star_wormwood.bulavka.TopScreen.TopScreenFragment


import kotlinx.android.synthetic.main.main_screen_fragment.view.*


class MainScreenFragment : Fragment() {
    lateinit var old_item: View


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_screen_fragment, container, false)
        view.to_search.setOnClickListener {
            Managers.fragmentManager.goToFragment(SearchScreenFragment())

        }
        view.to_top_music.setOnClickListener {
            Managers.fragmentManager.goToFragment(TopScreenFragment())
        }


        view.to_playlist_on_day.setOnClickListener {
            Toast.makeText(context, "Пока в разработке", Toast.LENGTH_SHORT).show()
        }
        view.to_starting_day.setOnClickListener {
            Toast.makeText(context, "Пока в разработке", Toast.LENGTH_SHORT).show()
        }
        view.to_unknown_music.setOnClickListener {
            Toast.makeText(context, "Пока в разработке", Toast.LENGTH_SHORT).show()
        }
//        view.buttons_view.layoutManager =
//            GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
//        val list = arrayListOf<ScreenItem>(
//            ScreenItem(image = R.drawable.top) {
//                Managers.fragmentManager.goToFragment(
//                    TopScreenFragment()
//                )
//            }
//        )

        //view.buttons_view.adapter = MainScreenAdapter(list)
//        view.to_user_play_lists.setOnClickListener {
//
//
//            Managers.fragmentManager.goToFragment(PlayListsFragment())
//
//
//        }
//        view.to_liked.setOnClickListener {
//            Managers.fragmentManager.goToFragment(LikedTracksFragment())
//
//        }
        return view
    }

    fun runOnUiThread(code: Runnable) {
        Screens.activity.runOnUiThread(code)
    }

}