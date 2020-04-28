import filters.Filtro
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.HBox
import tornadofx.add
import tornadofx.isInt
import java.awt.image.BufferedImage
import java.lang.Exception

class Convolucion (immage : BufferedImage) : Filtro(immage) {
    override val title = "Convolucion"
    private val inputs = ArrayList<ArrayList<TextField>>()
    private var offset : Int = 0
    private var div : Int = 1

    init {
        for(i in 0 until 3){
            inputs.add(arrayListOf())
            for(e in 0 until 3){
                val editText = TextField(if(i == 1 && e == 1){
                    "1"
                }else{
                    "0"
                })
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

        val modificadores = HBox()

        val div = TextField(div.toString())
        val offset = TextField(offset.toString())

        modificadores.add(div)

        modificadores.add(offset)

        layout.add(modificadores)

        val button = Button("Aplicar mascara")
        button.setOnAction {
            this.div = div.text.toInt()
            this.offset = offset.text.toInt()
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
                var r = 0
                var g = 0
                var b = 0
                for(i in -1..1){
                    for(e in -1..1){
                        if(mask[i+1][e+1] != 0){
                            try {
                                val imageColor = image.getRGB(x+e,y+i).getColorChannels()
                                r += imageColor[1] * mask[i+1][e+1]
                                g += imageColor[2] * mask[i+1][e+1]
                                b += imageColor[3] * mask[i+1][e+1]
                            }catch (e : Exception){}
                        }
                    }
                }

                imageCopy.setRGB(x,y, intArrayOf(255,
                    ((r/div)+ offset).validateBounds(),
                    ((g/div)+offset).validateBounds(),
                    ((b/div)+offset).validateBounds()).toIntColor())
            }
        }
        return imageCopy
    }
}