package gui

import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartFrame
import org.jfree.chart.plot.PlotOrientation
import org.jfree.data.xy.XYSeries
import org.jfree.data.xy.XYSeriesCollection
import java.awt.Color
import java.awt.Paint
import java.awt.image.BufferedImage

class Histogram (image: BufferedImage, private val channelVisibilityFlags : Int, private val isSeparated : Boolean = false) {
    private val dataset = XYSeriesCollection()

    init {
        val red =IntArray(256)
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
            val redSeries = XYSeries("Red")

            for((x, y) in red.withIndex()){
                redSeries.add(x, y)
            }

            dataset.addSeries(redSeries)
        }
        if(channelVisibilityFlags and 0b10 == 0b10) {
            val greenSeries = XYSeries("Green")

            for((x, y) in green.withIndex()){
                greenSeries.add(x, y)
            }

            dataset.addSeries(greenSeries)
        }
        if(channelVisibilityFlags and 0b01 == 0b01) {
            val blueSeries = XYSeries("Blue")

            for((x, y) in blue.withIndex()){
                blueSeries.add(x, y)
            }

            dataset.addSeries(blueSeries)
        }
        if(channelVisibilityFlags and 0b01000 == 0b1000) {
            val graySeries = XYSeries("Brightness")

            for((x, y) in gray.withIndex()){
                graySeries.add(x, y)
            }

            dataset.addSeries(graySeries)
        }
    }

    fun show(){
        if(isSeparated){
            showMultiple()
        }else{
            showSingle()
        }
    }

    private fun showSingle(){
        val graph = ChartFactory.createXYAreaChart("Frecuencia de colores", "Valor del canal", "Frecuencia", dataset, PlotOrientation.VERTICAL, true, false, false)

        var channelCount = 0

        if(channelVisibilityFlags and 0b0100 == 0b100){
            graph.xyPlot.renderer.setSeriesPaint(channelCount, Color(255,0,0))
            channelCount++
        }
        if(channelVisibilityFlags and 0b10 == 0b10) {
            graph.xyPlot.renderer.setSeriesPaint(channelCount, Color(0,255,0))
            channelCount++
        }
        if(channelVisibilityFlags and 0b01 == 0b01) {
            graph.xyPlot.renderer.setSeriesPaint(channelCount, Color(0,0,255))
            channelCount++
        }
        if(channelVisibilityFlags and 0b01000 == 0b1000) {
            graph.xyPlot.renderer.setSeriesPaint(channelCount, Color(0,0,0))
        }

        val chart = ChartFrame("Frecuencias", graph)
        chart.pack()
        chart.isVisible = true
    }

    private fun showMultiple(){
        var channelCount = 0
        if(channelVisibilityFlags and 0b0100 == 0b100){
            val redCollection = XYSeriesCollection()
            redCollection.addSeries(dataset.getSeries(channelCount))
            val graph = ChartFactory.createXYAreaChart("Frecuencia de colores", "Valor del canal", "Frecuencia", dataset, PlotOrientation.VERTICAL, true, false, false)
            graph.xyPlot.renderer.setSeriesPaint(channelCount, Color(255,0,0))
            channelCount++
            val chart = ChartFrame("Frecuencias", graph)
            chart.pack()
            chart.isVisible = true
        }
        if(channelVisibilityFlags and 0b10 == 0b10) {
            val redCollection = XYSeriesCollection()
            redCollection.addSeries(dataset.getSeries(channelCount))
            val graph = ChartFactory.createXYAreaChart("Frecuencia de colores", "Valor del canal", "Frecuencia", dataset, PlotOrientation.VERTICAL, true, false, false)
            graph.xyPlot.renderer.setSeriesPaint(channelCount, Color(0,255,0))
            channelCount++
            val chart = ChartFrame("Frecuencias", graph)
            chart.pack()
            chart.isVisible = true
        }
        if(channelVisibilityFlags and 0b01 == 0b01) {
            val redCollection = XYSeriesCollection()
            redCollection.addSeries(dataset.getSeries(channelCount))
            val graph = ChartFactory.createXYAreaChart("Frecuencia de colores", "Valor del canal", "Frecuencia", dataset, PlotOrientation.VERTICAL, true, false, false)
            graph.xyPlot.renderer.setSeriesPaint(channelCount, Color(0,0,255))
            channelCount++
            val chart = ChartFrame("Frecuencias", graph)
            chart.pack()
            chart.isVisible = true
        }
        if(channelVisibilityFlags and 0b01000 == 0b1000) {
            val redCollection = XYSeriesCollection()
            redCollection.addSeries(dataset.getSeries(channelCount))
            val graph = ChartFactory.createXYAreaChart("Frecuencia de colores", "Valor del canal", "Frecuencia", dataset, PlotOrientation.VERTICAL, true, false, false)
            graph.xyPlot.renderer.setSeriesPaint(channelCount, Color(0,0,0))
            val chart = ChartFrame("Frecuencias", graph)
            chart.pack()
            chart.isVisible = true
        }


    }
}