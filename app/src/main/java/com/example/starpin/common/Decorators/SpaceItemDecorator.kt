package com.example.starpin.common.Decorators

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VerticalSpaceItemDecoration(val verticalSpaceHeight: Int) :
    RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.bottom = verticalSpaceHeight
        outRect.top = verticalSpaceHeight
        outRect.left = verticalSpaceHeight
        outRect.right = verticalSpaceHeight
    }

}
