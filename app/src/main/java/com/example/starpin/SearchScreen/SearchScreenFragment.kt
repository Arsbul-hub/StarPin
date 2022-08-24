package com.example.starpin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.track_item.view.*

import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.search_fragment.view.*

class SearchScreenFragment: Fragment() {
    lateinit var old_item: View


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("err", "124")
        val view = inflater.inflate(R.layout.search_fragment, container, false)
        view.search_tracks_view.layoutManager =
            LinearLayoutManager(
                view.search_tracks_view.context,
                LinearLayoutManager.VERTICAL,
                false
            )
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

        view.back.setOnClickListener {
            requireActivity().fragment_view.setCurrentItem(0)
            view.search_tracks_view.adapter = TrackAdapter(
                mutableListOf(),
                null
            )
            view.search_field.setText("")
        }
        view.search_button.setOnClickListener {
            val imm: InputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.search_field.windowToken, 0)

            view.search_field.clearFocus()
            search(view)
        }
        view.search_field.setOnKeyListener { view1, keycode, keyevent ->
            if (keyevent.keyCode == KeyEvent.KEYCODE_ENTER) {

                search(view)


                val imm: InputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.search_field.windowToken, 0)

                view.search_field.clearFocus()
                return@setOnKeyListener true
            }
            runOnUiThread { view.found_status.visibility = View.INVISIBLE }
            return@setOnKeyListener false
        }

        return view
    }

    fun runOnUiThread(code: Runnable) {
        requireActivity().runOnUiThread(code)
    }

    fun search(view: View) {

        //view.search_loading.visibility = View.VISIBLE
        view.found_status.visibility = View.GONE
        view.connection_error_search.visibility = View.GONE
        view.search_tracks_view.visibility = View.GONE
        view.search_loading.visibility = View.VISIBLE


        val lt = Thread {
            val h = search_music(view.search_field.text.toString()).toMutableList()

            if (h.size != 0) {
                runOnUiThread {
                    view.search_tracks_view.adapter = TrackAdapter(
                        h,
                        object : OnClick {
                            override fun onClickTrack(tracks_list: MutableList<Track>, track_index: Int, item: View) {
                                requireActivity().bar.open(tracks_list, track_index, item)
                            }
                        }
                    )


                    view.search_tracks_view.visibility = View.VISIBLE
                }
            } else {
                runOnUiThread { view.found_status.visibility = View.VISIBLE }
            }
            runOnUiThread { view.search_loading.visibility = View.GONE }
        }
        lt.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { p1, p2 ->

            view.search_loading.visibility = View.GONE
            view.connection_error_search.visibility = View.VISIBLE


        }
        lt.start()


    }
}