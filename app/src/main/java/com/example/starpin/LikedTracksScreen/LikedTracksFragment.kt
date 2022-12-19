package com.example.starpin

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.starpin.common.Dialogs.ConfirmDialog
import com.example.starpin.common.Dialogs.ListListener
import com.example.starpin.common.Dialogs.PlayListDialog
import com.example.starpin.common.PlayList

import kotlinx.android.synthetic.main.tracks_list_fragment.view.*
import kotlinx.android.synthetic.main.tracks_list_fragment.view.actions_bar
import kotlinx.android.synthetic.main.tracks_list_fragment.view.back
import kotlinx.android.synthetic.main.tracks_list_fragment.view.empty_status
import kotlinx.android.synthetic.main.tracks_list_fragment.view.list_view
import kotlinx.android.synthetic.main.tracks_list_fragment.view.navigation_bar
import kotlinx.android.synthetic.main.tracks_list_fragment.view.title


class LikedTracksFragment(var adapter: TrackAdapter? = null, var selected: Boolean = false) :
    Fragment() {
    lateinit var currentAdapter: TrackAdapter
    lateinit var currentPlaylist: PlayList
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragment_view = inflater.inflate(R.layout.tracks_list_fragment, container, false)

        fragment_view.list_view.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        currentPlaylist = User.user_manager.servicePlayLists["Понравившиеся"]!!

        //if (::fragment_view.isInitialized) {

        if (adapter == null) {
            if (User.user_manager.servicePlayLists["Понравившиеся"]!!.tracks.isNotEmpty()) {
                fragment_view.empty_status.visibility = View.GONE
                currentAdapter = TrackAdapter(currentPlaylist.tracks, object : OnClick {
                    override fun onClickTrack(tracks_list: MutableList<Track>, track: Track) {
                        Managers.musicManager.play(tracks_list, track)
                    }

                    override fun onChooseTrack(
                        tracks_list: MutableList<Track>,
                        track: Track
                    ) {
                        fragment_view.navigation_bar.visibility = View.GONE
                        //Managers.musicManager.pause()
                        fragment_view.actions_bar.visibility = View.VISIBLE
                        selected = false
                    }

                    override fun onDeselectTracks(
                        tracks_list: MutableList<Track>,
                        track: Track
                    ) {
                        selected = false
                        fragment_view.navigation_bar.visibility = View.VISIBLE
                        fragment_view.actions_bar.visibility = View.GONE

                    }

                })
                fragment_view.list_view.adapter = currentAdapter

            } else {
                fragment_view.empty_status.visibility = View.VISIBLE
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
        fragment_view.edit_playlist.visibility = View.GONE
        fragment_view.navigation_bar.title.text = currentPlaylist.name
        fragment_view.navigation_bar.remove_playlist.visibility = View.GONE
        fragment_view.navigation_bar.back.visibility = View.GONE

        // Action Bar

        fragment_view.actions_bar.menu.findItem(R.id.delete_selected_tracks).isVisible = true
        fragment_view.actions_bar.setOnMenuItemClickListener {

            if (it.itemId == R.id.open_playlist_choose_window) {
                PlayListDialog(object : ListListener {
                    override fun onClick(dialog: Dialog, playList: PlayList) {
                        for (track in currentAdapter.getSelectedTracks()) {
                            User.user_manager.addToPlayList(playList.name, track)
                            dialog.dismiss()

                            Toast.makeText(
                                context,
                                "Треки добавлены в плейлист ${playList.name}",
                                Toast.LENGTH_SHORT
                            ).show()
                            fragment_view.actions_bar.visibility = View.GONE

                            fragment_view.navigation_bar.visibility = View.VISIBLE
                            currentAdapter.deselectAll()

                        }
                    }

                    override fun onCreateNew() {
                        Managers.fragmentManager.goToFragment(
                            NavigationScreenFragment(
                                likedScreen = LikedTracksFragment(
                                    adapter = currentAdapter,
                                    selected = true
                                ),
                                selectedItem = R.id.liked_screen

                            )

                        )


                        fragment_view.navigation_bar.visibility = View.VISIBLE
                        fragment_view.actions_bar.visibility = View.GONE


                    }

                }).show(
                    Managers.fragmentManager.supportFragmentManager!!.beginTransaction(),
                    "PlayListDialog"
                )
            } else if (it.itemId == R.id.delete_selected_tracks) {

                ConfirmDialog(
                    R.drawable.ic_baseline_delete_24,
                    "Вы действительно хотите удалить ${currentAdapter.getSelectedTracks().size} трека(ов)?"
                ) {
                    for (track in currentAdapter.getSelectedTracks()) {
                        User.user_manager.removeFromServicePlayList(currentPlaylist.name, track)

                    }
                    currentAdapter.deselectAll()
                    fragment_view.navigation_bar.visibility = View.VISIBLE
                    fragment_view.actions_bar.visibility = View.GONE
                    Managers.fragmentManager.goToFragment(NavigationScreenFragment(selectedItem = R.id.liked_screen)) // update the screen
                }.show(requireActivity().supportFragmentManager.beginTransaction(), "ConfirmDialog")


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