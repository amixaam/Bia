# AGENTS Guide for Bia

This file is the LLM-oriented contributor guide for this repository. Use it before making code changes.

## 1. Project Snapshot
- App type: Single-module Android app (`:app`)
- Stack: Kotlin + Jetpack Compose + Room + Navigation
- Purpose: Track daily meal groups, meal entries, and calories vs a goal
- Entry point: `app/src/main/java/com/example/bia/MainActivity.kt`
- Core state owner: `app/src/main/java/com/example/bia/ui/viewmodel/NutritionViewModel.kt`
- Local DB: Room database `nutrition_database`

## 2. Source-of-Truth for Versions
Use these files as the canonical version sources:
- Version catalog: `gradle/libs.versions.toml`
- Android module config: `app/build.gradle.kts`
- Project/plugin management: `build.gradle.kts`, `settings.gradle.kts`

Do not guess versions from memory. Read the catalog first.

## 3. Current Toolchain and Library Versions
From `gradle/libs.versions.toml` and `app/build.gradle.kts`:

- Android Gradle Plugin: `9.0.1`
- Kotlin (Compose plugin alias): `2.2.10`
- KSP plugin: `2.3.2`
- Kotlin serialization plugin in module: `2.0.21` (explicit in `app/build.gradle.kts`)
- Compose BOM: `2024.09.00`
- Navigation Compose: `2.9.6`
- Room: `2.8.4`
- Kotlinx Serialization JSON: `1.10.0`
- AndroidX Activity Compose: `1.8.0`
- Lifecycle Runtime KTX: `2.6.1`
- Core KTX: `1.10.1`
- compileSdk: `36`
- targetSdk: `36`
- minSdk: `31`
- Java source/target compatibility: `11`

## 4. Version-Aware Coding Rules (Important)
When generating code, use APIs compatible with the versions above.

- Compose:
  - Use Material 3 and Compose APIs that are available with BOM `2024.09.00`.
  - Do not introduce APIs from newer Compose releases unless versions are updated first.
- Navigation:
  - Use `androidx.navigation` `2.9.6` compatible APIs and patterns.
  - Keep current route style unless a migration is requested.
- Room:
  - Use Room `2.8.4` APIs with KSP (`ksp(libs.room.compiler)`).
  - Keep entities/DAO annotations compatible with Room `2.8.x`.
  - If schema changes are made, update DB version and provide migrations (or explicitly document destructive fallback intent).
- Kotlin:
  - Keep Kotlin language/codegen choices compatible with Kotlin `2.2.10` and Java 11.
  - Do not use features that require higher Java targets.
- Serialization:
  - Runtime dependency is `kotlinx-serialization-json:1.10.0`.
  - Respect currently configured serialization plugin version in `app/build.gradle.kts` unless explicitly asked to align/bump.

## 5. Architecture Constraints
- Keep the app single-module unless asked otherwise.
- Preserve current layering:
  - `data/` for entities/converters
  - `data/database/` for DAOs and `AppDatabase`
  - `ui/screens`, `ui/composables`, `ui/viewmodel`
- `NutritionViewModel` is currently constructed directly in `MainActivity` (no DI framework). Do not add DI unless requested.

## 6. Data Model Notes
- `FoodItem`, `MealEntry`, `MealGroup` are Room entities.
- `Instant` is persisted via `Converters` (`Instant` <-> `Long`).
- `MealEntry` stores snapshot fields (`nameSnapshot`, `caloriesSnapshot`) to keep historical rendering stable.
- `MealCategory` exists but is currently unused.

## 7. UI/Behavior Notes
- Main screens: `HomeScreen` and `AddMealScreen`.
- Home shows calorie summary + today's meal groups.
- Add Meal currently includes a temporary "Create fake item" path.
- "Today" range is computed once when `NutritionViewModel` is created; it does not auto-roll at midnight.

## 8. Change Checklist for LLMs
Before editing:
1. Read `gradle/libs.versions.toml` and `app/build.gradle.kts`.
2. Confirm planned APIs exist in these versions.
3. Keep package structure and naming consistent.

When editing:
1. Prefer minimal, local changes.
2. Avoid introducing new frameworks unless requested.
3. Keep Room/Compose/navigation code idiomatic for current versions.

After editing:
1. Validate build/test commands where possible.
2. If dependencies or plugin versions changed, explain compatibility impact in the PR/summary.

## 9. Suggested Validation Commands
- `./gradlew :app:assembleDebug`
- `./gradlew :app:testDebugUnitTest`
- `./gradlew :app:connectedDebugAndroidTest` (when device/emulator is available)

## 10. Baseline Documentation
For deeper functional details, use:
- `DOCUMENTATION.md`
