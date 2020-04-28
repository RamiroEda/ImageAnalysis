package open

import getColorChannels
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.FileChooser
import toIntColor
import tornadofx.FileChooserMode
import tornadofx.chooseDirectory
import tornadofx.chooseFile
import validateBounds
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import javax.imageio.ImageIO
import kotlin.math.max
import kotlin.random.Random

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

fun saveImage(image: Image, title: String){
    val folder = chooseDirectory()

    if(folder != null){
        ImageIO.write(image.toBufferedImage(), "png", File("${folder.absolutePath}/${title}_${Calendar.getInstance().timeInMillis}.png"))
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

fun addNoise(image: Image, noise: Int) : BufferedImage{
    val bufferedImage = image.toBufferedImage()
    val channels = noise.getColorChannels()
    val randomInt = Random(Calendar.getInstance().timeInMillis)

    for (y in 0 until  bufferedImage.height){
        for (x in 0 until bufferedImage.width){
            if(randomInt.nextInt()%100 == 0){
                val color = bufferedImage.getRGB(x,y).getColorChannels()
                bufferedImage.setRGB(x,y,
                    intArrayOf(255,
                        (color[1]+channels[1]).validateBounds(),
                        (color[2]+channels[2]).validateBounds(),
                        (color[3]+channels[3]).validateBounds()).toIntColor())
            }
        }
    }


    return bufferedImage
}