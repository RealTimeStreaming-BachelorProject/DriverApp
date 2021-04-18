package com.bachelor.DriverApp.data.repository.packageservice

import com.bachelor.DriverApp.data.models.packageservice.DeliveryResponse
import com.bachelor.DriverApp.data.models.packageservice.InRouteResponse
import com.bachelor.DriverApp.data.requestbodies.packageservice.DriverDeliveryRequestBody
import com.bachelor.DriverApp.data.requestbodies.packageservice.DriverInRouteRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface PackageService {
    @POST("inroute")
    suspend fun inRoute(@Body inRouteRequestBody: DriverInRouteRequestBody) : Response<InRouteResponse>
    @POST("deliver")
    suspend fun deliver(@Body deliveryRequestBody: DriverDeliveryRequestBody) : Response<DeliveryResponse>
}