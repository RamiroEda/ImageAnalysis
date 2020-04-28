package filters

import getColorChannels
import javafx.scene.control.*
import javafx.scene.paint.Color
import mean
import toIntColor
import tornadofx.add
import tornadofx.runAsync
import java.awt.image.BufferedImage

class Binarizacion(immage : BufferedImage) : Filtro(immage) {
    override val title = "Binarizacion"
    private var lowerMargin = 0
    private var upperMargin = 255

    init {
        val sliderLow = Slider(0.0, upperMargin.toDouble()-1.0, lowerMargin.toDouble())
        val sliderUp = Slider(lowerMargin.toDouble()+1.0, 255.0, upperMargin.toDouble())

        sliderUp.isShowTickLabels = true
        sliderUp.isShowTickMarks = true
        sliderLow.isShowTickLabels = true
        sliderLow.isShowTickMarks = true

        sliderLow.valueProperty().addListener { _, _, newValue ->
            this.lowerMargin = newValue.toInt()
            sliderUp.min = newValue.toDouble() + 0.5
            refresh(this@Binarizacion.apply())
        }
        this.layout.add(sliderLow)

        sliderUp.valueProperty().addListener { _, _, newValue ->
            this.upperMargin = newValue.toInt()
            sliderLow.max = newValue.toDouble() - 0.5
            refresh(this@Binarizacion.apply())
        }
        this.layout.add(sliderUp)
    }

    override fun apply(): BufferedImage {
        val imageCopy = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)

        for(y in 0 until image.height){
            for(x in 0 until image.width){
                val imageColor = image.getRGB(x,y).getColorChannels()
                val mean = imageColor.mean()
                if(mean in lowerMargin..upperMargin){
                    imageCopy.setRGB(x,y, intArrayOf(255,255,255, 255).toIntColor())
                }else{
                    imageCopy.setRGB(x,y, intArrayOf(255,0,0,0).toIntColor())
                }
            }
        }

        return imageCopy
    }
}