
# Google Places
This lets you launch the google places widget from your compose multiplatform code

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
            implementation("com.dilivva:google-places:${version}")
        } 
    }
   }
   ```

2. **Explore the API:**

## Usage Example

```kotlin
//Initialize places sdk
GooglePlace.initialize("key")

//in your composable
@Composable
fun SearchScreen(){
   val googlePlaces = rememberGooglePlaces(
      config = PlacesConfig(fields = PlacesFields.entries.toList(), countries = listOf("ng")),
      onResult = {
         when(it){
            is PlaceResult.Cancelled -> println("Closed")
            is PlaceResult.Failure -> it.error
            is PlaceResult.Success -> it.place
         }
      })
   
   //Launch the search widget
   Button(
      onClick = { googlePlaces.launch() },
      enabled = true
   ) {
      Text("Search place")
   }
}
```
