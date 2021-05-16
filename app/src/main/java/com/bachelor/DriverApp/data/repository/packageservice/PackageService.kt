package com.bachelor.DriverApp.data.repository.packageservice

import com.bachelor.DriverApp.data.models.packageservice.DeliveryResponse
import com.bachelor.DriverApp.data.models.packageservice.InRouteResponse
import com.bachelor.DriverApp.data.models.packageservice.PackageDetailsResponse
import com.bachelor.DriverApp.data.models.packageservice.RegisterPackageResponse
import com.bachelor.DriverApp.data.requestbodies.packageservice.DriverDeliveryRequestBody
import com.bachelor.DriverApp.data.requestbodies.packageservice.DriverInRouteRequestBody
import com.bachelor.DriverApp.data.requestbodies.packageservice.RegisterPackageRequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.*

interface PackageService {
    @POST(".")
    suspend fun register(@Body registerPackage: RegisterPackageRequestBody) : Response<RegisterPackageResponse>
    @POST("inroute")
    suspend fun inRoute(@Body inRouteRequestBody: DriverInRouteRequestBody) : Response<InRouteResponse>
    @POST("deliver")
    suspend fun deliver(@Body deliveryRequestBody: DriverDeliveryRequestBody) : Response<DeliveryResponse>
    @GET(".")
    suspend fun getPackageDetails(@Query("packageID") packageID: String) : Response<PackageDetailsResponse>
}