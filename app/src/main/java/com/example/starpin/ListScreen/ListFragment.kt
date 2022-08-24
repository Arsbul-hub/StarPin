package com.example.starpin

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.list_fragment.view.*
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.track_item.view.*


interface Load {
    fun load_list_items(fragment_view: View)
}

class ListFragment : Fragment() {

    lateinit var fragment_view: View
    lateinit var load_list: Load
    var old_adapter = TrackAdapter(mutableListOf(), object : OnClick {
        override fun onClickTrack(
            tracks_list: MutableList<Track>,
            track_index: Int,
            item: View
        ) {
        }
    })

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragment_view = inflater.inflate(R.layout.list_fragment, container, false)
        fragment_view.list_view.layoutManager =
            LinearLayoutManager(
                fragment_view.list_view.context,
                LinearLayoutManager.VERTICAL,
                false
            )


        fragment_view.back.setOnClickListener {
            //requireActivity(
            requireActivity().fragment_view.setCurrentItem(0, true)

        }


        return fragment_view
    }

    override fun onResume() {
        super.onResume()
        if (::load_list.isInitialized) {
//            fragment_view.list_view.layoutManager = LinearLayoutManager(fragment_view.list_view.context, LinearLayoutManager.VERTICAL, false)
//            fragment_view.list_view.adapter =


            load_list.load_list_items(fragment_view)

        }
    }

        override fun onPause() {
        super.onPause()

        fragment_view.loading.visibility = View.GONE
        fragment_view.list_view.visibility = View.GONE
        fragment_view.connection_error.visibility = View.GONE
    }
    fun runOnUiThread(code: Runnable) {
        requireActivity().runOnUiThread(code)

    }
}