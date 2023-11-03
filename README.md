# News app (Android)

Android application that display news and his sources.<br><br>
Data is fetched from the [News API](https://newsapi.org/).<br><br>
The implementation follows [app architecture](https://developer.android.com/topic/architecture) principles, such as modularization, dependency injection, single source of truth, etc.<br>

<img src="https://github.com/rbrauwers/news-app/blob/main/screenshots/headlines.png" alt="News app" width="200"/>

## Stack
- UI: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- Dependecy injection: [Hilt](https://dagger.dev/hilt/)
- Network: [Retrofit](https://square.github.io/retrofit/)
- Local storage: [Room](https://developer.android.com/training/data-storage/room)
