package filters

import getColorChannels
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import toIntColor
import tornadofx.add
import tornadofx.isInt
import java.awt.image.BufferedImage
import java.lang.Exception

class Suavizado (immage : BufferedImage) : Filtro(immage) {
    override val title = "Suavizado"
    private val inputs = ArrayList<ArrayList<TextField>>()

    init {
        for(i in 0 until 3){
            inputs.add(arrayListOf())
            for(e in 0 until 3){
                val editText = TextField("0")
                inputs.last().add(editText)
            }
        }


        for(row in inputs){
            val hbox = HBox()
            for(input in row){
                hbox.add(input)
            }
            layout.add(hbox)
        }

        val button = Button("Aplicar mascara")
        button.setOnAction {
            refresh(apply())
        }

        layout.add(button)
    }

    private fun inputsToArray() = Array(inputs.size){
        IntArray(inputs[it].size){e ->
            if(inputs[it][e].text.isInt()){
                inputs[it][e].text.toInt()
            }else{
                0
            }
        }
    }

    override fun apply(): BufferedImage {
        val imageCopy = BufferedImage(image.width, image.height, BufferedImage.TYPE_INT_ARGB)
        val mask = inputsToArray()

        for(y in 0 until image.height){
            for(x in 0 until image.width){
                var correctos = 0
                var r = 0
                var g = 0
                var b = 0
                for(i in -1..1){
                    for(e in -1..1){
                        if(mask[i+1][e+1] != 0){
                            try {
                                val imageColor = image.getRGB(x+e,y+i).getColorChannels()
                                correctos++
                                r += imageColor[1]
                                g += imageColor[2]
                                b += imageColor[3]
                            }catch (e : Exception){

                            }
                        }
                    }
                }

                if(correctos == 0){
                    imageCopy.setRGB(x,y, image.getRGB(x,y))
                }else{
                    imageCopy.setRGB(x,y, intArrayOf(255, r/correctos, g/correctos, b/correctos).toIntColor())
                }
            }
        }
        return imageCopy
    }
}