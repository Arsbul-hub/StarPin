package com.star_wormwood.bulavka.TracksInPlaylistsScreen

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.star_wormwood.bulavka.*
import com.star_wormwood.bulavka.EditPlayListScreen.EditPlayListFragment
import com.star_wormwood.bulavka.EditPlayListScreen.EditPlaylistListener

import com.star_wormwood.bulavka.LikedTracksScreen.LikedTracksFragment
import com.star_wormwood.bulavka.NavigationScreen.NavigationScreenFragment
import com.star_wormwood.bulavka.common.Adapters.OnClick
import com.star_wormwood.bulavka.common.Adapters.TrackAdapter
import com.star_wormwood.bulavka.common.Dialogs.ConfirmDialog

import com.star_wormwood.bulavka.common.Dialogs.PlayListDialog
import com.star_wormwood.bulavka.common.Items.PlayList
import com.star_wormwood.bulavka.common.Items.Track
import com.star_wormwood.bulavka.common.PlaylistListener
import kotlinx.android.synthetic.main.tracks_list_fragment.view.*
import kotlinx.android.synthetic.main.tracks_list_fragment.view.actions_bar
import kotlinx.android.synthetic.main.tracks_list_fragment.view.back
import kotlinx.android.synthetic.main.tracks_list_fragment.view.tracks_list_view
import kotlinx.android.synthetic.main.tracks_list_fragment.view.navigation_bar


class TracksFragment(
    var playList: PlayList,
    var adapter: TrackAdapter? = null,

    var selected: Boolean = false
) : Fragment() {

    lateinit var fragment_view: View

    var set_name = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragment_view = inflater.inflate(R.layout.tracks_list_fragment, container, false)

        fragment_view.tracks_list_view.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        fragment_view.navigation_bar.title.text = playList.name // Текст у навигационной панели
        if (adapter == null) {
            if (playList.tracks.isNotEmpty()) {
                fragment_view.empty_status.visibility = View.GONE
                adapter = TrackAdapter(playList.tracks.asReversed(), object : OnClick {
                    override fun onClickTrack(tracks_list: List<Track>, track: Track) {
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
            } else {
                fragment_view.empty_status.visibility = View.VISIBLE
            }
        } else {

            fragment_view.tracks_list_view.adapter = adapter
        }

        if (selected) {

            fragment_view.navigation_bar.visibility = View.GONE
            //Managers.musicManager.pause()
            fragment_view.actions_bar.visibility = View.VISIBLE
        }
        fragment_view.navigation_bar.back.setOnClickListener {

            Managers.fragmentManager.goToFragment(
                NavigationScreenFragment(
                    likedScreen = LikedTracksFragment(),
                    selectedItem = R.id.liked_screen
                )
            )

        }
        fragment_view.navigation_bar.edit_playlist.setOnClickListener {

            Managers.fragmentManager.goToFragment(
                EditPlayListFragment(
                    EditPlayListFragment.MODE_EDIT, object : EditPlaylistListener {
                        override fun onEdit(name: String) {


                            Managers.fragmentManager.goToPrevious()
                        }

                        override fun onBack() {
                            Managers.fragmentManager.goToPrevious()
                        }
                    }, oldName = playList.name
                )
            )

        }
        fragment_view.navigation_bar.remove_playlist.setOnClickListener {
            ConfirmDialog(
                R.drawable.ic_baseline_delete_24,
                "Вы действительно хотите удалить плейлист ${playList.name}?"
            ) {
                User.user_manager.removePlaylist(playList.name)
                Managers.fragmentManager.goToFragment(
                    NavigationScreenFragment(
                        likedScreen = LikedTracksFragment(),
                        selectedItem = R.id.liked_screen
                    )
                )

                Toast.makeText(context, "Плейлист ${playList.name} удалён", Toast.LENGTH_SHORT)
                    .show()
            }.show(requireActivity().supportFragmentManager.beginTransaction(), "ConfirmDialog")

        }
        // Action Bar

        fragment_view.actions_bar.menu.findItem(R.id.delete_selected_tracks).isVisible = true
        fragment_view.actions_bar.setOnMenuItemClickListener {

            if (it.itemId == R.id.open_playlist_choose_window) {
                PlayListDialog(object : PlaylistListener {
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
                        for (track in adapter?.getSelectedTracks()!!) {
                            User.user_manager.addToPlayList(playList.name, track)

                            Toast.makeText(
                                context,
                                "Треки добавлены в плейлист ${playList.name}",
                                Toast.LENGTH_SHORT
                            ).show()
                            //Toast.makeText(context, "Треки добавлены в плейлист ${playList.name}", Toast.LENGTH_SHORT).show()
                            fragment_view.actions_bar.visibility = View.GONE

                            fragment_view.navigation_bar.visibility = View.VISIBLE
                            adapter?.deselectAll()

                        }
                    }


                }).show(
                    requireActivity().supportFragmentManager.beginTransaction(),
                    "PlayListDialog"
                )
            } else if (it.itemId == R.id.delete_selected_tracks) {

                ConfirmDialog(
                    R.drawable.ic_baseline_delete_24,
                    "Вы действительно хотите удалить ${adapter!!.getSelectedTracks().size} трека(ов)?"
                ) {
                    for (track in adapter!!.getSelectedTracks()) {
                        User.user_manager.removeFromPlayList(playList.name, track)

                    }
                    adapter!!.deselectAll()
                    fragment_view.navigation_bar.visibility = View.VISIBLE
                    fragment_view.actions_bar.visibility = View.GONE

                    Managers.fragmentManager.goToFragment(TracksFragment(playList)) // update the screen

                }.show(requireActivity().supportFragmentManager.beginTransaction(), "ConfirmDialog")


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
            selected = false
            adapter!!.deselectAll()
        }
        return fragment_view
    }


    fun runOnUiThread(code: Runnable) {
        Screens.activity.runOnUiThread(code)

    }

}