package com.example.starpin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.edit_playlist_fragment.view.*


class CreatePlayListFragment(val onClosed: () -> Unit = {}) : Fragment() {


    var set_name = ""
    lateinit var current_adapter: PlayListAdapter

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
        fragment_view.playlist_name_field.setOnKeyListener { view, i, keyEvent ->
            fragment_view.playlist_name_field_layout.helperText = validateTitle(fragment_view.playlist_name_field.text.toString())

            return@setOnKeyListener false
        }
        fragment_view.create_playlist_button.setOnClickListener {
            fragment_view.playlist_name_field_layout.helperText = validateTitle(fragment_view.playlist_name_field.text.toString())
            if (validateTitle(fragment_view.playlist_name_field.text.toString()).isEmpty()) {
                User.user_manager.createPlayList(fragment_view.playlist_name_field.text.toString())
                //requireActivity().supportFragmentManager.

                onClosed()

                Toast.makeText(
                    context,
                    "Создан новый плейлист ${fragment_view.playlist_name_field.text}",
                    Toast.LENGTH_SHORT
                ).show()
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
        }else if (title.length > 15) {
            error = "Длина названия должна быть не больше 15 симбволов"
        }
        return error
    }

}