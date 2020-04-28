package filters

import javafx.geometry.Insets
import javafx.scene.layout.VBox
import java.awt.image.BufferedImage

abstract class Filtro (var image : BufferedImage) {
    abstract val title : String
    abstract fun apply() : BufferedImage
    private val refresh : ArrayList<(image : BufferedImage) -> Unit> = ArrayList()

    fun refresh(image : BufferedImage){
        for(lambda in refresh){
            lambda(image)
        }
    }

    open val layout = VBox()

    init {
        this.layout.spacing = 10.0
        this.layout.padding = Insets(12.0)
    }

    fun onRefresh(refresh : (image : BufferedImage) -> Unit){
        this.refresh.add(refresh)
    }
}