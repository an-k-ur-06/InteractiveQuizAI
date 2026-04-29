# 📱 Interactive Quiz AI

An Android application that generates interactive quizzes using Google's Gemini AI. Users can upload documents (PDF, DOCX), and the app uses AI to automatically generate quiz questions based on the content.

## ✨ Features

- 🤖 AI-powered quiz generation using Google Gemini
- 📄 Support for PDF and DOCX file uploads
- 💾 Local database storage using Room
- 🔄 Navigation between different quiz questions
- 📊 Interactive quiz experience with instant feedback
- 🎨 Modern Material Design UI

## 🛠️ Tech Stack

- **Language:** Kotlin
- **Architecture:** MVVM with Hilt Dependency Injection
- **Database:** Android Room
- **Networking:** Retrofit + OkHttp
- **AI Integration:** Google Gemini API
- **File Parsing:** PDFBox, Apache POI
- **UI:** Material Design, ViewPager2
- **Async:** Coroutines

## 📋 Prerequisites

Before setting up the project, ensure you have:

- **Android Studio** (latest version recommended)
- **JDK 17** or higher
- **Google Gemini API Key** ([Get it here](https://makersuite.google.com/app/apikey))
- **Git** installed on your machine

## 🚀 Setup Instructions

### Step 1: Clone the Repository

```bash
git clone https://github.com/an-k-ur-06/InteractiveQuizAI.git
cd InteractiveQuizAI
```

### Step 2: Open in Android Studio

1. Launch **Android Studio**
2. Click **File** → **Open**
3. Navigate to the cloned `InteractiveQuizAI` folder
4. Click **OK** to open the project
5. Wait for Gradle to sync and download dependencies (this may take a few minutes)

### Step 3: Configure API Key

1. In the project root directory, find the file named **`local.properties.example`**
2. **Copy this file** and rename it to **`local.properties`** (in the same root directory)
3. Open the **`local.properties`** file in Android Studio
4. Replace `your_gemini_api_key_here` with your actual Gemini API key:

```properties
GEMINI_API_KEY=your_actual_api_key_here
```

**Note:** The `local.properties` file is already in `.gitignore`, so your API key will **never be committed** to version control.

### Step 4: Build the Project

1. Click **Build** → **Make Project** (or press `Ctrl+F9` / `Cmd+F9`)
2. Wait for the build to complete successfully
3. If there are any Gradle sync errors, click **File** → **Sync Now**

### Step 5: Run the App

1. Connect an Android device (API level 26+) or create an Android Virtual Device (AVD)
2. Click the **Run** button (green play icon) or press `Shift+F10`
3. Select your device and click **OK**
4. The app will install and launch on your device/emulator

## 📦 Project Structure

```
InteractiveQuizAI/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/aiquizgenerator/
│   │   │   ├── res/
│   │   │   └── AndroidManifest.xml
│   │   └── test/
│   ├── build.gradle
│   └── proguard-rules.pro
├── gradle/
├── build.gradle
├── settings.gradle
├── gradlew
├── local.properties.example
└── README.md
```

## 🔐 API Key Setup

### Getting Your Gemini API Key:

1. Visit [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Click **Create API Key**
3. Copy your API key
4. Paste it in the `local.properties` file (see Step 3 above)

## ⚠️ Important Notes

- **Never commit `local.properties`** to version control
- **Never hardcode API keys** in your source code
- The `local.properties` file is automatically excluded via `.gitignore`
- Ensure your Gemini API key has sufficient quota for your usage

## 🤝 Contributing

Feel free to fork this repository and submit pull requests for any improvements!

## 📄 License

This project is open source and available under the MIT License.

## 🆘 Troubleshooting

### Gradle Sync Issues
- Try **File** → **Sync Now** again
- Delete `.gradle` and `.idea` folders, then sync again

### Build Errors
- Ensure JDK 17 is being used: **File** → **Project Structure** → **SDK Location**
- Check that all dependencies are downloaded properly

### API Key Errors
- Verify the `local.properties` file exists in the root directory
- Ensure your API key is correctly copied without extra spaces
- Check your Gemini API quota limits

---

**Happy coding! 🎉**
