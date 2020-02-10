

fun Int.getColorChannels() = intArrayOf(
    (this and 0xFF000000.toInt()).shr(24),
    (this and 0xFF0000).shr(16),
    (this and 0xFF00).shr(8),
    (this and 0xFF)
)

fun IntArray.toIntColor() : Int = this[0].shl(24)+
        this[1].shl(16)+
        this[2].shl(8)+
        this[3]