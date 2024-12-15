package nl.joozd.utils.imaging

import nl.joozd.utils.linearalgebra.IntVectorWithValue
import java.awt.AlphaComposite
import java.awt.image.BufferedImage
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * Builds a .gif animation
 * @property delay The delay between two frames (default 100 millis)
 * @property loop if true, the animation will loop (default true)
 * @property scale The size of every pixel (default 5x5 pixels)
 * @property colorMaker a [ColorMaker] that defines what value gets what color.
 *
 * use:
 * val gifMaker = GifSequenceBuilder(delay = 100, loop = true, scale = 3, colorMaker = myColorMaker)
 * repeat(10){
 *  gifMaker.addCoordinates(someCoordinates)
 * gifMaker.writeGif("filename.gif")
 */
class GifSequenceBuilder<T>(
    private val delay: Duration = 100.milliseconds,
    private val loop: Boolean = true,
    val scale: Int = 5,
    private val colorMaker: ColorMaker<T>
) {
    private val frames = ArrayList<BufferedImage>()

    private fun width() = frames.maxOf { it.width }
    private fun height() = frames.maxOf { it.height }

    fun addFrame(frame: BufferedImage){
        frames.add(frame)
    }

    /**
     * Add a frame to this gif
     * @param coordinates A collection of [IntVectorWithValue] objects to be plotted
     * @param overrideColorMaker A new ColorMaker, to override the one defined in the creation of this GifSequenceBuilder
     * @return this GifSequenceBuilder instance
     */
    fun addCoordinates(coordinates: Collection<IntVectorWithValue<T>>, overrideColorMaker: ColorMaker<T>? = null): GifSequenceBuilder<T>{
        addFrame(PNGMap(coordinates, scale, overrideColorMaker ?: colorMaker).makeBufferedImage())
        return this
    }

    /**
     * Writes the currently created gif to disk.
     * @param fileName The name of the file to write to
     */
    fun writeGif(fileName: String){
        if (frames.isEmpty()) error ("Cannot build an empty gif")
        GifSequenceWriter(fileName, frames.first().type, delay.inWholeMilliseconds.toInt(), loop).use{ writer ->
            frames.forEach {
                writer.writeToSequence(prepareFrame(it))
            }
        }
    }

    private fun prepareFrame(frame: BufferedImage): BufferedImage{
        val w = width()
        val h = height()
        if (frame.width == w && frame.height == h)
            return frame

        val xOffset = (w - frame.width) / 2
        val yOffset = (h - frame.height) / 2
        val newFrame = BufferedImage(w,h,frame.type)
        newFrame.createGraphics().apply{
            composite = AlphaComposite.Src
            drawImage(frame,
                xOffset,
                yOffset,
                null
            )
            dispose()
        }
        return newFrame
    }
}