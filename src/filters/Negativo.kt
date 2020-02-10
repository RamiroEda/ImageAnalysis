package filters

import getColorChannels
import toIntColor
import java.awt.image.BufferedImage

class Negativo (val image: BufferedImage) : Filtro(image) {
    override val title = "Negativo"

    override fun apply(): BufferedImage {
        for(y in 0 until image.height){
            for(x in 0 until image.width){
                val color = image.getRGB(x, y).getColorChannels()
                image.setRGB(x , y, intArrayOf(color[0], 255-color[1], 255-color[2], 255-color[3]).toIntColor())
            }
        }

        return image
    }
}