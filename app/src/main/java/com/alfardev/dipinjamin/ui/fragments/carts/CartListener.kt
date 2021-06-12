package com.alfardev.dipinjamin.ui.fragments.carts

import com.alfardev.dipinjamin.models.Cart

interface CartListener{
    fun delete(cart: Cart)
}