package com.example.starpin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.starpin.Loader.TracksInTop
import com.example.starpin.Loader.UserPlayLists

import com.example.starpin.common.PlayList
import kotlinx.android.synthetic.main.list_fragment.view.*


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
            requireActivity().fragment_view.setCurrentItem(1, true)
        }
        view.to_top.setOnClickListener {
            screens.list_screen.load_list = object : Load {
                override fun load_list_items(fragment_view: View) {
                    TracksInTop().load(fragment_view, requireActivity())

                }

            }
            requireActivity().fragment_view.setCurrentItem(2, true)

        }
        view.to_user_play_lists.setOnClickListener {
            screens.list_screen.load_list = object : Load {
                override fun load_list_items(fragment_view: View) {
                    UserPlayLists().load(fragment_view, requireActivity())

                }

            }
            requireActivity().fragment_view.setCurrentItem(2, true)
        }
        return view
    }
}