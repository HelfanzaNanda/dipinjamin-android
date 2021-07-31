package com.alfardev.dipinjamin.ui.fragments.books

import com.alfardev.dipinjamin.models.Book

interface BookListener{
    fun delete(book : Book)
}