package filters

import getColorChannels
import javafx.scene.control.Slider
import javafx.scene.control.TextField
import toIntColor
import tornadofx.Field
import tornadofx.add
import java.awt.image.BufferedImage

class Temperatura(val image: BufferedImage) : Filtro(image) {
    override val title = "Temperatura"
    private var temperature = 0

    init {
        val slider = Slider(-255.0, 255.0, 0.0)

        slider.isShowTickMarks = true
        slider.isShowTickLabels = true

        slider.valueProperty().addListener { _, _, _ ->
            this.temperature = slider.value.toInt()

            refresh(this.apply())
        }
        this.layout.add(slider)
    }

    override fun apply(): BufferedImage {
        val imageCopy = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)
        for(y in 0 until image.height){
            for(x in 0 until image.width){
                val color = image.getRGB(x,y).getColorChannels()
                imageCopy.setRGB(x , y, intArrayOf(color[0], temperature(color[1]), color[2], temperature2(color[3])).toIntColor())
            }
        }

        return imageCopy
    }

    private fun temperature(value : Int) = when{
        value+temperature > 255 -> 255
        value+temperature < 0 -> 0
        else -> value+temperature
    }

    private fun temperature2(value : Int) = when{
        value-temperature > 255 -> 255
        value-temperature < 0 -> 0
        else -> value-temperature
    }
}