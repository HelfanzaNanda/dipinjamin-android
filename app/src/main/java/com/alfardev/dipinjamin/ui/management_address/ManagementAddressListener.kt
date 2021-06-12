package com.alfardev.dipinjamin.ui.management_address

import com.alfardev.dipinjamin.models.DeliveryAddress

interface ManagementAddressListener {
    fun edit(deliveryAddress: DeliveryAddress)
}