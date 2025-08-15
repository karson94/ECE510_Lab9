# ECE510 Lab 9 - Android App

## Setup Instructions

### API Key Configuration

This project requires a Google Maps API key to function properly. Follow these steps to set up your API keys:

1. **Copy the template file:**
   ```bash
   cp local.properties.template local.properties
   ```

2. **Get your Google Maps API key:**
   - Go to the [Google Cloud Console](https://console.cloud.google.com/apis/credentials)
   - Create a new project or select an existing one
   - Enable the Google Maps API for your project
   - Create an API key under "Credentials"
   - Optionally, restrict the API key to your specific application

3. **Add your API key:**
   - Open the `local.properties` file you just created
   - Replace `your_google_maps_api_key_here` with your actual Google Maps API key
   - Example:
     ```
     GOOGLE_MAPS_API_KEY=AIzaSyBxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
     ```

4. **Important Security Notes:**
   - The `local.properties` file is already added to `.gitignore` and will not be committed to version control
   - Never commit your actual API keys to the repository
   - Keep your API keys secure and don't share them publicly

*All of this can be observed in the manual too*
