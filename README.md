# Project Anti-Gravity 🚀

Project Anti-Gravity is an ambitious Android app built in Kotlin to help students prepare for exams using their existing books and study material. The core idea is simple but powerful: a student can capture a question photo, the app extracts the text and options, shows a review popup for confirmation, and saves the question into a central database for future practice.

This project is designed to grow into a premium EdTech platform with AI-powered question understanding, personalized practice, adaptive difficulty, and analytics-driven learning.

## Why this project matters

Modern exam preparation is often scattered across books, notes, and random practice material. Project Anti-Gravity aims to unify that process by turning physical questions into structured digital content that can be reused for:

- smart revision
- targeted practice
- adaptive mock tests
- weak-topic reinforcement
- personalized study analytics

## Core vision

Students should be able to:
- click a photo of a question from a book
- extract the text and options automatically
- review the detected content in a large confirmation popup
- attach an image if the question has diagrams or visuals
- save the question into a central database
- practice questions based on topic, difficulty, PYQ relevance, and learning performance

## Premium features planned

- Smart question capture from physical books
- OCR-based text extraction using ML Kit
- Large verification popup for editing and confirming extracted content
- Image attachment support for diagrams and visual questions
- AI auto-labeling for topic, subtopic, difficulty, and exam type
- Adaptive practice engine for easy/medium/hard progression
- Mistake Vault for incorrect questions
- AI explanation for wrong answers
- Offline-first practice support
- Analytics dashboard for accuracy, time spent, and weak areas

## Recommended tech stack

### Android app
- Kotlin
- Jetpack Compose
- CameraX
- Google ML Kit
- Room Database
- Coroutines
- Navigation Compose

### Backend and cloud
- Firebase Authentication
- Firestore Database
- Firebase Storage
- Firebase Cloud Functions
- Gemini API for AI labeling

## Architecture flow

1. Capture a question photo using the camera.
2. Extract text using ML Kit OCR.
3. Show a large verification popup for the student to review and edit.
4. Attach a diagram or supporting image if needed.
5. Save the verified question into Firestore or a central cloud database.
6. Use AI to auto-label the question with subject, topic, subtopic, and difficulty.
7. Build practice sessions using adaptive logic and user analytics.

## Project structure

```text
app/
  src/main/java/com/edtech/ranks/
    data/           # Data layer and remote integration
    ui/
      auth/         # Authentication UI
      camera/       # Camera capture and OCR workflow
      home/         # Dashboard UI
      navigation/   # App navigation
      practice/     # Practice screen flow
      theme/        # App theme and styling
    MainActivity.kt
```

## Getting started

For setup instructions, see [SETUP.md](SETUP.md).

## Current status

This project is still in the early stages of development. The foundation is being built around camera capture, OCR, question review, and cloud-based question storage.

## License

This project is currently unlicensed. Add a license if you want to share or distribute it publicly.
