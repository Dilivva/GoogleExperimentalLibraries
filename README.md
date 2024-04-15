## Blueline: Kotlin Multiplatform Bluetooth Printing

**Blueline** is a Kotlin multiplatform library that simplifies Bluetooth printer integration in your applications. It provides a platform-agnostic API for common printer operations across various platforms (Android, iOS, etc.).

**Key Features:**

* **Effortless Printer Interaction:** Discover, connect, and manage Bluetooth printers with ease.
* **Rich Text Formatting:** Enhance your printouts with options for alignment, font, style, and color.
* **Seamless Image Printing:** Print images directly from your application.
* **Custom Command Support:** Send any printer-specific commands for advanced control.
* **Write Once, Run Everywhere:** Develop your printing logic once and deploy it across platforms with minimal adjustments.

**Benefits:**

* **Reduced Code Complexity:** Blueline's unified API simplifies printer integration, saving you development time.
* **Improved Maintainability:** Manage your printing logic in a single codebase for better maintainability.
* **Enhanced User Experience:** Offer rich printing capabilities to your users on various platforms.


## Getting Started

Blueline is easy to integrate into your Kotlin multiplatform project. Here's a quick guide:

1. **Add Blueline to your dependencies:**

   ```kotlin
   // In your build.gradle.kts file
   repositories {
       mavenCentral()
   }
   
   dependencies {
       val bluelineVersion = "1.0.0" // Replace with the latest version
       commonMainImplementation("com.example.blueline:blueline:$bluelineVersion")
   }
   ```

2. **Explore the API:**

   Refer to the Blueline documentation for detailed information about available functionalities and usage examples. (**Note:** Replace the placeholder link with your actual documentation location)

## Usage Example

Here's a basic example demonstrating how to print some formatted text and an image:

```kotlin
import com.example.blueline.*

fun main() {
    val printer = connectToPrinter() // Replace with Blueline's connection logic

    printer.print(
        styledText(
            "This is a formatted text!",
            alignment = Config.Alignment.CENTER,
            font = Config.Font.LARGE,
            style = Config.Style.BOLD
        ) + "\n" +
                appendImage(getImageFromResource(R.drawable.my_image))
    )
}
```

This example demonstrates:

* Connecting to a printer (replace with Blueline's connection logic).
* Printing formatted text with alignment, font, and style options.
* Printing an image from a resource.

**Remember:** Replace the placeholder functions with actual Blueline functions for connecting, retrieving images, etc. Refer to the full documentation for details.

## Contributing

We welcome contributions to Blueline! Please see the CONTRIBUTING.md file for guidelines on how to contribute.

## License

Blueline is licensed under the MIT License 2.0. See the LICENSE file for details.
