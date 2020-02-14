package filters

import javafx.scene.control.Slider
import toIntColor
import tornadofx.add
import java.awt.image.BufferedImage

class Segmentacion(val image : BufferedImage) : Filtro(image) {
    override val title = "Segmentacion"
    private var lowerMargin = 0
    private var upperMargin = 255

    init {
        val sliderLow = Slider(0.0, 255.0, 0.0)
        val sliderUp = Slider(0.0, 255.0, 255.0)

        sliderLow.valueProperty().addListener { _, _, newValue ->
            this.lowerMargin = newValue.toInt()
            sliderUp.min = newValue.toDouble()
            refresh(this.apply())
        }
        this.layout.add(sliderLow)

        sliderUp.valueProperty().addListener { _, _, newValue ->
            this.upperMargin = newValue.toInt()
            sliderUp.max = newValue.toDouble()
            refresh(this.apply())
        }
        this.layout.add(sliderUp)
    }

    override fun apply(): BufferedImage {
        val imageCopy = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)
        val grayscale = EscalaDeGrises(image).apply()

        for(y in 0 until image.height){
            for(x in 0 until image.width){
                if(image.getRGB(x, y) in lowerMargin..upperMargin){
                    imageCopy.setRGB(x , y, image.getRGB(x,y))
                }else{
                    imageCopy.setRGB(x , y, intArrayOf(255,0,0,0).toIntColor())
                }
            }
        }

        return imageCopy
    }
}