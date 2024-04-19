![badge](https://camo.githubusercontent.com/8ce65a3be14c94be47bceb832f55e376253dde249232136976baacb38b85438c/687474703a2f2f696d672e736869656c64732e696f2f62616467652f706c6174666f726d2d616e64726f69642d3645444238442e7376673f7374796c653d666c6174)
![badge](https://camo.githubusercontent.com/549a60a8c72c6b9ad3229b3d45dbf8cbd0f2bc9493b95463b2004b3546a36923/687474703a2f2f696d672e736869656c64732e696f2f62616467652f706c6174666f726d2d696f732d4344434443442e7376673f7374796c653d666c6174)
[![GitHub release (latest by date)](https://img.shields.io/github/v/release/Dilivva/GoogleExperimentalLibraries)](https://github.com/Dilivva/GoogleExperimentalLibraries/releases)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.dilivva/google-places/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.dilivva/google-places)

# GoogleExperimentalLibraries

**GoogleExperimentalLibraries** is a set of Google's libraries converted to Kotlin multiplatform. It provides a common API for google stuffs across mobile platforms (Android, iOS).

**Google Libraries:**

* **Google Places:** https://developers.google.com/maps/documentation/places/web-service.


## Getting Started

1. **Add to your dependencies:**

   ```kotlin
   // In your root build.gradle.kts file
   repositories {
       mavenCentral()
   }
   kotlin{
       sourceSets {
         commonMain.dependencies{
            implementation("com.dilivva:$library:${bluelineVersion}")
        } 
    }
   }
   ```

2. **Explore the API:**

## Usage Example


## Contributing

We welcome contributions!

## License

GoogleExperimentalLibraries is licensed under the MIT License. See the LICENSE file for details.
