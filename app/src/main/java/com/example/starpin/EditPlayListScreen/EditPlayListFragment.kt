package com.example.starpin.EditPlayListScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.starpin.Managers
import com.example.starpin.R
import com.example.starpin.User

import kotlinx.android.synthetic.main.edit_playlist_fragment.view.*


class EditPlayListFragment(val showingMode: String, val onClosed: (name: String) -> Unit = {}) :
    Fragment() {
    companion object {
        const val MODE_CREATE = "MODE_CREATE"
        const val MODE_EDIT = "MODE_EDIT"
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val fragment_view = inflater.inflate(R.layout.edit_playlist_fragment, container, false)

        fragment_view.exit.setOnClickListener {
            Managers.fragmentManager.goToPrevious()

        }

//        fragment_view.playlist_name_field_layout.setOnFocusChangeListener { view, b ->
//
//        }
        if (showingMode == MODE_CREATE) {
            fragment_view.title.text = "Создание плейлиста"
            fragment_view.create_playlist_button.text = "Создать плейлист"
        } else if (showingMode == MODE_EDIT) {
            fragment_view.title.text = "Редактирование плейлиста"
            fragment_view.create_playlist_button.text = "Сохранить плейлист"
        }

        fragment_view.playlist_name_field.setOnKeyListener { view, i, keyEvent ->
            fragment_view.playlist_name_field_layout.helperText =
                validateTitle(fragment_view.playlist_name_field.text.toString())

            return@setOnKeyListener false
        }
        fragment_view.create_playlist_button.setOnClickListener {
            fragment_view.playlist_name_field_layout.helperText =
                validateTitle(fragment_view.playlist_name_field.text.toString())
            if (validateTitle(fragment_view.playlist_name_field.text.toString()).isEmpty()) {

                User.user_manager.createPlayList(fragment_view.playlist_name_field.text.toString())
                //requireActivity().supportFragmentManager.

                onClosed(fragment_view.playlist_name_field.text.toString())


            }

        }


        return fragment_view
    }

    fun validateTitle(title: String): String {
        var error = ""
        if (title.isEmpty()) {
            error = "Поле не дожно быть пустым"
        } else if (title in User.user_manager.createdPlayLists) {
            error = "Плейлист с таким именем уже существует"
        } else if (title.length > 15) {
            error = "Длина названия должна быть не больше 15 симбволов"
        }
        return error
    }

}