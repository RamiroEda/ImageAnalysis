package gui

import javafx.scene.chart.LineChart
import javafx.scene.chart.NumberAxis
import javafx.scene.chart.XYChart
import java.awt.image.BufferedImage

class Histogram (image: BufferedImage, private val channelVisibilityFlags : Int, title: String) {
    val chart = LineChart<Number, Number>(NumberAxis(), NumberAxis())

    init {
        chart.title = "Histograma de Frecuencias de $title"
        chart.createSymbols = false
        chart.animated = false
        initDataset(image)
    }

    private fun initDataset(image: BufferedImage){
        this.chart.data.clear()
        val red = IntArray(256)
        val green = IntArray(256)
        val blue = IntArray(256)
        val gray = IntArray(256)

        for(x in 0 until image.width){
            for(y in 0 until image.height){
                val color = image.getRGB(x,y)
                if(channelVisibilityFlags and 0b0100 == 0b100){
                    red[(color and 0x00FF0000).shr(16)]++
                }
                if(channelVisibilityFlags and 0b10 == 0b10) {
                    green[(color and 0x0000FF00).shr(8)]++
                }
                if(channelVisibilityFlags and 0b01 == 0b01) {
                    blue[color and 0x000000FF]++
                }
                if(channelVisibilityFlags and 0b01000 == 0b1000) {
                    gray[((color and 0x00FF0000).shr(16) + (color and 0x0000FF00).shr(8) + (color and 0x000000FF)) / 3]++
                }
            }
        }

        if(channelVisibilityFlags and 0b0100 == 0b100){
            val redSeries = XYChart.Series<Number, Number>()
            redSeries.name = "Red"

            redSeries.data.addAll(red.mapIndexed { index, el ->
                XYChart.Data<Number, Number>(index, el)
            })

            chart.data.add(redSeries)
        }


        if(channelVisibilityFlags and 0b01000 == 0b1000) {
            val graySeries = XYChart.Series<Number, Number>()
            graySeries.name = "Gray"

            graySeries.data.addAll(gray.mapIndexed { index, el ->
                XYChart.Data<Number, Number>(index, el)
            })

            chart.data.add(graySeries)
        }

        if(channelVisibilityFlags and 0b10 == 0b10) {
            val greenSeries =XYChart.Series<Number, Number>()
            greenSeries.name = "Green"

            greenSeries.data.addAll(green.mapIndexed { index, el ->
                XYChart.Data<Number, Number>(index, el)
            })

            chart.data.add(greenSeries)
        }

        if(channelVisibilityFlags and 0b01 == 0b01) {
            val blueSeries = XYChart.Series<Number, Number>()
            blueSeries.name = "Blue"

            blueSeries.data.addAll(blue.mapIndexed { index, el ->
                XYChart.Data<Number, Number>(index, el)
            })

            chart.data.add(blueSeries)
        }
    }

    fun update(image: BufferedImage){
        chart.yAxis.isAutoRanging = false
        initDataset(image)
    }
}