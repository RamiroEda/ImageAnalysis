package filters

import getColorChannels
import javafx.scene.control.*
import javafx.scene.paint.Color
import toIntColor
import tornadofx.add
import tornadofx.runAsync
import java.awt.image.BufferedImage

class Segmentacion(val image : BufferedImage) : Filtro(image) {
    override val title = "Segmentacion"
    private var lowerMargin = 0
    private var upperMargin = 255
    private var color = Color.valueOf("#FFFFFF")

    init {
        val sliderLow = Slider(0.0, upperMargin.toDouble(), lowerMargin.toDouble())
        val sliderUp = Slider(lowerMargin.toDouble(), 255.0, upperMargin.toDouble())

        sliderUp.isShowTickLabels = true
        sliderUp.isShowTickMarks = true
        sliderLow.isShowTickLabels = true
        sliderLow.isShowTickMarks = true

        this.layout.add(Label("Color de segmentacion"))

        val colorPicker = ColorPicker()

        colorPicker.setOnAction {
            this.color = colorPicker.value
            refresh(this@Segmentacion.apply())
        }

        this.layout.add(colorPicker)

        sliderLow.valueProperty().addListener { _, _, newValue ->
            this.lowerMargin = newValue.toInt()
            sliderUp.min = newValue.toDouble()
            refresh(this@Segmentacion.apply())
        }
        this.layout.add(sliderLow)

        sliderUp.valueProperty().addListener { _, _, newValue ->
            this.upperMargin = newValue.toInt()
            sliderLow.max = newValue.toDouble()
            refresh(this@Segmentacion.apply())
        }
        this.layout.add(sliderUp)
    }

    override fun apply(): BufferedImage {
        val imageCopy = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)

        for(y in 0 until image.height){
            for(x in 0 until image.width){
                val imageColor = image.getRGB(x,y).getColorChannels()
                val mean = imageColor.slice(1..3).sum()/3

                if(mean in lowerMargin..upperMargin){
                    imageCopy.setRGB(x , y, image.getRGB(x,y))
                }else{
                    imageCopy.setRGB(x , y, doubleArrayOf(
                        color.opacity,
                        color.red,
                        color.green,
                        color.blue).toIntColor())
                }
            }
        }

        return imageCopy
    }
}