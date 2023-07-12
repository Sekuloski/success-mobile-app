package mk.sekuloski.success.finances.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class FinancesMainEntity (
    @PrimaryKey val id: Int = 0,
    val amount_left: Int,
    val salary: Int,
    val bank: Int,
    val cash: Int,
    val euros: Float,
    val expenses: Int,
    val reserved: Int
    )