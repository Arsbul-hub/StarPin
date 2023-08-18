package com.star_wormwood.bulavka.common.Dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager

import com.star_wormwood.bulavka.R
import com.star_wormwood.bulavka.User
import com.star_wormwood.bulavka.common.Adapters.PlayListAdapter
import com.star_wormwood.bulavka.common.Adapters.onClickList
import com.star_wormwood.bulavka.common.Decorators.SpaceItemDecoration
import com.star_wormwood.bulavka.common.Items.PlayList
import com.star_wormwood.bulavka.common.PlaylistListener


import kotlinx.android.synthetic.main.playlist_dialog.view.*
import kotlinx.android.synthetic.main.playlist_dialog.view.playlists_status

interface PlaylistDialogListener {
    fun onClick(dialog: Dialog, playList: PlayList)
    fun onCreateNew(newPlayList: PlayList, dialog: Dialog)


}

class PlayListDialog(var playlistListener: PlaylistListener) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialog_view = inflater.inflate(R.layout.playlist_dialog, container, false)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        //dialog_view.list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

//        dialog_view.list.addItemDecoration(SpaceItemDecoration(20))
//
//
//        dialog_view.list.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
//
//        dialog_view.list.adapter = PlayListAdapter(
//
//            User.user_manager.createdPlayLists,
//            object : onClickList {
//                override fun onClick(playList: PlayList) {
//                    listListener!!.onClick(dialog!!, playList)
//                }},
//            true
//
//        )
        dialog_view.playlists_status.setOnClickListener {
            dialog!!.dismiss()
            playlistListener.onClickAdd()
        }
        dialog_view.create_playlist.setOnClickListener {
            dialog!!.dismiss()
            playlistListener.onClickAdd()
//            Managers.fragmentManager.goToFragment(
//                EditPlayListFragment(
//                    EditPlayListFragment.MODE_CREATE, object : EditPlaylistListener {
//                        override fun onEdit(name: String) {
//
//                            Managers.fragmentManager.goToFragment(
//                                NavigationScreenFragment(
//
//                                    selectedItem = R.id.liked_screen
//                                )
//                            )
//                            User.user_manager.createPlayList(name)
//                        }
//
//                        override fun onLeave() {
//                            Managers.fragmentManager.goToFragment(
//                                NavigationScreenFragment(
//                                    likedScreen = LikedTracksFragment(),
//                                    selectedItem = R.id.liked_screen
//                                )PlaylistListener
//                            )
//                        }
//                    })
//            )
        }
        if (User.user_manager.createdPlayLists.isNotEmpty()) {
            dialog_view.create_playlist.visibility = View.VISIBLE
            dialog_view.playlists_status.visibility = View.GONE
            dialog_view.list.addItemDecoration(SpaceItemDecoration(20))

            dialog_view.list.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
            dialog_view.list.adapter =
                PlayListAdapter(User.user_manager.createdPlayLists, object : onClickList {
                    override fun onClick(playList: PlayList) {
                        playlistListener.onClick(playList)
                        dialog!!.dismiss()
                    }
                })

        }  else {
            dialog_view.playlists_status.visibility = View.VISIBLE
        }
        dialog_view.close.setOnClickListener {
            dialog!!.dismiss()
        }
//        dialog_view.add_playlist_in_container.setOnClickListener {
//            dialog!!.dismiss()
//
//
////            CreatePlayListFragment {
////                PlayListDialog().show(Managers.fragmentManager.supportFragmentManager!!.beginTransaction(), "PlayListDialog")
////            }.show(Managers.fragmentManager.supportFragmentManager!!.beginTransaction(), "PlayListDialog")
//        }
        return dialog_view
    }



}