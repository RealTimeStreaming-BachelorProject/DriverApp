package com.bachelor.DriverApp.data.models.packageservice

import com.google.gson.annotations.SerializedName
import java.util.*
import kotlin.collections.ArrayList

class PackageDetailsResponse {
    @SerializedName("packageID")
    lateinit var packageID: UUID
    @SerializedName("receiverAddress")
    var receiverAddress: String = ""
    @SerializedName("receiverName")
    var receiverName: String = ""
    @SerializedName("receiverEmail")
    var receiverEmail: String = ""
    @SerializedName("senderAddress")
    var senderAddress: String = ""
    @SerializedName("senderName")
    var senderName: String = ""
    @SerializedName("weightKg")
    var weightKg: Int = 0
    @SerializedName("registered")
    var registered: String = ""
    @SerializedName("expectedDeliveryDate")
    var expectedDeliveryDate: Date = Date()
    @SerializedName("historyEntries")
    var historyEntries: ArrayList<HistoryEntry> = ArrayList()
    @SerializedName("driverID")
    var driverID: String = ""
    @SerializedName("expectedDeliveryTime")
    var expectedDeliveryTime: Date = Date()
    @SerializedName("status")
    var status: Int = 0
    @SerializedName("message")
    var message: String = ""

    class HistoryEntry {
        @SerializedName("status")
        var status: String = ""
        @SerializedName("message")
        var message: String = ""
        @SerializedName("entryDate")
        var entryDate: Date = Date()
    }
}