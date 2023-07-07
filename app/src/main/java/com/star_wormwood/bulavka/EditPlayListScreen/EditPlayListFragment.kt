package com.star_wormwood.bulavka.EditPlayListScreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.star_wormwood.bulavka.R
import com.star_wormwood.bulavka.User

import kotlinx.android.synthetic.main.edit_playlist_fragment.view.*
import kotlinx.android.synthetic.main.search_fragment.view.search_field

interface EditPlaylistListener {
    fun onEdit(name: String)
    fun onBack()
}

class EditPlayListFragment(
    val showingMode: String,

    val editPlaylistListener: EditPlaylistListener,
    val oldName: String? = null
) :
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


//        fragment_view.playlist_name_field_layout.setOnFocusChangeListener { view, b ->
//
//        }
        if (showingMode == MODE_CREATE) {
            fragment_view.title.text = "Создание плейлиста"
            fragment_view.create_playlist_button.text = "Создать плейлист"
        } else {
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


                //requireActivity().supportFragmentManager.
                val imm: InputMethodManager =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(fragment_view.playlist_name_field_layout.windowToken, 0)
                if (showingMode == MODE_CREATE) {
                    User.user_manager.createPlayList(fragment_view.playlist_name_field.text.toString())
                    Toast.makeText(
                        context,
                        "Создан плейлист ${fragment_view.playlist_name_field.text.toString()}",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    User.user_manager.renamePlayList(
                        fragment_view.playlist_name_field.text.toString(),
                        oldName!!
                    )
                }
                editPlaylistListener.onEdit(fragment_view.playlist_name_field.text.toString())


            }

        }
        fragment_view.exit.setOnClickListener {
            editPlaylistListener.onBack()
        }

        return fragment_view
    }

    fun validateTitle(title: String): String {
        var error = ""
        if (title.isEmpty()) {
            error = "Поле не дожно быть пустым"
        } else if (User.user_manager.getCreatedPlaylist(title) != null) {
            error = "Плейлист с таким именем уже существует"
        } else if (title.length > 15) {
            error = "Длина названия должна быть не больше 15 симбволов"
        }
        return error
    }

}