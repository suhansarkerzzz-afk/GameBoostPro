# APK build

1. Keep this project extracted in the repository. The file `.github/workflows/build-apk.yml` must be present.
2. Delete any old workflow such as `blank.yml` or old `build-apk.yml` to avoid running the wrong workflow.
3. Open **Actions** → **Build GameBoost Pro v2 APK** → **Run workflow**.
4. Wait until the green check appears.
5. Open the successful run, scroll to **Artifacts**, and download `GameBoost-Pro-v2-APK`.

This workflow deliberately runs the installed Gradle 8.9 command (`gradle`), not `./gradlew`, so the missing-wrapper and Gradle-8.7 errors do not occur.
