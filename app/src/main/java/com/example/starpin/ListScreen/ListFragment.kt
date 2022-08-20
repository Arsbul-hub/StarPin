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
    lateinit var old_item: View
    lateinit var fragment_view: View
    lateinit var load_list: Load
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragment_view = inflater.inflate(R.layout.list_fragment, container, false)
        fragment_view.list_view.layoutManager =
            LinearLayoutManager(fragment_view.list_view.context, LinearLayoutManager.VERTICAL, false)


        fragment_view.back.setOnClickListener {
            //requireActivity(
            requireActivity().fragment_view.setCurrentItem(0, true)

        }
        requireActivity().bar.on_new_track = object : OnNewTrackPlay {
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

        return fragment_view
    }

    override fun onResume() {
        super.onResume()
        if (::load_list.isInitialized) {
            load_list.load_list_items(fragment_view)
        }
    }

    override fun onPause() {
        super.onPause()

        fragment_view.loading.visibility = View.VISIBLE
        fragment_view.list_view.visibility = View.GONE
        fragment_view.connection_error.visibility = View.GONE
    }
    fun runOnUiThread(code: Runnable) {
        requireActivity().runOnUiThread(code)

    }
}