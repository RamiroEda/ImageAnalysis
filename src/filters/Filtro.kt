package filters

import javafx.scene.layout.VBox
import java.awt.image.BufferedImage

abstract class Filtro (val immage : BufferedImage) {
    abstract val title : String
    abstract fun apply() : BufferedImage
    protected var refresh : (image : BufferedImage) -> Unit = {}

    open val layout = VBox()
    fun onRefresh(refresh : (image : BufferedImage) -> Unit){
        this.refresh =refresh
    }
}