# 💸 Expenses Tracker

A lightweight, offline-first **expense tracking app** built using **native Android (Kotlin)** with **Jetpack Compose**, **Room**, and **Koin**. The app tracks user expenses, supports image uploads and date selection, converts currency to USD, and includes pagination and CSV export functionality.

## 🚀 Project Overview

This app is a technical submission for Inovola. It demonstrates key native mobile competencies:
- Modern architecture (MVVM)
- Offline storage
- State management with `StateFlow`
- API integration (currency conversion)
- Pagination
- Clean and responsive UI with Compose
- ViewModel unit testing
- CSV export (Bonus)
- CI/CD: GitHub Actions for Android (Bonus)

## 🧱 Architecture Breakdown

- **Pattern**: MVVM
- **State Management**: `StateFlow` and `collectAsStateWithLifecycle`
- **Navigation**: `Navigation-Compose`
- **Dependency Injection**: Koin
- **Layers**:
  - `View`: Compose UI
  - `ViewModel`: Business logic and UI state
  - `Repository`: Connects data sources
  - `Local`: Room (Database)
  - `Remote`: Retrofit (Currency API)

## 🌐 API Integration Notes

- API: [https://open.er-api.com/v6/latest/USD](https://open.er-api.com/v6/latest/USD)
- Integration: Via Retrofit (no API key required)
- Currency conversion performed on saving an expense

## 🔄 Pagination Strategy

- **Type**: Local pagination (using Room and `LIMIT/OFFSET`)
- **Batch size**: 10 items per page
- **Mode**: LazyColumn with auto-loading items
- **Filter Compatibility**: Supports "This Month" and "Last 7 Days"

## 🖼️ Screenshots

<img width="120" height="280" alt="Screenshot_20250808_033729" src="https://github.com/user-attachments/assets/9c821f77-621a-4746-9eef-c0c05da00158" />
<img width="120" height="280" alt="Screenshot_20250808_033723" src="https://github.com/user-attachments/assets/76766bc8-6528-40ec-8f85-0b845c4fde6c" />
<img width="120" height="280" alt="Screenshot_20250808_033709" src="https://github.com/user-attachments/assets/7ad2da9d-9524-453f-9450-a60e21faa27c" />
<img width="120" height="280" alt="Screenshot_20250808_033657" src="https://github.com/user-attachments/assets/796b3d5e-81d4-4b7e-8a2a-3ef671bfb71f" />

## 🧪 Unit Testing

- ViewModel unit test included (`[AddExpenseViewModelTest.kt]`)
- UseCases unit test included (`[AddExpenseUseCaseTest.kt, ConvertToUSDUseCaseTest, FormatDateUseCaseTest]`)
- Frameworks: JUnit, Turbine, Mockito

## 📦 Android CI workflow (Bonus) 
- (Android CI) workflow for building, testing, and linting the project
  Triggered on pushes and pull requests to the 'main' branch.
### Steps
1. Check out the repository
2. Set up JDK 17 with Gradle caching
3. Build the project
4. Run unit tests
5. Run lint checks
6. Upload the generated debug APK as a build artifact and provide downloadable apk link
    
   
## 📦 How to Build and Run the App

### Prerequisites
- Android Studio Hedgehog+
- Kotlin 1.9+
- JDK 17+
- Internet access (for currency API)

### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/YoussefSeddik/InovolaTask.git
   ```

2. Open in Android Studio

3. Build the project:
   - Allow Gradle to sync
   - Click **Run**

4. App will start on your emulator/device

## 📋 Completed Features

- ✅ Compose UI: Dashboard + Add Expense
- ✅ Date & image pickers
- ✅ MVVM with state flows
- ✅ Room + Retrofit + Koin
- ✅ Local pagination with Room
- ✅ CSV export + file share
- ✅ Expense filtering
- ✅ Unit testing (ViewModel)
- ✅ Dark theme support

## 📄 License

This project is provided as part of a technical assessment and is not intended for production use.
