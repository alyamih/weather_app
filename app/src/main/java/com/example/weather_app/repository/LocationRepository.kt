package com.example.weather_app.repository

import android.content.Context
import com.example.weather_app.model.Location
import com.google.android.gms.location.LocationServices
import kotlin.coroutines.cancellation.CancellationException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class LocationRepository(private val context: Context) {

    private val locationManager by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    suspend fun getLocation() = suspendCoroutine<Location?> { cont ->
        try {
            locationManager.lastLocation
                .addOnSuccessListener {
                    val location =
                        it?.let { Location(it.latitude, it.longitude) } ?: Location(0.0, 0.0)
                    cont.resume(location)
                }.addOnCanceledListener {
                    cont.resumeWithException(CancellationException())
                }.addOnFailureListener {
                    cont.resumeWithException(it)
                }
        } catch (e: SecurityException) {
            cont.resumeWithException(e)
        }
    }
}