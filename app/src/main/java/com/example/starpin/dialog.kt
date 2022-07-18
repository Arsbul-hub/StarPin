package com.example.starpin

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import kotlinx.android.synthetic.main.my_dialog.*

class MyDialog(context: Context, val text: String): Dialog(context, R.style.HiStyle) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.my_dialog)
        main_text.setText(text)

    }
}