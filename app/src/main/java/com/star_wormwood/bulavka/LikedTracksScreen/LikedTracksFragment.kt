package com.star_wormwood.bulavka.LikedTracksScreen

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.star_wormwood.bulavka.*
import com.star_wormwood.bulavka.EditPlayListScreen.EditPlayListFragment
import com.star_wormwood.bulavka.EditPlayListScreen.EditPlaylistListener
import com.star_wormwood.bulavka.NavigationScreen.NavigationScreenFragment
import com.star_wormwood.bulavka.TracksInPlaylistsScreen.TracksFragment

import com.star_wormwood.bulavka.common.Adapters.OnClick
import com.star_wormwood.bulavka.common.Adapters.TrackAdapter
import com.star_wormwood.bulavka.common.Dialogs.ConfirmDialog

import com.star_wormwood.bulavka.common.Dialogs.PlayListDialog
import com.star_wormwood.bulavka.common.Items.PlayList
import com.star_wormwood.bulavka.common.Items.Track
import com.star_wormwood.bulavka.common.PlaylistListener
import com.star_wormwood.bulavka.common.PlaylistLoader

import kotlinx.android.synthetic.main.liked_fragment.view.*


class LikedTracksFragment(var adapter: TrackAdapter? = null, var selected: Boolean = false) :
    Fragment() {

    lateinit var currentPlaylist: PlayList

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragment_view = inflater.inflate(R.layout.liked_fragment, container, false)

        fragment_view.tracks_list_view.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        currentPlaylist = User.user_manager.getServicePlaylist("Понравившиеся")!!

        //if (::fragment_view.isInitialized) {

        if (adapter == null) {
            if (currentPlaylist.tracks.isNotEmpty()) {
                fragment_view.liked_tracks_status.visibility = View.GONE
                adapter = TrackAdapter(currentPlaylist.tracks.asReversed(), object : OnClick {
                    override fun onClickTrack(tracks_list: List<Track>, track: Track) {
                        Managers.musicManager.play(tracks_list, track)
                    }

                    override fun onChooseTrack(
                        tracks_list: List<Track>,
                        track: Track
                    ) {

                        //Managers.musicManager.pause()
                        fragment_view.actions_bar.visibility = View.VISIBLE
                        selected = false
                    }

                    override fun onDeselectTracks(
                        tracks_list: List<Track>,
                        track: Track
                    ) {
                        selected = false

                        fragment_view.actions_bar.visibility = View.GONE

                    }

                })
                fragment_view.tracks_list_view.adapter = adapter

            } else {
                fragment_view.liked_tracks_status.visibility = View.VISIBLE
                fragment_view.liked_tracks_status.text = "Пока здесь пусто!"
            }
        } else {

            fragment_view.tracks_list_view.adapter = adapter

        }



        if (selected) {

            //Managers.musicManager.pause()
            fragment_view.actions_bar.visibility = View.VISIBLE
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
                                        Managers.fragmentManager.goToFragment(
                                            NavigationScreenFragment(
                                                likedScreen = LikedTracksFragment(selected = true, adapter = adapter),
                                                selectedItem = R.id.liked_screen
                                            )
                                        )
                                        User.user_manager.createPlayList(name)

//                                        for (track in adapter!!.getSelectedTracks()) {
//                                            User.user_manager.addToPlayList(name, track)
//                                        }


                                    }

                                    override fun onBack() {
                                        Managers.fragmentManager.goToFragment(
                                            NavigationScreenFragment(
                                                likedScreen = LikedTracksFragment(selected = true, adapter = adapter),
                                                selectedItem = R.id.liked_screen
                                            )
                                        )
                                    }
                                })
                        )
                    }

                    override fun onClick(playList: PlayList) {

                        for (track in adapter!!.getSelectedTracks()) {
                            User.user_manager.addToPlayList(playList.name, track)
                        }
                        PlaylistLoader(
                            User.user_manager.createdPlayLists,
                            object : PlaylistListener {
                                override fun onClickAdd() {
                                    Managers.fragmentManager.goToFragment(
                                        EditPlayListFragment(
                                            EditPlayListFragment.MODE_CREATE,
                                            object : EditPlaylistListener {
                                                override fun onEdit(name: String) {
                                                    Managers.fragmentManager.goToFragment(
                                                        NavigationScreenFragment(
                                                            selectedItem = R.id.liked_screen
                                                        )
                                                    )
                                                    User.user_manager.createPlayList(name)
                                                }

                                                override fun onBack() {
                                                    Managers.fragmentManager.goToFragment(
                                                        NavigationScreenFragment(
                                                            selectedItem = R.id.liked_screen
                                                        )
                                                    )
                                                }
                                            })
                                    )
                                }

                                override fun onClick(playList: PlayList) {
                                    Managers.fragmentManager.goToFragment(TracksFragment(playList))
                                }
                            }).into(fragment_view.playlists_view)
                        Toast.makeText(
                            context,
                            "Треки добавлены в плейлист ${playList.name}",
                            Toast.LENGTH_SHORT
                        ).show()
                        fragment_view.actions_bar.visibility = View.GONE


                        adapter!!.deselectAll()

                    }
                }).show(
                    Managers.fragmentManager.supportFragmentManager!!.beginTransaction(),
                    "PlayListDialog"
                )
            } else if (it.itemId == R.id.delete_selected_tracks) {

                ConfirmDialog(
                    R.drawable.ic_baseline_delete_24,
                    "Вы действительно хотите удалить ${adapter!!.getSelectedTracks().size} трека(ов)?"
                ) {
                    for (track in adapter!!.getSelectedTracks()) {
                        User.user_manager.removeFromServicePlayList(currentPlaylist.name, track)

                    }
                    adapter!!.deselectAll()

                    fragment_view.actions_bar.visibility = View.GONE
                    Managers.fragmentManager.goToFragment(NavigationScreenFragment(selectedItem = R.id.liked_screen)) // update the screen
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


            adapter!!.deselectAll()
            selected = false
        }
        // Подключение к интернету отсутствует


//        fragment_view.playlists_view.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
//
//
//        fragment_view.playlists_view.addItemDecoration(SpaceItemDecoration(20))

        if (User.user_manager.createdPlayLists.isNotEmpty()) {

            fragment_view.playlists_status.visibility = View.GONE
            fragment_view.create_playlist.visibility = View.VISIBLE
            PlaylistLoader(User.user_manager.createdPlayLists, object : PlaylistListener {
                override fun onClickAdd() {
                    Managers.fragmentManager.goToFragment(
                        EditPlayListFragment(
                            EditPlayListFragment.MODE_CREATE, object : EditPlaylistListener {
                                override fun onEdit(name: String) {
                                    User.user_manager.createPlayList(name)

                                    Managers.fragmentManager.goToFragment(
                                        NavigationScreenFragment(
                                            selectedItem = R.id.liked_screen,
                                            likedScreen = LikedTracksFragment(
                                            )
                                        )
                                    )

                                }

                                override fun onBack() {
                                    Managers.fragmentManager.goToFragment(
                                        NavigationScreenFragment(
                                            selectedItem = R.id.liked_screen
                                        )
                                    )
                                }
                            })
                    )
                }

                override fun onClick(playList: PlayList) {
                    Managers.fragmentManager.goToFragment(TracksFragment(playList))
                }
            }).into(fragment_view.playlists_view)
//            fragment_view.playlists_view.adapter = PlayListAdapter(
//                User.user_manager.createdPlayLists,
//                object : onClickList {
//                    override fun onClick(playList: PlayList) {
//                        Log.e("play", playList.toString())
//                        Managers.fragmentManager.goToFragment(TracksFragment(playList))
//
//                    }
//
//                },
//                true
//            )
        } else {
            fragment_view.create_playlist.visibility = View.GONE
            fragment_view.playlists_status.visibility = View.VISIBLE
        }

        fragment_view.playlists_status.setOnClickListener {
            Managers.fragmentManager.goToFragment(
                EditPlayListFragment(
                    EditPlayListFragment.MODE_CREATE, object : EditPlaylistListener {
                        override fun onEdit(name: String) {
                            Managers.fragmentManager.goToFragment(
                                NavigationScreenFragment(
                                    selectedItem = R.id.liked_screen
                                )
                            )
                            User.user_manager.createPlayList(name)
                            for (track in adapter!!.getSelectedTracks()) {
                                User.user_manager.addToPlayList(name, track)
                            }

                            Toast.makeText(
                                context,
                                "Треки добавлены в плейлист $name",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onBack() {
                            Managers.fragmentManager.goToFragment(
                                NavigationScreenFragment(
                                    selectedItem = R.id.liked_screen
                                )
                            )
                        }
                    })
            )
        }
        fragment_view.create_playlist.setOnClickListener {
            Managers.fragmentManager.goToFragment(
                EditPlayListFragment(
                    EditPlayListFragment.MODE_CREATE, object : EditPlaylistListener {
                        override fun onEdit(name: String) {
                            Managers.fragmentManager.goToFragment(
                                NavigationScreenFragment(
                                    selectedItem = R.id.liked_screen
                                )
                            )
                            User.user_manager.createPlayList(name)
                            for (track in adapter!!.getSelectedTracks()) {
                                User.user_manager.addToPlayList(name, track)
                            }

                            Toast.makeText(
                                context,
                                "Треки добавлены в плейлист $name",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onBack() {
                            Managers.fragmentManager.goToFragment(
                                NavigationScreenFragment(
                                    selectedItem = R.id.liked_screen
                                )
                            )
                        }
                    })
            )
        }


        return fragment_view
    }


    fun runOnUiThread(code: Runnable) {
        Screens.activity.runOnUiThread(code)

    }
}