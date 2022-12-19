package com.example.starpin

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast

import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.starpin.common.Dialogs.ListListener

import com.example.starpin.common.Dialogs.PlayListDialog
import com.example.starpin.common.PlayList
import kotlinx.android.synthetic.main.play_list_fragment.view.*
import kotlinx.android.synthetic.main.playlist_dialog.view.*

import kotlinx.android.synthetic.main.top_fragment.view.*
import kotlinx.android.synthetic.main.top_fragment.view.actions_bar
import kotlinx.android.synthetic.main.top_fragment.view.back
import kotlinx.android.synthetic.main.top_fragment.view.connection_error
import kotlinx.android.synthetic.main.top_fragment.view.list_view
import kotlinx.android.synthetic.main.top_fragment.view.loading
import kotlinx.android.synthetic.main.top_fragment.view.navigation_bar
import kotlinx.android.synthetic.main.top_fragment.view.title
import kotlinx.android.synthetic.main.tracks_list_fragment.view.*


class TopScreenFragment(var adapter: TrackAdapter? = null, var selected: Boolean = false) :
    Fragment() {

    lateinit var fragment_view: View

    //var updateList = true
    var set_name = ""
    lateinit var currentAdapter: TrackAdapter
    var reloadData = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragment_view = inflater.inflate(R.layout.top_fragment, container, false)
        fragment_view.list_view.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        fragment_view.navigation_bar.title.text = "Лучшее" // Текст у навигационной панели

        if (adapter == null) {
            fragment_view.loading.visibility = View.VISIBLE
            val top_thread = Thread {

                val data = get_top().toMutableList()
                runOnUiThread {

                    currentAdapter = TrackAdapter(data, object : OnClick {
                        override fun onClickTrack(
                            tracks_list: MutableList<Track>,
                            track: Track
                        ) {
                            Managers.musicManager.play(tracks_list, track)
                        }

                        override fun onChooseTrack(
                            tracks_list: MutableList<Track>,
                            track: Track
                        ) {
                            fragment_view.navigation_bar.visibility = View.GONE
                            //Managers.musicManager.pause()
                            fragment_view.actions_bar.visibility = View.VISIBLE

                        }

                        override fun onDeselectTracks(
                            tracks_list: MutableList<Track>,
                            track: Track
                        ) {

                            fragment_view.navigation_bar.visibility = View.VISIBLE
                            fragment_view.actions_bar.visibility = View.GONE

                        }
                    })
                    fragment_view.list_view.adapter = currentAdapter
                    fragment_view.loading.visibility = View.GONE
                }


            }

            top_thread.start()
            top_thread.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { _, _ ->
                fragment_view.loading.visibility = View.GONE
                fragment_view.connection_error.visibility = View.VISIBLE
            }
        } else {
            currentAdapter = adapter!!
            fragment_view.list_view.adapter = currentAdapter
        }

        if (selected) {
            fragment_view.navigation_bar.visibility = View.GONE
            //Managers.musicManager.pause()
            fragment_view.actions_bar.visibility = View.VISIBLE
        }
        fragment_view.back.setOnClickListener {

            Managers.fragmentManager.goToFragment(NavigationScreenFragment())

        }
        fragment_view.actions_bar.setOnMenuItemClickListener {

            if (it.itemId == R.id.open_playlist_choose_window) {
                val d = PlayListDialog(object : ListListener {
                    override fun onClick(dialog: Dialog, playList: PlayList) {
                        for (track in currentAdapter.getSelectedTracks()) {
                            User.user_manager.addToPlayList(playList.name, track)
                            dialog.dismiss()


                            Toast.makeText(context, "Треки добавлены в плейлист ${playList.name}", Toast.LENGTH_SHORT).show()
                            fragment_view.actions_bar.visibility = View.GONE

                            fragment_view.navigation_bar.visibility = View.VISIBLE
                            currentAdapter.deselectAll()

                        }
                    }

                    override fun onCreateNew() {
                        Managers.fragmentManager.goToFragment(
                            TopScreenFragment(
                                adapter = currentAdapter,
                                selected = true
                            )
                        )
                        fragment_view.navigation_bar.visibility = View.VISIBLE
                        fragment_view.actions_bar.visibility = View.GONE

//                        dialog_fragment.list.adapter = PlayListAdapter(
//
//                            User.user_manager.createdPlayLists.values.toMutableList(),
//                            object : onClickList {
//                                override fun onClick(playList: PlayList) {
//                                    for (track in currentAdapter.getSelectedTracks()) {
//                                        User.user_manager.addToPlayList(playList.name, track)
//                                        dialog.hide()
//
//                                        val message = Toast(context)
//                                        message.setText("Треки добавлены в плейлист ${playList.name}")
//                                        message.duration = Toast.LENGTH_SHORT
//                                        fragment_view.actions_bar.visibility = View.GONE
//
//                                        fragment_view.navigation_bar.visibility = View.VISIBLE
//                                        currentAdapter.deselectAll()
//
//                                    }
//                                }},
//                            true
//
//                        )
//                        dialog.show()
                    }


                }).show(
                    requireActivity().supportFragmentManager.beginTransaction(),
                    "PlayListDialog"
                )
            } else if (it.itemId == R.id.share_music) {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND

                val tracks_names: String =
                    currentAdapter.getSelectedTracks()
                        .joinToString(separator = "\n", transform = { "${it.artist} - ${it.name}" })

                intent.putExtra(Intent.EXTRA_TEXT, tracks_names)
                intent.type = "text/plain"
                startActivity(Intent.createChooser(intent, "Поделиться музыкой"))

            }
            true
        }
        fragment_view.actions_bar.setNavigationOnClickListener {

            fragment_view.actions_bar.visibility = View.GONE
            fragment_view.navigation_bar.visibility = View.VISIBLE
            currentAdapter.deselectAll()
        }
        return fragment_view
    }


    fun runOnUiThread(code: Runnable) {
        Screens.activity.runOnUiThread(code)

    }
}