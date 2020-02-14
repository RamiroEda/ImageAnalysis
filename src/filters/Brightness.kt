package filters

import getColorChannels
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.scene.control.Slider
import javafx.scene.image.ImageView
import javafx.scene.layout.VBox
import toIntColor
import tornadofx.add
import tornadofx.findAll
import java.awt.image.BufferedImage
import java.nio.Buffer

class Brightness(val image: BufferedImage) : Filtro(image) {
    var brightness = 0

    override val title = "Iluminación"

    init {
        val slider = Slider(-255.0, 255.0, 0.0)
        slider.valueProperty().addListener { _, _, newValue ->
            this@Brightness.brightness = newValue.toInt()
            refresh(this@Brightness.apply())
        }
        this.layout.add(slider)
    }

    override fun apply(): BufferedImage {
        val imageCopy = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)
        for(y in 0 until image.height){
            for(x in 0 until image.width){
                val color = image.getRGB(x,y).getColorChannels()
                imageCopy.setRGB(x , y, intArrayOf(color[0], bright(color[1]), bright(color[2]), bright(color[3])).toIntColor())
            }
        }

        return imageCopy
    }

    private fun bright(value : Int) = when{
        value+brightness > 255 -> 255
        value+ brightness < 0 -> 0
        else -> value+brightness
    }
}