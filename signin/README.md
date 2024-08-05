# *NOTE:* this is highly experimental, regardless we use this code in production and it's stable.

# Google SignIn
This lets you implement google sign-in from your compose multiplatform code. Currently only supports Android and iOS.

## Getting Started

1. **Setup your project on google cloud for Android and iOS:** https://developers.google.com/identity

2. **Add to your dependencies:**

   ```kotlin
   // In your root build.gradle.kts file
   repositories {
       mavenCentral()
   }
   kotlin{
       sourceSets {
         commonMain.dependencies{
            implementation("com.dilivva:google-signin:${version}")
        } 
    }
   }
   ```

3. Add [-ObjC linker](https://developer.apple.com/library/content/qa/qa1490/_index.html) for iOS on Xcode

4. [Download GoogleSignIn.bundle](https://github.com/Dilivva/GoogleExperimentalLibraries/tree/master/signin/libs/GoogleSignIn.bundle.zip) and 
[Download GTMSessionFetcher_Core_Privacy.bundle](https://github.com/Dilivva/GoogleExperimentalLibraries/tree/master/signin/libs/GTMSessionFetcher_Core_Privacy.bundle.zip) resources,
      extract and move GoogleSignIn.bundle & GTMSessionFetcher_Core_Privacy.bundle to Xcode.

5. Initialize with your client ID on Android:

```kotlin
GoogleSignInConfig.configure("client_id")
```

6. **Explore the API:**

## Usage Example

```kotlin
//in your composable
@Composable
fun LoginScreen(){
   val signIn = rememberGoogleSignIn {
      when(it){
         is GoogleSignInResult.Error -> println("GoogleSignInError: ${it.message}")
         is GoogleSignInUser -> { println("User: $it") }
         GoogleSignInResult.NoResult -> println("No result")
      }
   }
   
   //Launch the search widget
   Button(
      onClick = { signIn.signIn() },
      enabled = true
   ) {
      Text("Search place")
   }
   
   //Get previous signed in user, 
   signIn.restorePreviousSignIn()
   // Sign-out
   signIn.signOut()
}
```

