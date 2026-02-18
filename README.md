# Android News App - Clean Architecture with MVVM Pattern

A native Android news app demonstrating **Clean Architecture** with **MVVM pattern** for state management and dependency injection using Hilt. Features List-Detail navigation, Pull-to-Refresh, offline caching, and comprehensive unit tests covering Presentation, Domain, and Data layers.

## Features

- **Clean Architecture**: Domain, Data, and Presentation layers with clear separation of concerns
- **MVVM Pattern**: Predictable and lifecycle-aware UI state management using ViewModel with StateFlow
- **Offline Support**: Caches last fetched articles for offline viewing
- **Pull-to-Refresh**: Swipe down to refresh articles
- **Article Detail View**: WebView integration for reading full articles
- **Material 3 Design**: Modern UI with dynamic color support
- **Comprehensive Unit Tests**: Tests for all layers (Presentation, Domain, Data)

## Screenshots

<p align="center">
  <img src="screenshots/article_list.png" width="300" alt="Article List"/>
  &nbsp;&nbsp;&nbsp;&nbsp;
  <img src="screenshots/article_detail.png" width="300" alt="Article Detail"/>
</p>

<p align="center">
  <em>Article List (Pull-to-Refresh)</em>
  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
  <em>Article Detail (WebView)</em>
</p>

## Architecture

```
app/src/main/java/com/portfolio/newsapp/
├── core/                          # Core utilities and shared code
│   ├── config/                    # Environment configuration
│   ├── constants/                 # API constants
│   ├── error/                     # Exceptions (sealed classes)
│   ├── network/                   # Network connectivity
│   └── util/                      # Utilities (date formatting)
│
├── di/                            # Dependency injection modules
│   ├── AppModule.kt               # Core dependencies
│   ├── NetworkModule.kt           # Retrofit, OkHttp, Moshi
│   └── ArticleModule.kt           # Feature dependencies
│
├── features/
│   └── articles/
│       ├── data/                  # Data layer
│       │   ├── api/               # Retrofit API interface
│       │   ├── datasources/       # Remote and local data sources
│       │   ├── models/            # DTOs with Moshi serialization
│       │   └── repositories/      # Repository implementations
│       │
│       ├── domain/                # Domain layer (business logic)
│       │   ├── entities/          # Business entities
│       │   ├── repositories/      # Repository interfaces
│       │   └── usecases/          # Use cases
│       │
│       └── presentation/          # Presentation layer
│           ├── components/        # Reusable Compose components
│           ├── navigation/        # Navigation with type-safe routes
│           ├── screens/           # Screen composables
│           └── viewmodels/        # ViewModels with UiState
│
├── ui/theme/                      # Material 3 theme
├── MainActivity.kt                # Entry point activity
└── NewsApplication.kt             # Hilt application
```

## Getting Started

### Prerequisites

- Android Studio Hedgehog or higher
- JDK 17 or higher
- Android SDK 36 (compileSdk)
- Min SDK 24
- Register for a free NewsAPI Key at [newsapi.org](https://newsapi.org/register)

### Installation

1. Clone the repository:
```bash
git clone https://github.com/pspimentel/android-clean-architecture-mvvm-news-app.git
cd AndroidCleanArchitectureMvvmNewsApp
```

2. Add your NewsAPI key to `local.properties`:
```properties
NEWS_API_KEY=your_actual_api_key_here
```

3. Sync the project with Gradle files

4. Run the app on an emulator or device

### Running Tests

```bash
./gradlew test
```

Or in Android Studio: Right-click on `app/src/test` → **Run Tests**

## Key Technologies

| Technology | Purpose |
|------------|---------|
| [Jetpack Compose](https://developer.android.com/jetpack/compose) | Modern declarative UI |
| [Hilt](https://dagger.dev/hilt/) | Dependency injection |
| [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) | UI state management |
| [StateFlow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-state-flow/) | Reactive state holder |
| [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) | Type-safe navigation |
| [Retrofit](https://square.github.io/retrofit/) | HTTP client |
| [Moshi](https://github.com/square/moshi) | JSON serialization |
| [Coil](https://coil-kt.github.io/coil/) | Image loading |
| [Kotlin Result](https://kotlinlang.org/api/latest/jvm/stdlib/kotlin/-result/) | Functional error handling |
| [MockK](https://mockk.io/) | Mocking for tests |
| [Turbine](https://github.com/cashapp/turbine) | Flow testing |
| [JUnit](https://junit.org/) | Testing framework |

## Modern Kotlin Features

This project leverages modern Kotlin features:

- **Sealed Classes**: For exhaustive pattern matching in UiState and exceptions
- **Sealed Interfaces**: For state definitions with data objects
- **Kotlin Result**: For functional error handling (no Either dependency needed)
- **StateFlow + stateIn**: Reactive state management pattern
- **Coroutines**: Async operations throughout
- **KDoc**: Comprehensive documentation with @param, @return, @throws

## Testing

The project includes comprehensive unit tests:

| Layer | Tests |
|-------|-------|
| Core | DateFormatter, NetworkInfo |
| Data/DataSources | Remote and Local data sources |
| Data/Repositories | Repository with online/offline scenarios |
| Domain/UseCases | GetArticles use case |
| Presentation/ViewModels | State transitions and error handling |

Run all tests:
```bash
./gradlew test
```

## Error Handling

The app handles various error scenarios:

| Error | User Message |
|-------|--------------|
| API Key not configured | Shows instructions to set up API key |
| Server error (online) | Falls back to cached data or shows error |
| No connection (offline) | Shows cached data or error if no cache |
| No cache available | Shows error with retry option |

## Configuration

### API Key Setup

Add your NewsAPI key to `local.properties` (project root):

```properties
NEWS_API_KEY=your_api_key_here
```

> **Important**: The `local.properties` file is in `.gitignore` by default. Never commit API keys to version control.

### API Configuration

The app fetches US business news from NewsAPI's top-headlines endpoint:
- Base URL: `https://newsapi.org/v2`
- Endpoint: `/top-headlines`
- Country: `us`
- Category: `business`

## Project Configuration

| File | Purpose |
|------|---------|
| `gradle/libs.versions.toml` | Version catalog for dependencies |
| `build.gradle.kts` (root) | Project-level build configuration |
| `app/build.gradle.kts` | App module configuration |
| `local.properties` | API key (git-ignored) |

## Future Improvements

- [ ] Implement navigation with Navigation 3
- [ ] Add search functionality
- [ ] Implement category filtering
- [ ] Add UI tests with Compose Testing
- [ ] Implement dark mode toggle
- [ ] Add article bookmarking
- [ ] Implement pagination with Paging 3
- [ ] Add offline-first with Room database

## License

This project is open source and available under the [MIT License](LICENSE).

---

<p align="center">
  <strong>Built with ❤️ using Kotlin and Clean Architecture</strong>
</p>
