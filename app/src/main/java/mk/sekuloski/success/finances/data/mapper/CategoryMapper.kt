package mk.sekuloski.success.finances.data.mapper

import mk.sekuloski.success.finances.data.local.CategoryEntity
import mk.sekuloski.success.finances.domain.model.Category

fun CategoryEntity.toCategory(): Category {
    return Category(
        id = id,
        name = name,
        limit = limit
    )
}

fun Category.toCategoryEntity(): CategoryEntity {
    return CategoryEntity(
        id = id,
        name = name,
        limit = limit
    )
}