package com.star_wormwood.bulavka.common

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.star_wormwood.bulavka.R
import kotlinx.android.synthetic.main.my_dialog.*

class MyDialog(context: Context, val text: String): Dialog(context, R.style.HiStyle) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.my_dialog)
        main_text.setText(text)

    }
}