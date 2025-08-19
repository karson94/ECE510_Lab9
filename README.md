# ECE510 Lab 9 - Android Environmental Sensor App

This Android app displays real-time environmental sensor data from Firebase and shows sensor locations on Google Maps.

## Quick Start for Students

### 1. Clone and Build
```bash
git clone [your-repo-url]
cd ECE510_Lab9
./gradlew :app:assembleDebug
```

The app will build and run with demo data. **No special Java versions or Android SDK setup needed** - Gradle handles this automatically.

### 2. Setup Your API Keys

**Google Maps API:**
1. Copy the template: `cp local.properties.template local.properties`
2. Get your API key from [Google Cloud Console](https://console.cloud.google.com/apis/credentials)
3. Enable the "Maps SDK for Android" API
4. Edit `local.properties` and replace `your_google_maps_api_key_here` with your actual key

**Firebase Setup:**
1. Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
2. Add an Android app with package name: `com.example.ece510_exp9`
3. Download `google-services.json` from your Firebase project
4. Copy the template: `cp app/google-services.json.template app/google-services.json`
5. Replace the placeholder values in `app/google-services.json` with your actual Firebase config
6. Enable "Realtime Database" in your Firebase project console

### 3. Run
Open in Android Studio or run:
```bash
./gradlew :app:installDebug
```

## Notes
- `local.properties` and `google-services.json` are ignored by Git for security
- App works without Firebase (shows loading state) but needs Maps API for location features  
- Use the template files (`*.template`) to set up your own credentials
- Tested on Android API 24+ (Android 7.0+)