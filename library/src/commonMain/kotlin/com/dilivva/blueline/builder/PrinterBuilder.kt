/*
 * Copyright (C) 2024, Send24.
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT
 * SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */


package com.dilivva.blueline.builder

import com.dilivva.blueline.commands.PrintCommands
import com.dilivva.blueline.commands.PrinterEncoding


typealias PrintData = Pair<ByteArray, ByteArray?>

/**
 * Builds a byte array containing the print data based on the provided configuration.
 * @param data A lambda expression that configures the `PrinterBuilder` for defining the print data.
 * @return [PrintData] A pair containing the primary byte array with the print data and an optional secondary byte array
 *         with image data (if applicable).
 */

fun buildPrintData(data: PrinterBuilder.() -> Unit): PrintData {
    return PrinterBuilder().apply(data).build()
}

class PrinterBuilder{

    private var bytesToSend = byteArrayOf()
    private val printerEncoding = PrinterEncoding()
    private var imagePreview: ByteArray? = null


    /**
     * Appends formatted text to the print data buffer.
     * @param text A lambda expression that configures the [TextBuilder] for defining the formatted text.
     */
    fun appendText(text: TextBuilder.() -> Unit){
        if (bytesToSend.isNotEmpty()) resetPrinter()
        bytesToSend += printerEncoding.getCommand()
        val data = TextBuilder().apply(text).build()
        bytesToSend += data
    }
    /**
     * Appends an image to the print data buffer.
     * @param image A lambda expression that configures the `ImageBuilder` for defining the image to be printed.
     */
    fun appendImage(image: ImageBuilder.() -> Unit){
        if (bytesToSend.isNotEmpty()) resetPrinter()
        val imageBuilder = ImageBuilder().apply(image)
        bytesToSend += imageBuilder.build()
        imagePreview = imageBuilder.imagePreview
    }

    /**
     * Inserts line breaks into the print data buffer.
     * This function adds line breaks to the buffer. It takes an optional [times] parameter
     * which defaults to 1.
     * @param times The number of line breaks to insert (defaults to 1).
     */
    fun newLine(times: Int = 1){
        repeat(times){
            bytesToSend += PrintCommands.NEW_LINE
        }
    }

    private fun resetPrinter(){
        bytesToSend += PrintCommands.RESET_CONFIGURATION
    }

    internal fun build(): PrintData {
        newLine(5)
        return bytesToSend to imagePreview
    }

}

