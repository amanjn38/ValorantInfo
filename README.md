# ValorantInfo App

A clean, beautiful app for all your VALORANT agent needs.

## Features

- Browse all VALORANT agents with details
- Search for specific agents
- View agent abilities and roles
- Explore different VALORANT content categories

## Architecture

This app follows the MVVM (Model-View-ViewModel) architecture pattern and uses:

- **Kotlin** as the programming language
- **Hilt** for dependency injection
- **Retrofit** for network requests
- **Glide** for image loading
- **Navigation Component** for navigation
- **ViewBinding** for view binding
- **Kotlin Coroutines** for asynchronous operations
- **Kotlin Flow** for reactive streams

## Testing

The app follows a comprehensive testing strategy with three types of tests:

### Unit Tests

Unit tests verify individual components in isolation, focusing on testing a single "unit" of code.

**Location**: `src/test/java/com/example/valorantinfo/`

**Examples**:
- `AgentViewModelTest`: Tests the ViewModel responsible for listing agents
- `AgentDetailsViewModelTest`: Tests the ViewModel responsible for showing agent details
- `AgentRepositoryImplTest`: Tests the repository implementation in isolation

**Libraries**:
- JUnit: Base testing framework
- MockK: Mocking library for Kotlin
- Turbine: Testing library for Flow
- Truth: Assertion library from Google

**How to run**:
```bash
./gradlew test
```

### UI Tests

UI tests simulate user interactions with the app's interface and verify that the UI behaves as expected.

**Location**: `src/androidTest/java/com/example/valorantinfo/`

**Examples**:
- `HomeFragmentTest`: Tests the home screen categories display and navigation
- `AgentsFragmentTest`: Tests the agents list screen, including search functionality

**Libraries**:
- Espresso: Google's UI testing framework
- Hilt Testing: For dependency injection in tests
- Fragment Testing: For testing fragments in isolation

**How to run**:
```bash
./gradlew connectedAndroidTest
```

### Integration Tests

Integration tests verify that multiple components work together correctly.

**Location**: Mixed with unit tests in `src/test/java/com/example/valorantinfo/`

**Examples**:
- Tests that verify ViewModel-Repository interactions

**How to run specific tests**:
```bash
./gradlew test --tests "com.example.valorantinfo.ui.viewmodels.*"
```

## Best Practices

1. **Test Pyramids**: More unit tests than integration tests, and more integration tests than UI tests.
2. **Test Independence**: Each test should be independent of other tests.
3. **Readable Tests**: Tests should be clear and descriptive, using naming patterns like \`when_X_happens_then_Y_is_expected\`.
4. **Fast Tests**: Tests should run quickly to provide quick feedback.
5. **Reliable Tests**: Tests should be deterministic and not flaky. 