package com.alfardev.dipinjamin.utils.extensions

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

fun Context.showToast(message : String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.AlertRegister(message: String){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("ya"){dialog, _ ->
            dialog.dismiss()
        }
    }.show()
}

fun Context.AlertInfo(message: String){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("ya"){dialog, _ ->
            dialog.dismiss()
        }
    }.show()
}