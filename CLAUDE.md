# Maatram — project memory

Screen-time reduction Android app. School business project, SSVM School of
Excellence, class 10B. Tagline: **Action ⟹ Value**. Open-source.

## Stack (locked)
Kotlin · Jetpack Compose · Material 3 · single-activity · MVVM (ViewModel +
StateFlow) · Hilt · Room · Coroutines/Flow · Navigation-Compose ·
minSdk 26 / targetSdk 35 · appId `org.maatram.app`

v1 is **fully offline**. No backend. Study Rooms + leaderboards are local stubs
behind repository interfaces so a backend can be added later.

## Design
- Palette: green + blue. Tokens live in `ui/theme/`. Derived from the reference
  site — never hardcode a colour in a composable.
- One screen per feature. Navigation-Compose routes in `ui/navigation/`.

## Hard rules
1. **Verify before claiming done.** `compileDebugKotlin` → `lint` →
   `testDebugUnitTest` → `assembleDebug`. All four clean. No exceptions.
2. **Never suppress a lint/compile error to make it pass.** Read the real error.
3. **You cannot see the UI.** Say "compiles and passes tests", never "it works".
   Runtime and look-and-feel are the user's to confirm on device.
4. **One screen at a time.** Stop for approval before the next. Never batch.
5. Commit after each approved screen. Conventional commits. Push to `origin main`.
6. Update `PROGRESS.md` after every unit.
7. No invented API keys, no placeholder credentials, no fake package names.

## Blocking engine caveats — restate these when relevant, don't let them slide
- AccessibilityService + UsageStatsManager + SYSTEM_ALERT_WINDOW + foreground service.
- Play Store may reject accessibility-API use for blocking. Fine for a sideloaded
  school trial; a real listing is a live risk.
- "Hard Lock" is not unbypassable. The user can always disable the service in
  system Settings. Build the strongest honest friction; don't oversell it.

## Delegation
Main session orchestrates (Fable 5). Heavy implementation goes to the
`android-implementer` subagent (Opus 4.8). See `.claude/agents/`.

## Build & install

```bash
./gradlew assembleDebug
adb install -r app/build/outputs/apk/debug/app-debug.apk
```
