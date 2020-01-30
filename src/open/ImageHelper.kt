package open

import javafx.scene.image.Image
import javafx.stage.FileChooser
import tornadofx.FileChooserMode
import tornadofx.chooseFile
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

fun open() : BufferedImage?{
    val files = chooseFile(
        filters = arrayOf(FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")),
        mode = FileChooserMode.Single
    )

    return if(files.isNotEmpty()){
        ImageIO.read(files.first())
    }else{
        null
    }
}


fun BufferedImage.toImage() : Image{
    val os = ByteArrayOutputStream()
    ImageIO.write(this, "png", os)
    return Image(ByteArrayInputStream(os.toByteArray()))
}

fun Image.toBufferedImage() : BufferedImage{
    val bufferedImage = BufferedImage(this.width.toInt(), this.height.toInt(), BufferedImage.TYPE_INT_ARGB)

    for(y in 0 until this.height.toInt()){
        for (x in 0 until this.height.toInt()){
            bufferedImage.setRGB(x, y, this.pixelReader.getArgb(x,y))
        }
    }

    return bufferedImage
}