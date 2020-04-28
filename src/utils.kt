import javafx.scene.paint.Color
import java.io.File
import java.util.*


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

fun DoubleArray.toIntColor() : Int = (this[0]*255).toInt().shl(24)+
        (this[1]*255).toInt().shl(16)+
        (this[2]*255).toInt().shl(8)+
        (this[3]*255).toInt()

fun IntArray.mean() : Double{
    var res = 0.0

    for(i in this){
        res += i
    }

    return res.div(this.size)
}

fun DoubleArray.mean() : Double{
    var res = 0.0

    for(i in this){
        res += i
    }

    return res.div(this.size)
}

fun Int.validateBounds() : Int{
    return when{
        this > 255 -> 255
        this < 0 -> 0
        else -> this
    }
}

fun Color.toInt() : Int = (this.opacity*255).toInt().shl(24)+
        (this.red*255).toInt().shl(16)+
        (this.green*255).toInt().shl(8)+
        (this.blue*255).toInt()