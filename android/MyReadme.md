Steps I had to take

--------------------------------------------------------------------------------------------------------------------------------------------------------------------
Problem: when trying to run I would get a NullPointerException coming from the MainActivity.kt file.

file: MainActivity.kt in ui
line: 27
/*
The source of the NullPointerException and the reason for the transaction failure.
The core issue is that we were trying to access and use the textView before we've set the content view using setContentView().
To fix that I moved setContentView up.
*/
setContentView(R.layout.activity_main)
val textView = findViewById<TextView>(R.id.text_view_user)
viewModel.userName.observeForever {
textView.text = it
}
--------------------------------------------------------------------------------------------------------------------------------------------------------------------
Problem: when trying to compile I would get an error related to to the Room Database in Android.

file: User.kt in entities

we get an error related to to the Room Database in Android: An entity must have at least 1 field annotated with @PrimaryKey

fix: added import androidx.room.PrimaryKey

@PrimaryKey(autoGenerate = true) val id: Int,
val name: String,
val image: String
)

!!! note !!! not sure if we need the autoGenerate or not yet
--------------------------------------------------------------------------------------------------------------------------------------------------------------------
Problem: when building I would get a warning : warning: Schema export directory is not provided to the annotation processor so we cannot export the schema.

file: build.gradle.kts in Module:app

fix: added the following lines to the build.gradle.kts at line 20

javaCompileOptions {
annotationProcessorOptions {
arguments.put("room.schemaLocation", "$projectDir/schemas")
}
}
--------------------------------------------------------------------------------------------------------------------------------------------------------------------
Problem: When running the app crashes

Logcat output: 2025-02-22 16:32:33.875  7304-7409  AndroidRuntime          com.android.support.exercise         E  FATAL EXCEPTION: OkHttp Dispatcher
Process: com.android.support.exercise, PID: 7304
java.lang.SecurityException: Permission denied (missing INTERNET permission?)

according to the Logcat logs, the app does not have internet permissions and that is causing it to crash, so we need to give the app internet permissions

file: AndroidManifest.xml
added the following lines to the AndroidManifest.xml at line 4
<!--
      We need to give INTERNET Permissions because Glide library uses this permission.
      This line tells the Android system that your app needs permission to access the internet.
      -->
    <uses-permission android:name="android.permission.INTERNET" />

---------------------------------------------------------------------------------------------------------------------------------------------------------------------
Problem: Glide Configuration Issue

There is a warning: Failed to find GeneratedAppGlideModule.
we should include an annotationProcessor compile dependency on com.github.bumptech.glide:compiler in the application and a @GlideModule annotated AppGlideModule implementation or LibraryGlideModules will be silently ignored.

file: build.gradle.kts in Module:app

kapt(libs.glide.compiler) // Add this line for Glide annotation processing in line 63

Made a new Kotlin class called MyAppGlideModule that extends AppGlideModule.



Problem: The log shows an error related to the Room database: Room cannot verify the data integrity. I've changed schema but forgot to update the version number.

file: AppDatabase.kt in bd

fix:  @Database(entities = [User::class], version = 2) // Increment the version to 2 in line 10

had to change the function of initDatabase() to the following so that it doesn't run on the main thread.
because database operations can take time, and if they happen on the main thread, they will freeze your UI, making your app unresponsive.

Problem: The loadUserData() and loadPostsData() are not called when the view model is created.

file: MainActivityViewModel.kt in ui.viewmodel



Problem: Incorrect ViewModel Creation: we're creating the ViewModel directly: private val viewModel = MainActivityViewModel(). This is bypassing the ViewModel's lifecycle management.

file: MainActivity.kt














