# MASTER PROMPT — Maatram Android App

> Paste this whole block as your first message in Claude Code.
> Fill in the `<<< FILL IN >>>` slot first.

---

You are building **Maatram**, an Android app, from scratch, with me, over the next 3–4 days.
Use the `android-app-builder` skill. Read it before you do anything else.

## 0. Environment check — do this FIRST, before any code

Run and report the results back to me in a short table (found / not found / version):

```bash
java -version
echo "ANDROID_HOME=$ANDROID_HOME"
echo "ANDROID_SDK_ROOT=$ANDROID_SDK_ROOT"
ls "$ANDROID_HOME/platforms" 2>/dev/null
ls "$ANDROID_HOME/build-tools" 2>/dev/null
which sdkmanager adb gradle git gh
git config user.name; git config user.email
gh auth status
```

Requirements: JDK 17+, Android SDK with platform 35 + build-tools, `adb`, `git`,
and `gh` authenticated. **If anything is missing, stop and tell me exactly what
to install — do not proceed to codegen.** I would rather lose 20 minutes fixing
the toolchain than discover on day 3 that nothing has ever compiled.

## 1. What we are building

**Maatram** — tagline *"Action ⟹ Value"*. A screen-time reduction ecosystem for
students. It is a school business project (SSVM School of Excellence, class 10B).
Open-source. Colour scheme: **green + blue**.

The full spec is in the two PDFs in `docs/`:
- `docs/Business_Project__1_Updated.pdf` — the raw feature spec
- `docs/Maatram_Pitch_Deck.pdf` — the pitch deck

Read **both, fully** (use the `pdf-reading` skill — do not skim), then produce
`REQUIREMENTS.md` per the android-app-builder workflow. **Stop and show it to me
before writing any code.**

## 2. Architecture (locked — don't re-litigate these)

- Kotlin, Jetpack Compose, Material 3, single-activity
- MVVM: ViewModel + StateFlow, Repository layer
- Hilt (DI), Room (local), Coroutines/Flow
- Navigation-Compose
- minSdk 26, targetSdk 35
- **100% local/offline for v1.** No backend, no Firebase. Study Rooms and
  leaderboards are local/stub in v1 with a repository interface designed so a
  backend can drop in later. Do not spend day 1 building a server.
- App ID: `org.maatram.app`

## 3. Screen list — one screen per feature, one at a time

Build in this order. Each numbered item is one unit of work.

| # | Screen | Notes |
|---|--------|-------|
| 0 | Splash / intro | Falling cherry-blossom petals, interactive on touch — see §4 |
| 1 | Onboarding + permissions | Usage Access, Accessibility Service, Overlay, Notifications |
| 2 | Home dashboard | Today's screen time, time saved, productivity score |
| 3 | App blocklist / target setup | Pick apps, set daily target times |
| 4 | **Friction Layer overlay** | 5–10s mandatory pause + "What is your goal right now?" |
| 5 | Hard Lock Mode | Target breached → settings lock, no override |
| 6 | Dopamine Replacement | White noise / breathing micro-exercise, awards points |
| 7 | Pomodoro timer (25/5) | Integrated with blocking |
| 8 | Deep Work timer (90/20) | Nothing accessible except essential tools |
| 9 | Mood tracking + journal | "Why did you want to open this?" → pattern view |
| 10 | Analytics / trends | Daily / weekly / monthly graphs, time-of-day heatmap |
| 11 | Productivity Score | Time saved + blocks respected + sessions completed |
| 12 | Streaks & badges | Streak counter, badge grid |
| 13 | Leaderboard | Local-only in v1 |
| 14 | Study Rooms | Synced timer + strike system — **local stub in v1** |
| 15 | Weekly Sunday summary | Generates a PDF, shareable |
| 16 | Home-screen widget | Productive time vs. social media time |
| 17 | Parent/Teacher view | High-level summary only, non-invasive |
| 18 | Settings | Theme, targets, export data |

Screens 0–6 are the **must-ship core** given the deadline. 7–12 are next.
13–18 are stretch. If we run short on time, say so early — don't quietly
half-build everything.

## 4. Splash screen spec

Cherry blossom petals falling down the screen. On touch/drag, petals near the
finger react — scatter, swirl, get pushed aside — then resume falling. Compose
Canvas + a physics-ish particle system, 60fps, must not jank on a mid-range
phone. Green/blue palette (petals can be soft pink against a green-blue gradient
if that reads better — show me both).

UI reference site to match: <<< FILL IN: reference website URL >>>
Fetch it, extract the palette, type scale, spacing rhythm, and corner radii, and
write them into `ui/theme/` as design tokens **before** building screen 0.

## 5. The blocking engine — read this carefully

The One Sec-style interception needs:
- `AccessibilityService` — detects foreground app changes (the interception hook)
- `UsageStatsManager` — needs `PACKAGE_USAGE_STATS` (Usage Access, user-granted in Settings)
- `SYSTEM_ALERT_WINDOW` — for the friction overlay
- A foreground service to stay alive

Two things I need you to flag to me in `REQUIREMENTS.md`, not bury:
1. Google Play restricts the Accessibility API to genuine accessibility use.
   A screen-time blocker using it **may be rejected from Play**. For a school
   trial (sideloaded APK / internal testing track) this is a non-issue.
2. "Hard Lock, no override" cannot be truly unbypassable — the user can always
   disable the accessibility service in system Settings or force-stop the app.
   Build the strongest honest version (deep-link friction, re-enable nag,
   streak penalty) and tell me plainly what the ceiling is. Do not pretend.

## 6. Per-feature workflow — follow this loop exactly

For **each** screen in §3:

1. **Plan** — one short paragraph: files you'll create, state model, deps.
2. **Delegate the implementation** to the `android-implementer` subagent
   (it runs Opus 4.8). You stay on Fable 5 and orchestrate.
3. **Verify** — non-negotiable, in this order, loop until clean:
   ```bash
   ./gradlew compileDebugKotlin
   ./gradlew lint
   ./gradlew testDebugUnitTest
   ./gradlew assembleDebug
   ```
   Read real Gradle errors and fix them. Never suppress, never guess.
4. **Show me**:
   - a summary of what changed (files + why)
   - the verification output (pass/fail per command)
   - the APK path, so I can install it: `adb install -r app/build/outputs/apk/debug/app-debug.apk`
   - **be honest about what you did NOT verify** — you cannot see the UI. You can
     confirm it compiles and passes tests. Whether it *looks* right is my call,
     after I run it.
5. **Wait for my approval.** Do not start the next screen until I say go.
6. On approval:
   ```bash
   git add -A
   git commit -m "feat(<screen>): <what it does>"
   git push origin main
   ```
   Update `PROGRESS.md`.

Do not batch screens. One at a time, verified, approved, committed.

## 7. Things you should ask me about, not assume

- Anything the PDFs are genuinely silent on
- Any choice that changes the shape of the app
- Any moment where the honest answer is "this can't be done the way it's described"

Ask with a real question, not a wall of options.

## 8. Start now

1. Run the environment check (§0). Report.
2. If green: read both PDFs, write `REQUIREMENTS.md`, show me. Stop there.
