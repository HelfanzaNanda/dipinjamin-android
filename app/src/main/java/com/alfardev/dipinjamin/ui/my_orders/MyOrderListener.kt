package com.alfardev.dipinjamin.ui.my_orders

import com.alfardev.dipinjamin.models.Checkout

interface MyOrderListener {
    fun delete(checkout: Checkout)
    fun detail(checkout: Checkout)
}