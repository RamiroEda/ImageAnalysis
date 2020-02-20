package filters

import javafx.geometry.Insets
import javafx.scene.layout.VBox
import java.awt.image.BufferedImage

abstract class Filtro (val immage : BufferedImage) {
    abstract val title : String
    abstract fun apply() : BufferedImage
    protected var refresh : (image : BufferedImage) -> Unit = {}

    open val layout = VBox()

    init {
        this.layout.spacing = 10.0
        this.layout.padding = Insets(12.0)
    }

    fun onRefresh(refresh : (image : BufferedImage) -> Unit){
        this.refresh =refresh
    }
}