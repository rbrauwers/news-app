# News app (Android)

Android application that displays news and its sources.<br>
The implementation follows [app architecture](https://developer.android.com/topic/architecture) principles, such as modularization, dependency injection, single source of truth, etc.<br><be>

https://github.com/rbrauwers/news-app/assets/3301123/c814345e-90ad-4571-a9b1-40abd060aee9

## How it works
Data is fetched from the [News API](https://newsapi.org/), stored locally, and observed by UI.

## Setup
Create an [API Key](https://newsapi.org/account) and place it at `local.properties`:
```
newsApiKey=YOUR_API_KEY
```

## Stack
- UI: [Jetpack Compose](https://developer.android.com/jetpack/compose) + [Material](https://m3.material.io/develop/android/jetpack-compose) + [Coil](https://coil-kt.github.io/coil/)
- Dependency injection: [Hilt](https://dagger.dev/hilt/)
- Network: [Retrofit](https://square.github.io/retrofit/) + [Chucker](https://github.com/ChuckerTeam/chucker)
- Local storage: [Room](https://developer.android.com/training/data-storage/room)
- Build system: [Gradle Version Catalog](https://docs.gradle.org/current/userguide/platforms.html) + [Convention plugins](https://docs.gradle.org/current/userguide/sharing_build_logic_between_subprojects.html)
- GraphQL: [Apollo](https://www.apollographql.com/developers/collection/graphql-for-android)

## Unit tests
- Instrumental tests runs with [Robolectric](https://robolectric.org/)
- Code coverage is generated with [Kover](https://github.com/Kotlin/kotlinx-kover)
- To generate coverage report, run `./gradlew koverHtmlReportDebug`

Part 1 change
Part 2 change