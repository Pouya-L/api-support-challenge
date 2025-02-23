# Posts App - Technical Challenge

## CODING ASSIGNMENT 💻
This project is an Android application that retrieves posts from an API, displays user information, and stores user data locally using Room. 
The goal of this task was to identify and fix issues in the codebase to make the app functional while following Android development best practices.

---

## APP DESCRIPTION

The app consists of the following key components:
### KEY COMPONENTS
1. **Posts**: The app fetches posts from the API endpoint `https://jsonplaceholder.typicode.com/posts` using **Retrofit2** for network communication.
2. **User Information**: In the MainActivity, it is displayed the name and image of the **User** of this Application.
3. **Image Loading**: The user's image is loaded using **Glide**.
4. **Database**: User information is stored locally in a **Room** database. The app will retrieve this data on launch.

---

## INSTRUCTIONS
The task involved:
- **Identify the issues** in the provided codebase.
- **Fix the identified issues** to make the app functional. Your fixes should ensure that:
  1. The app retrieves posts from the API and displays them.
  2. The app shows the user name and image correctly.
  3. The app interacts with the local Room database properly, ensuring that user data is correctly stored and retrieved.
- Ensure that the app handles **networking**, **image loading**, and **database operations** in a way that follows Android development best practices.

---

## **Problems and Fixes**

Below are the issues encountered during development and the steps taken to resolve them:

---

### **1. NullPointerException in MainActivity**

**Problem**:  
The app crashed with a `NullPointerException` in `MainActivity.kt` because the `TextView` was accessed before `setContentView()` was called.

**File**: `MainActivity.kt`  
**Fix**:  
Moved `setContentView(R.layout.activity_main)` to the top of the `onCreate()` method to ensure the view is initialized before accessing any UI components.
```kotlin
override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main) // Moved to the top
    val textView = findViewById<TextView>(R.id.text_view_user)
    viewModel.userName.observeForever {
        textView.text = it
    }
}
```

### **2. Room Database Primary Key Error** 

**Problem**:  
The app failed to compile with the error:
`An entity must have at least 1 field annotated with @PrimaryKey`

**File**: `User.kt`  
**Fix**:  
Added the `@PrimaryKey` annotation to the `id` field in the `User` entity.
```kotlin
@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val image: String
)
```

### **3.  Room Schema Export Warning** 
**Problem**:  
A warning appeared during the build:
`Schema export directory is not provided to the annotation processor so we cannot export the schema`

**File**: `build.gradle.kts`  
**Fix**:  
Added the following configuration to the `build.gradle.kts` file to specify the schema export directory.
```kotlin
android {
  ...
  defaultConfig {
    ...
    javaCompileOptions {
      annotationProcessorOptions {
        arguments.put("room.schemaLocation", "$projectDir/schemas")
      }
    }
  }
}
```

### **4.  Missing Internet Permission**
**Problem**:  
The app crashed with a `SecurityException` due to missing internet permissions.

**File**: `AndroidManifest.xml`  
**Fix**:  
Added the `INTERNET` permission to the `AndroidManifest.xml` file.
```xml
<uses-permission android:name="android.permission.INTERNET" />
```

### **5. Glide Configuration Issue**
**Problem**:  
A warning appeared in Logcat when running the app:
`Failed to find GeneratedAppGlideModule.`

**File**: `build.gradle.kts` and `MyAppGlideModule.kt`
**Fix**:  
1. Added the Glide annotation processor dependency to `build.gradle.kts`.
2. Created a `MyAppGlideModule` class to configure Glide.
```kotlin
@GlideModule
class MyAppGlideModule : AppGlideModule()
```

### **6.  Room Database Schema Version Mismatch**
**Problem**:  
A warning appeared in Logcat when running the app:
`Room cannot verify the data integrity. Looks like you've changed the schema but forgot to update the version number.`

**File**: `AppDatabase.kt`  
**Fix**:  
1. Incremented the database version to `2`.
```kotlin
@Database(entities = [User::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
  ...
}
```

2. Ensured database operations run on a background thread to avoid freezing the UI.
```kotlin
private fun initDatabase(context: Context) {
  CoroutineScope(Dispatchers.IO).launch {
    val database = getDatabase(context)
    if (database.userDao().getUserCount() == 0) {
      database.userDao().insertUser(
        User(
          1,
          "John Doe",
          "https://example.com/image.jpg"
        )
      )
    }
  }
}
```

### **7.  Posts Not Displaying in RecyclerView**
**Problem**:  
The posts were not showing up in the RecyclerView.

**File**: `PostAdapter.kt`  
**Fix**:
The `getItemCount()v method was returning `0`, causing the RecyclerView to display no items. Updated it to return the size of the `postList`.
```kotlin
override fun getItemCount(): Int = postList.size
```

## How to Run the Project

1. Clone the repository.
2. Open the project in Android Studio.
3. Build and run the app on an emulator or physical device.

## Dependencies

**Retrofit2**: For network communication.
**Glide**: For image loading.
**Room**: For local database storage.
**Coroutines**: For asynchronous programming.

## Conclusion
This task was a great opportunity to practice debugging and fixing common Android development issues. By addressing problems related to UI initialization, database configuration, permissions, and RecyclerView setup, the app is now functional and follows the requirements.