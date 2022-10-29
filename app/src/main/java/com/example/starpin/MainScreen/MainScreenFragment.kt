package com.example.starpin

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment


import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.view.*


class MainScreenFragment : Fragment() {
    lateinit var old_item: View


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        view.to_search.setOnClickListener {
            Managers.fragmentManager.goToFragment(Screens.search_screen)

        }

        view.to_top.setOnClickListener {
//            screens.list_screen.load_list = object : Load {
//                override fun load_list_items(fragment_view: View) {
//                    TracksInTop().load(fragment_view, requireActivity())
//
//                }
//
//            }

            Managers.fragmentManager.goToFragment(TopScreenFragment())





        }
        view.to_user_play_lists.setOnClickListener {


            Managers.fragmentManager.goToFragment(PlayListsFragment())


        }
        view.to_liked.setOnClickListener {
            Managers.fragmentManager.goToFragment(LikedTracksFragment())

        }
        return view
    }

    fun runOnUiThread(code: Runnable) {
        Screens.activity.runOnUiThread(code)
    }

}