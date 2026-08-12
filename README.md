# Project Anti-Gravity 🚀

Project Anti-Gravity (RANKS) is an ambitious Android app built in Kotlin to help students prepare for exams using their existing books and study material. The core idea is simple but powerful: a student can capture a question photo, the app extracts the text and options, shows a review popup for confirmation, and saves the question into a central database or private vault for future practice.

This project is designed to grow into a premium EdTech platform with AI-powered question understanding, personalized practice, adaptive difficulty, global leaderboards, and analytics-driven learning.

## Why this project matters

Modern exam preparation is often scattered across books, notes, and random practice material. Project Anti-Gravity aims to unify that process by turning physical questions into structured digital content that can be reused for:

- smart revision
- targeted practice
- adaptive mock tests
- weak-topic reinforcement
- competitive gamification (leaderboards)
- personalized study analytics

## Core vision

Students should be able to:
- click a photo of a question from a book (including batch capturing)
- extract the text automatically
- review the detected content in a large confirmation popup
- attach an image if the question has diagrams or visuals
- save the question privately into a **Question Vault**
- share questions publicly to the **Central Database**
- practice questions using Spaced Repetition (SM-2)
- climb the **Global Leaderboard** by adding and solving questions

## Premium features implemented

- **Batch Camera Mode**: Capture multiple questions in a row before processing them.
- **OCR-based text extraction**: Google ML Kit integration.
- **Question Vault**: A private space for your own saved questions.
- **Central Database**: A public space to search and discover questions from other students.
- **Spaced Repetition Practice**: Local SQLite-backed SM-2 algorithm for reviewing mistakes.
- **Global Leaderboards**: Optimized PostgreSQL views to rank top contributors and top scholars, filterable by Exam type.
- **Authentication & Backend**: Fully integrated with Supabase (Auth, Postgres, Storage).

## Recommended tech stack

### Android app
- Kotlin
- Jetpack Compose
- CameraX
- Google ML Kit (Vision Text Recognition)
- Room Database (Local Practice Engine)
- Coroutines & Flow
- Navigation Compose
- Supabase Kotlin SDK

### Backend and cloud
- Supabase Authentication
- Supabase PostgreSQL Database (with Row Level Security)
- Supabase Storage (for Question Images)
- PostgreSQL Views (for high-performance Leaderboards)
- Gemini API for AI labeling (Planned)

## Architecture flow

1. Capture one or multiple question photos using the batch camera mode.
2. Extract text using ML Kit OCR.
3. Show a verification modal for the student to review, format question/answer, and set visibility.
4. Save the verified question into Supabase (Vault or Central Database).
5. Search for public questions in the Central Database.
6. Practice questions locally using the Room DB spaced repetition engine.
7. Solving questions syncs with Supabase to increase your Global Leaderboard rank!

## Project structure

```text
app/
  src/main/java/com/edtech/ranks/
    data/           # Data layer, Room DB, Supabase integration
    ui/
      auth/         # Authentication UI
      camera/       # Batch Camera capture and OCR workflow
      central/      # Public Central Database search
      home/         # Dashboard UI
      leaderboard/  # Global Leaderboard UI
      navigation/   # App navigation
      practice/     # Spaced Repetition Practice screen
      profile/      # User profile setup
      theme/        # App theme and styling
      vault/        # Private Question Vault
    MainActivity.kt
```

## Getting started

1. Review the database schema in `setup_supabase.sql`.
2. Run the SQL script in your Supabase project's SQL Editor to create tables, Storage Buckets, and Postgres Views.
3. Add your Supabase URL and Anon Key to `local.properties` (or initialize within the app).
4. Run the app!

## Current status

This project has a strong foundation including authentication, camera capture, OCR, private/public database segmentation, local spaced-repetition practice, and competitive gamification via leaderboards.

## License

This project is currently unlicensed. Add a license if you want to share or distribute it publicly.
