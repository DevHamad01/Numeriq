# Numeriq — Personal AI Math Study Companion 📐✨

Numeriq is a modern, native Android application designed to be every student's personal AI math companion. Built with **Kotlin**, **Jetpack Compose**, **Material 3**, **Room Local Database**, and powered by the **Google Gemini API**, Numeriq transforms complex math equations, proofs, and handwritten notes into clear, step-by-step visual solutions.

---

## 🌟 Key Features

- **Step-by-Step AI Math Solver**:
  - Solves algebra, calculus, trigonometry identities, arithmetic, and word problems.
  - Step-by-step timeline breakdowns with expandable explanations.
  - Highlights the final verified answer with clean mathematical notation.
  - Key concepts, pro tips/shortcuts, and common pitfalls to avoid on exams.

- **3 Tailored AI Tutor Modes**:
  - **Tutor AI (Free)**: Beginner-friendly explanations that break down core principles simply.
  - **Tutor AI Pro**: Deep dive with underlying theorems, mathematical proofs, and alternative methods.
  - **Tutor AI Max**: Exam-focused speed shortcuts, mnemonics, and high-yield test tips.

- **Scan & Solve (Multimodal OCR & AI)**:
  - Take or upload photos of handwritten homework or textbook problems using the native photo picker.
  - Transcribes math expressions into readable text with an editable preview before solving.
  - Quick-start one-tap textbook equations.

- **Local Solved History**:
  - Automatic persistence using Android Room SQLite Database.
  - Fast search by topic or formula with capsule search bar.
  - Reopen previous solutions anytime, even offline.

- **Polished Material 3 UI / UX**:
  - Signature `#AFE976` lime-fresh green accent on crisp pure white surfaces.
  - Seraphic typography and clean vector icons.
  - Floating pill-shaped navigation bar with active dark badge.
  - Smooth animated transitions and non-blocking state handling.

---

## 🛠 Tech Stack & Architecture

- **Language**: Kotlin 2.0+
- **UI Framework**: Jetpack Compose & Material 3 (M3)
- **Architecture**: MVVM (Model-View-ViewModel) + Clean Architecture
- **State Management**: Kotlin Coroutines & `StateFlow`
- **Local Database**: Android Jetpack Room with Kotlin Symbol Processing (KSP)
- **Networking**: OkHttp3 & Google Generative Language (Gemini API)
- **Image Loading**: Coil Compose

---

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK 35+ (Minimum SDK: 24)

### Setup & Run
1. Clone the repository:
   ```bash
   git clone https://github.com/your-username/numeriq.git
   cd numeriq
   ```

2. Configure Gemini API Key:
   - Copy `.env.example` to `.env` in the root folder:
     ```bash
     GEMINI_API_KEY=YOUR_GEMINI_API_KEY
     ```
   - Obtain a key from [Google AI Studio](https://aistudio.google.com).

3. Build and install:
   - Open project in Android Studio.
   - Sync Gradle files.
   - Run on an Android device or emulator (`Run > Run 'app'`).

---

## 📄 License
This project is open-source and available under the [MIT License](LICENSE).
