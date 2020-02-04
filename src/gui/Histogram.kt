package gui

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartFrame
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.statistics.HistogramType
import org.jfree.data.xy.XYDataset
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.awt.image.BufferedImage

class Histogram (image: BufferedImage) {
    val dataset = XYSeriesCollection()

    init {
        val red =IntArray(256)
        val green = IntArray(256)
        val blue = IntArray(256)

        for(x in 0 until image.width){
            for(y in 0 until image.height){
                val color = image.getRGB(x,y)
                red[(color and 0x00FF0000).shr(16)]++
                green[(color and 0x0000FF00).shr(8)]++
                blue[color and 0x000000FF]++
            }
        }

        val redSeries = XYSeries("Red")

        for((x, y) in red.withIndex()){
            redSeries.add(x, y)
        }

        val greenSeries = XYSeries("Green")

        for((x, y) in green.withIndex()){
            greenSeries.add(x, y)
        }

        val blueSeries = XYSeries("Blue")

        for((x, y) in blue.withIndex()){
            blueSeries.add(x, y)
        }

        dataset.addSeries(redSeries)
        dataset.addSeries(blueSeries)
        dataset.addSeries(greenSeries)
    }

    fun show(){
        val graph = ChartFactory.createXYLineChart("Frecuencia de colores", "Valor del canal", "Frecuencia", dataset, PlotOrientation.VERTICAL, true, false, false)
        val chart = ChartFrame("Frecuencias", graph)
        chart.pack()
        chart.isVisible = true
    }
}