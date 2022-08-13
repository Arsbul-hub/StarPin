package com.example.starpin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager

import kotlinx.android.synthetic.main.music_item.view.*
import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.main_fragment.view.*

class MainScreenFragment: Fragment() {
    lateinit var old_item: View


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.main_fragment, container, false)
        view.top_tracks_view.layoutManager =
            LinearLayoutManager(view.top_tracks_view.context, LinearLayoutManager.VERTICAL, false)


        view.to_search.setOnClickListener {
            requireActivity().fragment_view.setCurrentItem(1, true)
        }
        requireActivity().bar.on_new_track = object : OnNewTrackPlay {
            override fun OnPlay(url: String, item: View) {
                if (::old_item.isInitialized) {
                    old_item.loader_bar.visibility = View.INVISIBLE
                }
                old_item = item

                item.loader_bar.visibility = View.VISIBLE

                //bar.open_bar(data.name, data.artist, data.avatar, data.url)
                Thread {
                    //music_player().play(data.url)
                    MusicPlayer.play(track_url = url)
                    playing_state = true
                    runOnUiThread {
                        item.loader_bar.visibility = View.INVISIBLE

                    }
                }.start()
            }
        }

        view.top_loading.visibility = View.VISIBLE
        view.top_tracks_view.visibility = View.GONE
        view.connection_error_top.visibility = View.GONE


        val lt = Thread {
            val p = get_top().toMutableList()
            runOnUiThread {
                view.top_tracks_view.adapter = TrackAdapter(p, object : OnClick {
                    override fun onClickTrack(data: Track, item: View) {
                        requireActivity().bar.open_bar(data.name, data.artist, data.avatar, data.url, item)
                    }

                })
                view.top_tracks_view.visibility = View.VISIBLE
                view.top_loading.visibility = View.GONE

            }
        }
        lt.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { p0, p1 ->
            view.connection_error_top.visibility = View.VISIBLE
            view.top_loading.visibility = View.GONE
        }
        lt.start()
        return view
    }
    fun runOnUiThread(code: Runnable) {
        requireActivity().runOnUiThread(code)
    }
}