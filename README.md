<div align="center">

# Unit Converter

A clean, MVVM Android app for converting length, weight, and temperature — built as a first hands-on project with Jetpack Compose state management and testable business logic.

![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=flat-square&logo=kotlin&logoColor=white)
![License](https://img.shields.io/badge/license-MIT-blue?style=flat-square)
![Status](https://img.shields.io/badge/status-v1.0-brightgreen?style=flat-square)

</div>

## 📖 Overview

A simple converter app covering three categories — length, weight, and temperature — with a from/to unit picker and live results as you type. The focus of this project wasn't the UI complexity, it was getting the fundamentals right: separating pure conversion math from the ViewModel, and the ViewModel from the UI, so each piece can be reasoned about (and tested) independently.

## ✨ Features

- Convert between 3 categories: Length, Weight, Temperature
- Live conversion as you type, no "convert" button needed
- Swap-units button
- Input validation (rejects non-numeric input, shows an inline error instead of crashing or showing garbage)
- Dark mode support (follows system theme)
- Unit tests covering the conversion math, including edge cases (negative temperatures, same-unit conversion)

## 📱 Screenshots

<!-- Replace with real screenshots once run on an emulator/device -->
| Length | Weight | Temperature |
|---|---|---|
| _placeholder_ | _placeholder_ | _placeholder_ |

## 🏗️ Architecture

```
model/       → ConverterEngine (pure math, zero Android imports) + UnitCatalog (data)
viewmodel/   → ConverterViewModel (UI state, input validation, calls into model)
ui/          → ConverterScreen (Composables, reads ViewModel state)
```

The conversion math lives in a plain Kotlin object with no Android dependencies, which is why it has real JUnit tests (`app/src/test/...ConverterEngineTest.kt`) that run without an emulator. This separation — keep the "business logic" ignorant of the framework — is a small-scale preview of the Clean Architecture pattern used in later, larger projects in this portfolio.

## 🛠️ Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose, Material 3
- **Architecture:** MVVM
- **Testing:** JUnit 4

## 🚀 Getting Started

### Prerequisites
- Android Studio (Koala/Ladybug or later)
- JDK 17+

### Installation
```bash
git clone https://github.com/rroyops/unit-converter-android.git
cd unit-converter-android
```
1. Open in Android Studio
2. Let Gradle sync
3. Run on an emulator or device (minSdk 26 / Android 8.0+)

### Running tests
```bash
./gradlew test
```

## 🗺️ Roadmap

- [x] Length, weight, temperature conversion (v1)
- [ ] Currency conversion via a live exchange-rate API (first network call in this portfolio)
- [ ] Save recent conversions locally
- [ ] Widen unit catalog (volume, area, speed)

## 🧠 What I Learned

Building the swap-units feature exposed a subtle bug early on: swapping units without re-running the conversion left the displayed result stale relative to the new unit labels. Fixing it made me realize every state mutation in the ViewModel needs to explicitly trigger recalculation rather than assuming Compose recomposition alone keeps derived state in sync. I also learned that keeping `ConverterEngine` free of Android imports made writing tests trivial — no emulator, no mocking, just plain JUnit.

## 🤝 Contributing

This is a personal learning project, but suggestions and issues are welcome — feel free to open an issue.

## 📄 License

Distributed under the MIT License. See [`LICENSE`](LICENSE) for details.
