package com.example.starpin

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.starpin.common.Dialogs.PlayListDialog
import com.example.starpin.common.PlayList


import kotlinx.android.synthetic.main.main_activity.*
import kotlinx.android.synthetic.main.search_fragment.view.*
import kotlinx.android.synthetic.main.search_fragment.view.back

class SearchScreenFragment: Fragment() {
    lateinit var old_item: View

    lateinit var current_adapter: TrackAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.e("err", "124")
        val fragment_view = inflater.inflate(R.layout.search_fragment, container, false)
        fragment_view.search_tracks_view.layoutManager =
            LinearLayoutManager(
                fragment_view.search_tracks_view.context,
                LinearLayoutManager.VERTICAL,
                false
            )
//        requireActivity().bar.on_new_track = object : OnNewTrackPlay {
//            override fun OnPlay(url: String, item: View) {
//                if (::old_item.isInitialized) {
//                    old_item.loader_bar.visibility = View.INVISIBLE
//                }
//                old_item = item
//
//                item.loader_bar.visibility = View.VISIBLE
//
//                //bar.open_bar(data.name, data.artist, data.avatar, data.url)
//                val t = Thread {
//                    //music_player().play(data.url)
//                    MusicPlayer.play(track_url = url)
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

        fragment_view.back.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(requireActivity().fr.id, Screens.main_screen).commit()
            fragment_view.search_tracks_view.adapter = TrackAdapter(
                mutableListOf(),
                null
            )
            fragment_view.search_field.setText("")
        }
        fragment_view.search_button.setOnClickListener {
            val imm: InputMethodManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(fragment_view.search_field.windowToken, 0)

            fragment_view.search_field.clearFocus()
            search(fragment_view)
        }
        fragment_view.search_field.setOnKeyListener { view1, keycode, keyevent ->
            if (keyevent.keyCode == KeyEvent.KEYCODE_ENTER) {

                search(fragment_view)


                val imm: InputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(fragment_view.search_field.windowToken, 0)

                fragment_view.search_field.clearFocus()
                return@setOnKeyListener true
            }
            runOnUiThread { fragment_view.found_status.visibility = View.INVISIBLE }
            return@setOnKeyListener false
        }
        fragment_view.actions_bar.setOnMenuItemClickListener {
            Log.e("ids", it.itemId.toString())
            if (it.itemId == R.id.open_playlist_choose_window ){
                val d = PlayListDialog()
                d.on_choose = object: onClickList {
                    override fun onClick(playList: PlayList) {
                        for (track in current_adapter.getSelectedItems()) {
                            User.user_manager.addToPlayList(playList.name, track)
                            d.dismiss()

                            Toast.makeText(context, "Треки добавлены в плейлист ${playList.name}", Toast.LENGTH_SHORT).show()
                            fragment_view.actions_bar.visibility = View.GONE

                            fragment_view.search_panel.visibility = View.VISIBLE
                            current_adapter.deselectAll()

                        }
                    }
                }
                d.show(requireActivity().supportFragmentManager.beginTransaction(), "PlayListDialog")
            }
            true
        }
        fragment_view.actions_bar.setNavigationOnClickListener {

            fragment_view.actions_bar.visibility = View.GONE

            fragment_view.search_panel.visibility = View.VISIBLE
            current_adapter.deselectAll()
        }
        return fragment_view
    }

    fun runOnUiThread(code: Runnable) {
        Screens.activity.runOnUiThread(code)
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
                    current_adapter = TrackAdapter(
                        h,
                        object : OnClick {
                            override fun onClickTrack(tracks_list: MutableList<Track>, track_index: Int) {
                                Managers.musicManager.play(tracks_list, track_index)

                                //myDialogFragment.show(manager, "dialog")

                            }
                            override fun onChooseTrack(tracks_list: MutableList<Track>, track_index: Int) {
                                view.actions_bar.visibility = View.VISIBLE
                                view.search_panel.visibility = View.GONE
                            }
                            override fun onDeselctTracks(tracks_list: MutableList<Track>, track_index: Int) {
                                view.actions_bar.visibility = View.GONE
                                view.search_panel.visibility = View.VISIBLE

                            }
                        })

                    view.search_tracks_view.adapter = current_adapter


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