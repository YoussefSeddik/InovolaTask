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

> _Replace below paths with real image links or assets in your repo_

- 📊 Dashboard  
  ![Dashboard](screenshots/dashboard.png)

- ➕ Add Expense  
  ![Add Expense](screenshots/add_expense.png)

## 🧪 Unit Testing

- ViewModel unit test included (`[AddExpenseViewModelTest.kt]`)
- UseCases unit test included (`[AddExpenseUseCaseTest.kt, ConvertToUSDUseCaseTest, FormatDateUseCaseTest]`)
- Frameworks: JUnit, Turbine, Mockito

## 📦 How to Build and Run the App

### Prerequisites
- Android Studio Hedgehog+
- Kotlin 1.9+
- JDK 17+
- Internet access (for currency API)

### Steps

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/expenses-tracker.git
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