package com.bachelor.DriverApp.data.models.packageservice

class PackageData(receiverAddress: String, weightKg: Int, packageId: String) {
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
    var packageId: String = packageId
        get() {
            return field
        }
        set(value) {
            field = value
        }
}