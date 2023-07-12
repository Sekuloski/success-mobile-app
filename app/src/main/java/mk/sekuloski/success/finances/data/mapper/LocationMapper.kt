package mk.sekuloski.success.finances.data.mapper

import mk.sekuloski.success.finances.data.local.LocationEntity
import mk.sekuloski.success.finances.domain.model.Location

fun LocationEntity.toLocation(): Location {
    return Location(
        id = id,
        name = name,
        coordinates = coordinates
    )
}

fun Location.toLocationEntity(): LocationEntity {
    return LocationEntity(
        id = id,
        name = name,
        coordinates = coordinates
    )
}