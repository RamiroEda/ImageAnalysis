package gui

import filters.*
import javafx.scene.image.Image
import open.open
import open.toBufferedImage
import open.toImage
import tornadofx.*
import java.awt.image.BufferedImage
import kotlin.math.cos
import kotlin.math.sin
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

fun main() = launch<MainGUI>()

class MainGUI : App(MainView::class)

class MainView : View(){
    override val root = vbox{
        button ("Abrir Archivo"){
            setOnAction {
                find<ImageViewer>(
                    ImageScope(open()?.toImage(),
                        true,
                        arrayOf(
                            Segmentacion::class
                        )
                    )
                ).openWindow(owner = null, resizable = false)
            }
        }
    }

    private fun makeCircle(radio : Int) : BufferedImage{
        val bufferedImage = BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB)

        for(i in 0..360*radio){
            try {
                bufferedImage.setRGB(
                    (sin(i/radio.toFloat())*radio).toInt()+bufferedImage.width/2,
                    (cos(i/radio.toFloat())*radio).toInt()+bufferedImage.height/2, 0xFF000000.toInt())
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
            isPreserveRatio = true
            if(scope.isFilter){
                scope.filter!!.onRefresh{
                    this.image = it.toImage()
                }
            }
        }.image = if (scope.isFilter){
            scope.image = scope.filter!!.apply().toImage()
            scope.image
        }else{
            scope.image
        }
        if(scope.isFilter){
            this@ImageViewer.title = scope.filter!!.title
            this.add(scope.filter.layout)
        }
        if(scope.withHistogram){
            showHistogram()
        }
        for (filter in scope.filters){
            find<ImageViewer>(ImageScope(
                isFilter = true,
                filter = filter.primaryConstructor?.call(scope.image!!.toBufferedImage()) as Filtro,
                withHistogram = true
            )).openWindow(owner = null, resizable = false)
        }
    }

    private fun showHistogram(){
       if(scope.image is Image){
           Histogram(scope.image!!.toBufferedImage(), 0b1111).show()
       }
    }
}

class ImageScope (
    var image: Image? = null,
    val withHistogram : Boolean = false,
    val filters : Array<KClass<*>> = arrayOf(),
    val isFilter : Boolean = false,
    val filter : Filtro? = null
) : Scope()