package com.example.starpin

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import com.example.starpin.common.Dialogs.PlayListDialog
import com.example.starpin.common.PlayList

import kotlinx.android.synthetic.main.list_fragment.*


import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.view.*
import kotlinx.android.synthetic.main.track_info_bar.view.*


class MainScreenFragment : Fragment() {
    lateinit var old_item: View


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        view.to_search.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(requireActivity().fr.id, Screens.search_screen).commit()

        }

        view.to_top.setOnClickListener {
//            screens.list_screen.load_list = object : Load {
//                override fun load_list_items(fragment_view: View) {
//                    TracksInTop().load(fragment_view, requireActivity())
//
//                }
//
//            }

            requireActivity().supportFragmentManager.beginTransaction().replace(requireActivity().fr.id, Screens.top_screen).commit()
            Screens.top_screen.loadList()




        }
        view.to_user_play_lists.setOnClickListener {


            requireActivity().supportFragmentManager.beginTransaction().replace(requireActivity().fr.id, Screens.playlists_screen).commit()
            Screens.playlists_screen.loadList()

        }
        return view
    }

    fun runOnUiThread(code: Runnable) {
        Screens.activity.runOnUiThread(code)
    }

}