package com.example.starpin

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.starpin.EditPlayListScreen.EditPlayListFragment
import com.example.starpin.common.Dialogs.ConfirmDialog
import com.example.starpin.common.Dialogs.ListListener
import com.example.starpin.common.Dialogs.PlayListDialog
import com.example.starpin.common.PlayList
import kotlinx.android.synthetic.main.tracks_list_fragment.view.*
import kotlinx.android.synthetic.main.tracks_list_fragment.view.actions_bar
import kotlinx.android.synthetic.main.tracks_list_fragment.view.back
import kotlinx.android.synthetic.main.tracks_list_fragment.view.list_view
import kotlinx.android.synthetic.main.tracks_list_fragment.view.navigation_bar


class TracksFragment(
    val playlist: PlayList,
    var adapter: TrackAdapter? = null,
    var selected: Boolean = false
) : Fragment() {

    lateinit var fragment_view: View

    var set_name = ""
    lateinit var currentAdapter: TrackAdapter
    lateinit var current_playlist: PlayList
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        fragment_view = inflater.inflate(R.layout.tracks_list_fragment, container, false)

        fragment_view.list_view.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        fragment_view.navigation_bar.title.text = playlist.name // Текст у навигационной панели
        if (adapter == null) {
            if (playlist.tracks.isNotEmpty()) {
                fragment_view.empty_status.visibility = View.GONE
                currentAdapter = TrackAdapter(playlist.tracks, object : OnClick {
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
        fragment_view.navigation_bar.back.setOnClickListener {

            Managers.fragmentManager.goToFragment(PlayListsFragment())

        }
        fragment_view.navigation_bar.edit_playlist.setOnClickListener {

            Managers.fragmentManager.goToFragment(EditPlayListFragment(EditPlayListFragment.MODE_EDIT) {


                User.user_manager.createPlayList(
                    it,
                    User.user_manager.createdPlayLists[it]!!.tracks
                )

                User.user_manager.removePlaylist(playlist.name)
                Managers.fragmentManager.goToFragment(TracksFragment(User.user_manager.createdPlayLists[it]!!))
            })

        }
        fragment_view.navigation_bar.remove_playlist.setOnClickListener {
            ConfirmDialog(
                R.drawable.ic_baseline_delete_24,
                "Вы действительно хотите удалить плейлист ${playlist.name}?"
            ) {
                User.user_manager.removePlaylist(playlist.name)
                Managers.fragmentManager.goToFragment(PlayListsFragment())
                Toast.makeText(context, "Плейлист ${playlist.name} удалён", Toast.LENGTH_SHORT)
                    .show()
            }.show(requireActivity().supportFragmentManager.beginTransaction(), "ConfirmDialog")

        }
        // Action Bar

        fragment_view.actions_bar.menu.findItem(R.id.delete_selected_tracks).isVisible = true
        fragment_view.actions_bar.setOnMenuItemClickListener {

            if (it.itemId == R.id.open_playlist_choose_window) {
                val d = PlayListDialog(object : ListListener {
                    override fun onClick(dialog: Dialog, playList: PlayList) {
                        for (track in currentAdapter.getSelectedTracks()) {
                            User.user_manager.addToPlayList(playList.name, track)
                            dialog.dismiss()
                            Toast.makeText(
                                context,
                                "Треки добавлены в плейлист ${playList.name}",
                                Toast.LENGTH_SHORT
                            ).show()
                            //Toast.makeText(context, "Треки добавлены в плейлист ${playList.name}", Toast.LENGTH_SHORT).show()
                            fragment_view.actions_bar.visibility = View.GONE

                            fragment_view.navigation_bar.visibility = View.VISIBLE
                            currentAdapter.deselectAll()

                        }
                    }

                    override fun onCreateNew() {
                        Managers.fragmentManager.goToFragment(
                            TracksFragment(
                                playlist = playlist,
                                adapter = currentAdapter,
                                selected = true
                            )
                        )
                        fragment_view.navigation_bar.visibility = View.VISIBLE
                        fragment_view.actions_bar.visibility = View.GONE
                        //dialog.show()
                    }

                }).show(
                    requireActivity().supportFragmentManager.beginTransaction(),
                    "PlayListDialog"
                )
            } else if (it.itemId == R.id.delete_selected_tracks) {

                ConfirmDialog(
                    R.drawable.ic_baseline_delete_24,
                    "Вы действительно хотите удалить ${currentAdapter.getSelectedTracks().size} трека(ов)?"
                ) {
                    for (track in currentAdapter.getSelectedTracks()) {
                        User.user_manager.removeFromPlayList(playlist.name, track)

                    }
                    currentAdapter.deselectAll()
                    fragment_view.navigation_bar.visibility = View.VISIBLE
                    fragment_view.actions_bar.visibility = View.GONE

                    Managers.fragmentManager.goToFragment(TracksFragment(playlist)) // update the screen

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