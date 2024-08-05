
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import org.jetbrains.skia.Image
import platform.UIKit.UIDevice

class IOSPlatform: Platform {
    override val name: String = UIDevice.currentDevice.systemName() + " " + UIDevice.currentDevice.systemVersion
    override fun toImage(byteArray: ByteArray): ImageBitmap {
        return Image.makeFromEncoded(byteArray).toComposeImageBitmap()
    }
}

actual fun getPlatform(): Platform = IOSPlatform()