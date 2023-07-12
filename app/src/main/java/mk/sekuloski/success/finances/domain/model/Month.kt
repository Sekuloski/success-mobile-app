package mk.sekuloski.success.finances.domain.model

data class Month(
    val id: Int,
    var name: String,
    val left: Int,
    val expenses: Int,
    )