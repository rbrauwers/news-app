# News app (Android)

Android application that display news and his sources.
Data is fetched from [News API](https://newsapi.org/).
The implementation follows [app architecture](https://developer.android.com/topic/architecture) principles, such as modularization, dependency injection, single source of truth, etc.

![News App](https://github.com/rbrauwers/news-app/blob/main/screenshots/headlines.png)

## Stack
- UI: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- Dependecy injection: [Hilt](https://dagger.dev/hilt/)
- Network: [Retrofit](https://square.github.io/retrofit/)
- Local storage: [Room](https://developer.android.com/training/data-storage/room)
