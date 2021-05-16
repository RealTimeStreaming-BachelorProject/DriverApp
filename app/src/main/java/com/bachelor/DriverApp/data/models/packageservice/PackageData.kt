package com.bachelor.DriverApp.data.models.packageservice

import java.util.*

class PackageData(receiverAddress: String, weightKg: Int, packageId: UUID, expectedDeliveryTime: Date) {
    var receiverAddress: String = receiverAddress
        get() {
            return field
        }
        set(value) {
            field = value
        }
    var weightKg: Int = weightKg
        get() {
            return field
        }
        set(value) {
            field = value
        }
    var packageId: UUID = packageId
        get() {
            return field
        }
        set(value) {
            field = value
        }
    var expectedDeliveryTime: Date = expectedDeliveryTime
        get() {
            return field
        }
        set(value) {
            field = value
        }
    var delivered: Boolean = false
        get() {
            return field
        }
        set(value) {
            field = value
        }
}