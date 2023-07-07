package com.star_wormwood.bulavka.TopScreen

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast

import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.star_wormwood.bulavka.*
import com.star_wormwood.bulavka.EditPlayListScreen.EditPlayListFragment
import com.star_wormwood.bulavka.EditPlayListScreen.EditPlaylistListener
import com.star_wormwood.bulavka.NavigationScreen.NavigationScreenFragment
import com.star_wormwood.bulavka.common.Adapters.OnClick
import com.star_wormwood.bulavka.common.Adapters.TrackAdapter


import com.star_wormwood.bulavka.common.Dialogs.PlayListDialog
import com.star_wormwood.bulavka.common.Items.PlayList
import com.star_wormwood.bulavka.common.Items.Track
import com.star_wormwood.bulavka.common.PlaylistListener
import com.star_wormwood.bulavka.common.get_top

import kotlinx.android.synthetic.main.top_fragment.view.actions_bar
import kotlinx.android.synthetic.main.top_fragment.view.back
import kotlinx.android.synthetic.main.top_fragment.view.connection_error
import kotlinx.android.synthetic.main.top_fragment.view.tracks_list_view
import kotlinx.android.synthetic.main.top_fragment.view.loading
import kotlinx.android.synthetic.main.top_fragment.view.navigation_bar
import kotlinx.android.synthetic.main.top_fragment.view.title


class TopScreenFragment(var adapter: TrackAdapter? = null, var selected: Boolean = false) :
    Fragment() {

    lateinit var fragment_view: View

    //var updateList = true
    var set_name = ""

    var reloadData = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragment_view = inflater.inflate(R.layout.top_fragment, container, false)
        fragment_view.tracks_list_view.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        fragment_view.navigation_bar.title.text = "Лучшее" // Текст у навигационной панели

        if (adapter == null) {
            fragment_view.loading.visibility = View.VISIBLE
            val top_thread = Thread {

                val data = get_top().toMutableList()
                runOnUiThread {

                    adapter = TrackAdapter(data, object : OnClick {
                        override fun onClickTrack(
                            tracks_list: List<Track>,
                            track: Track
                        ) {
                            Managers.musicManager.play(tracks_list, track)
                        }

                        override fun onChooseTrack(
                            tracks_list: List<Track>,
                            track: Track
                        ) {
                            fragment_view.navigation_bar.visibility = View.GONE
                            //Managers.musicManager.pause()
                            fragment_view.actions_bar.visibility = View.VISIBLE
                            selected = true

                        }

                        override fun onDeselectTracks(
                            tracks_list: List<Track>,
                            track: Track
                        ) {

                            fragment_view.navigation_bar.visibility = View.VISIBLE
                            fragment_view.actions_bar.visibility = View.GONE
                            selected = false

                        }
                    })
                    fragment_view.tracks_list_view.adapter = adapter
                    fragment_view.loading.visibility = View.GONE
                }


            }


            top_thread.uncaughtExceptionHandler = Thread.UncaughtExceptionHandler { p0, p1 ->
                fragment_view.connection_error.visibility = View.VISIBLE
                fragment_view.loading.visibility = View.GONE
            }
            top_thread.start()
        } else {

            fragment_view.tracks_list_view.adapter = adapter
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
                val d = PlayListDialog(object : PlaylistListener {
                    override fun onClickAdd() {
                        Managers.fragmentManager.goToFragment(
                            EditPlayListFragment(
                                EditPlayListFragment.MODE_CREATE, object : EditPlaylistListener {
                                    override fun onEdit(name: String) {

                                        Managers.fragmentManager.goToPrevious()
                                    }

                                    override fun onBack() {
                                        Managers.fragmentManager.goToPrevious()
                                    }
                                })
                        )
                    }

                    override fun onClick(playList: PlayList) {
                        for (track in adapter!!.getSelectedTracks()) {
                            User.user_manager.addToPlayList(playList.name, track)

                            Toast.makeText(
                                context,
                                "Треки добавлены в плейлист ${playList.name}",
                                Toast.LENGTH_SHORT
                            ).show()
                            //Toast.makeText(context, "Треки добавлены в плейлист ${playList.name}", Toast.LENGTH_SHORT).show()
                            fragment_view.actions_bar.visibility = View.GONE

                            fragment_view.navigation_bar.visibility = View.VISIBLE
                            adapter!!.deselectAll()

                        }
                    }


                }).show(
                    requireActivity().supportFragmentManager.beginTransaction(),
                    "PlayListDialog"
                )
            } else if (it.itemId == R.id.share_music) {
                val intent = Intent()
                intent.action = Intent.ACTION_SEND

                val tracks_names: String =
                    adapter!!.getSelectedTracks()
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
            adapter!!.deselectAll()
            selected = false
        }
        return fragment_view
    }


    fun runOnUiThread(code: Runnable) {
        Screens.activity.runOnUiThread(code)

    }
}