# Demo data for screenshots

`UserController.java` — `handleUnsafe` flagged (logs the whole `User`
object, whose `toString()` exposes `email`); `handleSafe` not flagged
(explicit field selection).

## How to get the screenshot

1. `./gradlew runIde` from `pii-tostring-leak-companion`, open this
   `demo/` folder as the project.
2. Full Screen, open `UserController.java` — a warning should appear
   on `handleUnsafe`'s `log.info(user)` call but not on `handleSafe`'s.
3. Screenshot with both methods visible, save into
   `pii-tostring-leak-companion/docs/screenshots/`. Close the sandbox.
