package com.bachelor.DriverApp.services

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.bachelor.DriverApp.config.Urls
import com.bachelor.DriverApp.data.viewmodel.AuthEvent
import com.bachelor.DriverApp.data.viewmodel.CoordEvent
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.transports.WebSocket
import org.json.JSONObject
import java.net.URISyntaxException

class LocationService : Service() {

    private lateinit var locationCallback: LocationCallback
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest

    private lateinit var mSocket: Socket

    private var notificationChannelId = "channel_1"
    private var STOP_SELF_ACTION = "STOP_SELF"
    private var isStarted = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (STOP_SELF_ACTION.equals(intent?.action)) {
            stopSelf()
            isStarted = false
            Log.i("Location Service", "Stopping Location Service")
        }

        // Don't create new location if already started.
        if (isStarted) return START_STICKY
        if (!isStarted) isStarted = true

        Log.i("Location Service", "Starting Location Service")

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        createNotificationChannel()
        createSocket()
        createLocationCallBack()
        createLocationRequest()
        requestLocationUpdates()

        val notification = NotificationCompat.Builder(this, notificationChannelId)
            .setContentTitle("In Route")
            .setContentText("We're continuously sending your coordinates to our server.")
            .setSmallIcon(R.mipmap.sym_def_app_icon)
            .addAction(createStopServiceAction("Stop location service"))
            .build()
        startForeground(1, notification)

        return START_STICKY
    }

    private fun createStopServiceAction(label: String): NotificationCompat.Action {
        val stopSelf = Intent(this, LocationService::class.java)
        stopSelf.action = STOP_SELF_ACTION
        val pendingIntent = PendingIntent.getService(
            this,
            0,
            stopSelf,
            PendingIntent.FLAG_CANCEL_CURRENT
        )
        val action = NotificationCompat.Action.Builder(
            0, label, pendingIntent
        ).build()
        return action
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 3000
        locationRequest.fastestInterval = 100
        locationRequest.smallestDisplacement = 1f

    }

    private fun createLocationCallBack(){
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    val newLatLng = LatLng(location.latitude, location.longitude)

                    val intent = Intent("new_latlng");
                    intent.putExtra("latlng", newLatLng);
                    sendBroadcast(intent);

                    val gson = Gson()
                    val obj = JSONObject(
                        gson.toJson(
                            CoordEvent(
                                arrayOf(
                                    location.latitude,
                                    location.longitude
                                )
                            )
                        )
                    )
                    mSocket.emit("new_coordinates", obj)
                }
            }
        }
    }

    private fun requestLocationUpdates() {
        /* START - PERMISSION CHECK */
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("NO PERMISSION!!!")
            return
        }
        /* END - PERMISSION CHECK */

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun createSocket() {
        try {
            val opts = IO.Options()
            opts.transports = arrayOf(WebSocket.NAME)
            mSocket = IO.socket(Urls.DRIVERS_URL, opts)
            mSocket.connect()
            mSocket.on(Socket.EVENT_CONNECT_ERROR) { error ->
                println("SOCKETIO:  error");
                println(error.forEach { err -> println(err.toString()) })
                val intent = Intent("socket_error");
                intent.putExtra("error_message", "Could not connect to GPS server.");
                sendBroadcast(intent);
            }
            mSocket.on(Socket.EVENT_CONNECT, { println("SOCKETIO: connected") })
            mSocket.on(Socket.EVENT_DISCONNECT, { println("SOCKETIO: disconnected") })
            var token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6Im5pY29sYWlncmFtIiwiZHJpdmVySUQiOiI2MjgzODhiMy1kYjM4LTQyMzgtOWRiYS00MjgyYmY2Y2E0ZmQifQ.UYPoCta13O-qpPa_oybbDU6S8FhClciM58efY0FZiwc"
            val gson = Gson()
            val obj = JSONObject(gson.toJson(AuthEvent(token)))
            mSocket.emit("authenticate", obj)
        } catch (e: URISyntaxException) {
            println("Error connecting to socket")
        }
    }

    private fun createNotificationChannel() {
        val notificationChannel = NotificationChannel(
            notificationChannelId,
            "Foreground Notification",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(notificationChannel)
    }
}