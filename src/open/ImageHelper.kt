package open

import javafx.scene.image.Image
import javafx.stage.FileChooser
import tornadofx.FileChooserMode
import tornadofx.chooseFile
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO
import kotlin.math.max

const val MAX_SIZE : Double = 300.0

fun open() : BufferedImage?{
    val files = chooseFile(
        filters = arrayOf(FileChooser.ExtensionFilter("Images", "*.png", "*.jpg", "*.jpeg")),
        mode = FileChooserMode.Single
    )

    return if(files.isNotEmpty()){
        scaleImage(ImageIO.read(files.first()))
    }else{
        null
    }
}

fun scaleImage(image: BufferedImage) : BufferedImage{
    val compressionRatio = MAX_SIZE/max(image.width, image.height)
    val tmp = image.getScaledInstance((image.width*compressionRatio).toInt(), (image.height*compressionRatio).toInt(), java.awt.Image.SCALE_SMOOTH)
    val bufferedImage = BufferedImage((image.width*compressionRatio).toInt(), (image.height*compressionRatio).toInt(), BufferedImage.TYPE_INT_ARGB)

   val graphics2D = bufferedImage.createGraphics()
    graphics2D.drawImage(tmp,0,0,null)
    graphics2D.dispose()

    return bufferedImage
}

fun BufferedImage.toImage() : Image{
    val os = ByteArrayOutputStream()
    ImageIO.write(this, "png", os)
    return Image(ByteArrayInputStream(os.toByteArray()))
}

fun Image.toBufferedImage() : BufferedImage{
    val bufferedImage = BufferedImage(this.width.toInt(), this.height.toInt(), BufferedImage.TYPE_INT_ARGB)

    for(y in 0 until this.height.toInt()){
        for (x in 0 until this.width.toInt()){
            bufferedImage.setRGB(x, y, this.pixelReader.getArgb(x,y))
        }
    }

    return bufferedImage
}