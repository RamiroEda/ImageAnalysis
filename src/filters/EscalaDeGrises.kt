package filters

import getColorChannels
import toIntColor
import java.awt.image.BufferedImage

class EscalaDeGrises (immage : BufferedImage) : Filtro(immage) {
    override val title = "Escala de grises"

    override fun apply(): BufferedImage {
        for(y in 0 until image.height){
            for(x in 0 until image.width){
                val color = image.getRGB(x, y).getColorChannels()
                val mean  = (color[3]+color[1]+color[2])/3
                image.setRGB(x , y, intArrayOf(255, mean, mean, mean).toIntColor())
            }
        }

        return image
    }
}