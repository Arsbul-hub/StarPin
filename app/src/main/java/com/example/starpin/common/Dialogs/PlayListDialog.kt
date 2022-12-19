package com.example.starpin.common.Dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager

import com.example.starpin.*
import com.example.starpin.EditPlayListScreen.EditPlayListFragment

import com.example.starpin.common.Decorators.VerticalSpaceItemDecoration

import com.example.starpin.common.PlayList


import kotlinx.android.synthetic.main.playlist_dialog.view.*

interface ListListener {
    fun onClick(dialog: Dialog, playList: PlayList)
    fun onCreateNew()
}

class PlayListDialog(var listListener: ListListener?) : DialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val dialog_view = inflater.inflate(R.layout.playlist_dialog, container, false)
        dialog!!.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        //dialog_view.list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        dialog_view.list.addItemDecoration(VerticalSpaceItemDecoration(20))


        dialog_view.list.layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)

        dialog_view.list.adapter = PlayListAdapter(

            User.user_manager.createdPlayLists.values.toMutableList(),
            object : onClickList {
                override fun onClick(playList: PlayList) {
                    listListener!!.onClick(dialog!!, playList)
                }},
            true

        )

        dialog_view.close.setOnClickListener {
            dialog!!.dismiss()
        }
        dialog_view.create_playlist_button.setOnClickListener {
            dialog!!.dismiss()

            Managers.fragmentManager.goToFragment(EditPlayListFragment(EditPlayListFragment.MODE_CREATE) {


                User.user_manager.createPlayList(it)



                listListener!!.onCreateNew()
            })
//            CreatePlayListFragment {
//                PlayListDialog().show(Managers.fragmentManager.supportFragmentManager!!.beginTransaction(), "PlayListDialog")
//            }.show(Managers.fragmentManager.supportFragmentManager!!.beginTransaction(), "PlayListDialog")
        }
        return dialog_view
    }


    override fun onStart() {
        super.onStart()
        val width = (resources.displayMetrics.widthPixels * 0.85).toInt()
        val height = (resources.displayMetrics.heightPixels * 0.5).toInt()
        dialog!!.window?.setLayout(width, height)
    }
}