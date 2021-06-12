package com.alfardev.dipinjamin.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import java.text.NumberFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.regex.Pattern


class Constants {
    companion object{
        const val END_POINT = "http://dipinjamin.alfara-dev.com/api/"
        const val END_POINT_TERRITORY = "https://dev.farizdotid.com/api/daerahindonesia/"


        fun getToken(c : Context) : String {
            val s = c.getSharedPreferences("USER", MODE_PRIVATE)
            val token = s?.getString("TOKEN", "UNDEFINED")
            return token!!
        }

        fun setToken(context: Context, token : String){
            val pref = context.getSharedPreferences("USER", MODE_PRIVATE)
            pref.edit().apply {
                putString("TOKEN", token)
                apply()
            }
        }

        fun clearToken(context: Context){
            val pref = context.getSharedPreferences("USER", MODE_PRIVATE)
            pref.edit().clear().apply()
        }

        fun isValidEmail(email : String) = android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
        fun isValidPassword(pass : String) = pass.length >= 8

        fun setToIDR(num : Int) : String {
            val localeID = Locale("in", "ID")
            val formatRupiah: NumberFormat = NumberFormat.getCurrencyInstance(localeID)
            return formatRupiah.format(num)
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun changeFormatDate(date : String) : String{
            val current = LocalDateTime.now()
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            return current.format(formatter)
        }

        fun isAlpha(name : String) = Pattern.matches("[a-zA-Z]+", name)


        fun getRealPathFromURI(context: Context, contentURI: Uri): String {
            val result: String
            val cursor = context.contentResolver.query(contentURI, null, null, null, null)
            if (cursor == null) { // Source is Dropbox or other similar local file path
                result = contentURI.path!!
            } else {
                cursor.moveToFirst()
                val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                result = cursor.getString(idx)
                cursor.close()
            }
            return result
        }
    }
}