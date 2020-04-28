package gui

import Convolucion
import filters.*
import javafx.geometry.Insets
import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.control.CheckBox
import javafx.scene.image.Image
import javafx.scene.text.Font
import open.*
import toInt
import tornadofx.*
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

fun main() = launch<MainGUI>()

class MainGUI : App(MainView::class)

val filters = arrayOf(
    Brightness::class,
    EscalaDeGrises::class,
    Negativo::class,
    Segmentacion::class,
    Temperatura::class,
    Estiramiento::class,
    Binarizacion::class,
    Suavizado::class,
    Convolucion::class
)

class MainView : View(){
    override val root = vbox{
        prefWidth = 200.0
        val set = fieldset {
            spacing = 4.0
            label {
                text = "Filtros"
                font = Font.font(20.0)
            }
            padding = Insets(16.0)
            for (clazz in filters){
                this.add(checkbox {
                    text = clazz.simpleName
                })
            }
        }
        separator {  }
        borderpane {
            padding = Insets(8.0)
            center = button ("Abrir Archivo"){
                setOnAction {
                    val image = open()?.toImage()
                    if(image != null){
                        find<ImageViewer>(
                            ImageScope(image,
                                true,
                                set.getChildList()!!.filterIsInstance<CheckBox>()
                                    .zip(filters)
                                .filter {
                                    (it.first as CheckBox).isSelected
                                }.map {
                                    it.second
                                }.toTypedArray()
                            )
                        ).openWindow(owner = null, resizable = false)
                    }
                }
            }
        }
    }

}

class ImageViewer : Fragment(){
    override val scope = super.scope as ImageScope
    private lateinit var histogram: Histogram

    override val root = hbox {
        vbox {
            imageview {
                fitWidth = 800.0
                fitHeight = 600.0
                isPreserveRatio = true
                if(scope.isFilter){
                    scope.filter!!.onRefresh{
                        this.image = it.toImage()
                        if(::histogram.isInitialized){
                            this@ImageViewer.histogram.update(it)
                        }
                    }
                }
            }.image = if (scope.isFilter){
                scope.image = scope.filter!!.apply().toImage()
                scope.image
            }else{
                this@ImageViewer.title = "Imagen original"
                scope.image
            }
            if(scope.isFilter){
                this@ImageViewer.title = scope.filter!!.title
                this.add(scope.filter.layout)
            }
            if(scope.withHistogram){
                this@hbox.add(this@ImageViewer.getHistogram())
            }
            if(scope.isFilter){
                hbox {
                    button("Guardar imagen") {
                        setOnAction {
                            saveImage(scope.image!!, this@ImageViewer.title)
                        }
                    }
                    padding = Insets(8.0)
                    spacing = 4.0

                    val noisePicker = colorpicker {}

                    button("Agregar ruido") {
                        setOnAction {
                            val noise = addNoise(scope.image!!, noisePicker.value.toInt())
                            scope.filter?.refresh(noise)
                            scope.filter?.image = noise
                        }
                    }
                }
            }
            for (filter in scope.filters){
                find<ImageViewer>(ImageScope(
                    isFilter = true,
                    filter = filter.primaryConstructor?.call(scope.image!!.toBufferedImage()) as Filtro,
                    withHistogram = true
                )).openWindow(owner = null, resizable = false)
            }
        }
    }

    private fun getHistogram() : LineChart<Number, Number>{
        if(scope.image is Image){
            this.histogram = Histogram(scope.image!!.toBufferedImage(), 0b1111, this.title)
            return this.histogram.chart
        }

        return LineChart(NumberAxis(), NumberAxis())
    }
}

class ImageScope (
    var image: Image? = null,
    val withHistogram : Boolean = false,
    val filters : Array<KClass<*>> = arrayOf(),
    val isFilter : Boolean = false,
    val filter : Filtro? = null
) : Scope()