package alex.carcar.idosudoku6

import java.util.*

data class Square(
    val row: Int = 0,
    val col: Int = 0,
    val value: Int = 0,
    var isHidden: Boolean = false
) {
    val id get() = "$row$col"
}