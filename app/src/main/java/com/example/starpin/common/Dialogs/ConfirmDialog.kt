package com.example.starpin.common.Dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.drawable.Drawable

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.example.starpin.R
import com.example.starpin.User


class ConfirmDialog(val icon: Int, val message: String, val  on_confirm: Runnable): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Предупреждение")
                .setMessage(message)
                .setIcon(icon)
                .setCancelable(true)

                .setPositiveButton("Да") {_, _ -> on_confirm.run() }
                .setNegativeButton(
                    "Отмена"
                ) { _, _ ->

                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}
