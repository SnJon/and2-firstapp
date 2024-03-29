package ru.netology.util

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.Group

fun View.hideKeyboard() {
    val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun Group.setAllOnClickListener(listener: (View) -> Unit) {
    val constraintLayout = parent as? ViewGroup
    referencedIds.forEach { id ->
        constraintLayout?.findViewById<View>(id)?.setOnClickListener(listener)
    }
}