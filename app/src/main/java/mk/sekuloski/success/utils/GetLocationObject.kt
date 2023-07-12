package mk.sekuloski.success.utils

import mk.sekuloski.success.finances.domain.model.Location
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


fun findClosestLocation(latitude: Double, longitude: Double, locations: List<Location>): Location? {
    var closestLocation: Location? = null
    var closestDistance = Double.MAX_VALUE

    for (location in locations) {
        val locationCoordinates = location.coordinates.split(",").map { it.trim() }
        val locationLatitude = locationCoordinates[0].toDouble()
        val locationLongitude = locationCoordinates[1].toDouble()

        val distance = calculateDistance(
            latitude,
            longitude,
            locationLatitude,
            locationLongitude
        )

        if (distance < closestDistance) {
            closestDistance = distance
            closestLocation = location
        }
    }

    return closestLocation
}

fun calculateDistance(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double
): Double {
    val r = 6371 // Radius of the earth in kilometers
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return r * c
}
