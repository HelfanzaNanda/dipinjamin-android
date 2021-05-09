package com.alfardev.dipinjamin.utils.extensions

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.alfardev.dipinjamin.ui.login.LoginActivity

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

fun Context.alertInfo(message: String){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("ya"){dialog, _ ->
            dialog.dismiss()
        }
    }.show()
}

fun Context.alertNotLogin(context: Context, message: String){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setCancelable(false)
        setPositiveButton("Login"){dialog, _ ->
            dialog.dismiss()
            startActivity(Intent(context, LoginActivity::class.java).putExtra("EXCEPT_RESULT", true))
        }
    }.show()
}