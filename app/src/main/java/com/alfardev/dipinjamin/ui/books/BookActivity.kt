package com.alfardev.dipinjamin.ui.books

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.alfardev.dipinjamin.R

class BookActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book)
        setSupportActionBar(findViewById(R.id.toolbar))

    }
}