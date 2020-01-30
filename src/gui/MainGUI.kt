package gui

import javafx.scene.Parent
import javafx.scene.image.Image
import open.open
import open.toImage
import tornadofx.*
import java.awt.Color
import java.awt.image.BufferedImage
import kotlin.math.cos
import kotlin.math.sin

fun main() = launch<MainGUI>()

class MainGUI : App(MainView::class)

class MainView : View(){
    override val root = vbox{
        button ("Abrir Archivo"){
            setOnAction {
                find<ImageViewer>(ImageScope(open()?.toImage())).openWindow(owner = null)
            }
        }
        find<ImageViewer>(ImageScope(makeCircle(100).toImage())).openWindow(owner = null)
    }

    private fun makeCircle(radio : Int) : BufferedImage{
        val bufferedImage = BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB)

        for(i in 0..360*radio){
            try {
                bufferedImage.setRGB((sin(i/radio.toFloat())*radio).toInt()+bufferedImage.width/2, (cos(i/radio.toFloat())*radio).toInt()+bufferedImage.height/2, 0xFF000000.toInt())
            }catch (e : Exception){}
        }


        return bufferedImage
    }
}

class ImageViewer : Fragment(){
    override val scope = super.scope as ImageScope

    override val root = vbox {
        imageview {
            fitWidth = 800.0
            fitHeight = 600.0
        }.image = scope.image
    }
}

class ImageScope (val image: Image?) : Scope()