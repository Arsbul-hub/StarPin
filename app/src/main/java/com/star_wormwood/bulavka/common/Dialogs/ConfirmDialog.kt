package com.star_wormwood.bulavka.common.Dialogs

import android.app.AlertDialog
import android.app.Dialog

import android.os.Bundle
import androidx.fragment.app.DialogFragment


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
