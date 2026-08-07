# Setup Guide for Project Anti-Gravity

This guide helps you start building Project Anti-Gravity in Android Studio with a premium, scalable architecture.

## 1. Prerequisites

- Android Studio Hedgehog or newer
- JDK 17 or newer
- An Android emulator or real device
- A Google account for Firebase / Google Cloud setup
- A basic understanding of Kotlin and Jetpack Compose

## 2. Project Setup

1. Open the project in Android Studio.
2. Sync Gradle files.
3. Run the app on a connected device or emulator.

## 3. Firebase Setup (Recommended for MVP)

### Create a Firebase Project
1. Go to https://console.firebase.google.com/
2. Create a new project named Anti-Gravity.
3. Register your Android app with the package name used by the project.
4. Download the google-services.json file and place it in the app folder.

### Enable Firebase Services
Enable:
- Authentication
- Firestore Database
- Cloud Storage
- Cloud Functions (optional for AI auto-labeling)

### Authentication
Enable Google Sign-In in Firebase Authentication.

## 4. Recommended Tech Stack

### Android App
- Kotlin
- Jetpack Compose
- CameraX
- Google ML Kit
- Room Database
- Coroutines
- Navigation Compose

### Backend / Cloud
- Firebase Firestore
- Firebase Storage
- Firebase Authentication
- Firebase Cloud Functions
- Gemini API for AI labeling

## 5. Core Architecture Plan

### A. Capture Flow
- Use CameraX to capture book pages or question images.
- Use ML Kit to extract text from the image.
- Show the OCR result in a large verification popup.
- Let the user confirm, edit, and save the question.

### B. Question Storage
- Save the verified question text.
- Attach image references if a diagram or question image exists.
- Store data in Firestore for central access.

### C. AI Labeling
- When a question is uploaded, call Gemini AI.
- Automatically generate:
  - subject
  - topic
  - subtopic
  - difficulty
  - exam type / PYQ hint

### D. Practice Engine
- Query questions based on:
  - topic
  - difficulty
  - PYQ relevance
  - user performance history
- Build adaptive practice flows such as:
  - easy to medium to hard progression
  - boosted marks mode
  - weak-topic reinforcement

## 6. Suggested Database Structure

Each question can be stored as a document like this:

```json
{
  "id": "question_001",
  "uploaderId": "user_123",
  "subject": "Physics",
  "topic": "Gravitation",
  "subTopic": "Escape Velocity",
  "questionText": "A body is projected ...",
  "options": [
    { "id": "A", "text": "Option 1", "isCorrect": false },
    { "id": "B", "text": "Option 2", "isCorrect": true }
  ],
  "imageUrl": "",
  "aiMetadata": {
    "difficulty": "Medium",
    "isPYQ": true,
    "examCategory": "JEE Main"
  }
}
```

## 7. Premium Features to Add Next

- Mistake Vault
- Adaptive difficulty engine
- AI explanation for wrong answers
- Offline-first practice mode
- Analytics dashboard for accuracy, time spent, and weak topics
- Leaderboards and streaks

## 8. Development Roadmap

### Phase 1
- Camera capture
- OCR extraction
- Verification popup
- Save to database

### Phase 2
- AI labeling
- Image upload
- Practice screen

### Phase 3
- Adaptive engine
- Analytics
- Mistake Vault
- Premium UI polish

## 9. Notes

This project is ambitious and can grow into a strong EdTech product. Start with a simple MVP first, then add premium features step by step.
