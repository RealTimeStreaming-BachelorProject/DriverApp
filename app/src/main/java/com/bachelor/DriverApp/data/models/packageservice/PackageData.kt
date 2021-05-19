package com.bachelor.DriverApp.data.models.packageservice

import java.util.*

class PackageData(receiverAddress: String, weightKg: Int, packageId: UUID, expectedDeliveryTime: Date) {
    var receiverAddress: String = receiverAddress
    var weightKg: Int = weightKg
    var packageId: UUID = packageId
    var expectedDeliveryTime: Date = expectedDeliveryTime
    var delivered: Boolean = false
}