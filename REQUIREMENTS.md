# Maatram — Requirements

> Derived from `docs/Business_Project__1_Updated.pdf` and
> `docs/Maatram_Pitch_Deck.pdf`, both read in full. This is the review
> checkpoint — **please confirm or correct before any code is written.**

## 1. App identity

- **Name:** Maatram (Tamil: "change / transformation")
- **Tagline:** *Action ⟹ Value* — moving students from passive scrolling to active living.
- **One-line purpose:** A friction-first screen-time reduction app that intercepts
  unconscious short-form scrolling and converts distraction into focus.
- **Target users:** Students (primary), Parents, Teachers/School admins (secondary, view-only).
- **Origin:** School business project — SSVM School of Excellence, Class 10B.
  Team: Ridhun Sankar, A. Ram Saravanan, Vivan V.R, Keshawa Wardhana K, Aswanth Kumar J.
- **Licence:** Open-source.

## 2. Platform & scope decision (please confirm)

The PDFs describe **both** a "full-time Chrome extension" **and** a mobile app.
We are building the **Android app only** in this project. The Chrome extension is
**out of scope** for v1. → *Confirm this is right.*

The PDFs also describe a **monetisation / premium tier** (AI insights, Pro plans,
school licensing). v1 is **free and fully offline** — no payments, no premium
gating, no AI features. Premium is a **future phase**, noted but not built. → *Confirm.*

## 3. Architecture (locked defaults)

| Concern | Choice |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3, single-activity |
| Pattern | MVVM — ViewModel + immutable `UiState` via `StateFlow`, Repository layer |
| DI | Hilt |
| Local storage | Room + DataStore (preferences) |
| Async | Coroutines / Flow |
| Navigation | Navigation-Compose |
| Networking | **None in v1** (fully offline) |
| minSdk / targetSdk | 26 / 35 |
| App ID | `org.maatram.app` |

**Offline:** 100% local. Study Rooms and Leaderboards are **local stubs** behind
repository interfaces, so a backend can be added later without a rewrite.

## 4. Design system (IMPORTANT — overrides the PDF)

- The PDFs specify a **green + blue** palette.
- **User decision (2026-07-15): use the warm claude.ai palette instead** — cream/
  off-white backgrounds, coral-rust accent (~`#CC785C`), near-black text, warm-grey
  borders, full warm dark mode. Design language modelled on the **claude.ai app UI**
  (whitespace, soft rounded cards, large calm headings).
- ⚠️ **Consequence to be aware of:** your printed pitch deck still says "green + blue,"
  so the running app will not match that slide. You've accepted this.
- All colours/dimensions/type live as tokens in `ui/theme/`.

## 5. Permissions required (Android)

| Permission | Why | How granted |
|---|---|---|
| `PACKAGE_USAGE_STATS` (Usage Access) | Read per-app screen time | User toggles in system Settings |
| `BIND_ACCESSIBILITY_SERVICE` | Detect foreground app to trigger friction | User enables service in Settings |
| `SYSTEM_ALERT_WINDOW` (Overlay) | Draw the friction/pause overlay over other apps | User grants in Settings |
| `POST_NOTIFICATIONS` | Foreground-service + reminders (Android 13+) | Runtime prompt |
| `FOREGROUND_SERVICE` (+ special-use) | Keep the blocking service alive | Manifest |

## 6. Blocking engine — honest limitations (must stay visible, not buried)

1. **Play Store risk:** Google restricts the Accessibility API to genuine
   accessibility use. A screen-time blocker using it **may be rejected** from the
   Play Store. For a **sideloaded APK / internal school trial this is fine.** A
   public Play listing is a live risk.
2. **"Hard Lock, no override" is not truly unbypassable.** A user can always
   disable the accessibility service in system Settings or force-stop the app. We
   build the **strongest honest friction** (deep-link nag to re-enable, streak
   penalty, delay) and state the ceiling plainly. We will **not** pretend it is unbreakable.

## 7. Screens (build order — one unit each)

| # | Screen | Core user story | Tier |
|---|---|---|---|
| 0 | Splash / intro | Animated interactive petals; brand moment | Core |
| 1 | Onboarding + permissions | "As a user I grant the 4 permissions with clear explanations" | Core |
| 2 | Home dashboard | "I see today's screen time, time saved, productivity score" | Core |
| 3 | Blocklist / target setup | "I pick apps to restrict and set daily target times" | Core |
| 4 | Friction Layer overlay | "When I open a blocked app I get a 5–10s pause + goal prompt" | Core |
| 5 | Hard Lock Mode | "Once I breach my target, settings lock with no override" | Core |
| 6 | Dopamine Replacement | "A blocked tap redirects me to white-noise/breathing + points" | Core |
| 7 | Pomodoro timer (25/5) | "I run focus/break cycles integrated with blocking" | Next |
| 8 | Deep Work timer (90/20) | "During Deep Work nothing but essential tools is accessible" | Next |
| 9 | Mood tracking + journal | "I log why I wanted to open an app; I see my patterns" | Next |
| 10 | Analytics / trends | "I see daily/weekly/monthly graphs + a time-of-day heatmap" | Next |
| 11 | Productivity Score | "I see a score from time saved + blocks respected + sessions" | Next |
| 12 | Streaks & badges | "I keep a streak and earn badges" | Next |
| 13 | Leaderboard | "I compare my reduction with others" (local-only v1) | Stretch |
| 14 | Study Rooms | "I join a synced-timer room with a strike system" (local stub v1) | Stretch |
| 15 | Weekly Sunday summary | "Every Sunday I get a **shareable in-app card** of my week" (no PDF) | Stretch |
| 16 | Home-screen widget | "My home screen shows productive vs social time" | Stretch |
| 17 | Parent/Teacher view | "A parent sees a non-invasive high-level summary" | Stretch |
| 18 | Settings | "I set theme, targets, and export my data" | Stretch |

Core (0–6) is the must-ship set. If time runs short I will say so early rather
than half-build everything.

## 8. Data model (inferred — Room entities)

Marked *(inferred)* where the PDF implies but doesn't spell out.

- **TrackedApp** — packageName (PK), label, isBlocked, dailyTargetMinutes.
- **UsageRecord** *(inferred)* — id, packageName, date, minutesUsed, openCount.
- **FocusSession** — id, type (POMODORO/DEEP_WORK), startedAt, endedAt, completed, appPackage?.
- **FrictionEvent** — id, packageName, timestamp, moodTag?, reasonText?, outcome (PROCEEDED/BACKED_OFF/REDIRECTED).
- **MoodLog** — id, timestamp, mood, note, linkedFrictionEventId?.
- **DailyStat** *(inferred)* — date (PK), totalScreenMinutes, timeSavedMinutes, blocksRespected, sessionsCompleted, productivityScore.
- **Badge / StreakState** — badge catalogue + current/longest streak counters.
- **StudyRoom / RoomMember / Strike** *(stub v1)* — behind a repository interface; local fake data until a backend exists.

## 9. Auth

- **None in v1.** Single local user, no login/signup. (Backend accounts, real
  Study Rooms, and multi-device sync are future phases.)

## 10. Non-functional

- Full **light + dark** mode (warm palette both).
- Accessibility: content descriptions, dynamic type, adequate contrast.
- Localisation-ready structure (strings in resources), English only for v1.
- Phone layout first; tablet not a v1 target.

## 11. Decisions (all confirmed 2026-07-15)

1. **Chrome extension** — out of scope for v1. Android app only. ✅
2. **No premium / no payments / no AI** in v1 — free & offline. ✅
3. **Warm Claude palette overriding green+blue** — confirmed. (§4) ✅
4. App **display name** on the launcher: **"Maatram"**. ✅
5. Splash — **keep interactive cherry-blossom petals**. ✅
6. Weekly summary — **shareable in-app card**, not a PDF (drops the PDF lib). ✅
