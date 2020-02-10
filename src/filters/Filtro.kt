package filters

import java.awt.image.BufferedImage

abstract class Filtro (val immage : BufferedImage) {
    abstract val title : String
    abstract fun apply() : BufferedImage
}