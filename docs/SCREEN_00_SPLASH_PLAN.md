# Screen 0 — Splash — Implementation Brief

Approved plan. Implementer: read `CLAUDE.md` + `REQUIREMENTS.md` first.
Source spec: `MASTER_PROMPT.md` §4.

## Decisions already made (do not re-litigate)

- **Palette:** warm claude.ai tokens, NOT the green+blue in the PDFs. Tokens already
  exist in `ui/theme/` (`Color.kt`, `Type.kt`, `Shape.kt`, `Spacing.kt`).
- **Petals:** soft blush pink — `#F2C6C2` / `#E8A0A0`, with slight per-petal tint
  variance — against the cream `#FAF9F5` background. Coral `#CC785C` stays reserved
  for interactive accents; do not tint petals coral.
- **Never hardcode a colour in a composable.** Add the two blush tokens to
  `ui/theme/Color.kt` and reference them from there.

## Files

| File | Purpose |
|---|---|
| `ui/splash/PetalSystem.kt` | Pure physics. `Petal` model + `update(dt, touch)`. **No Compose imports** — keeps it unit-testable. |
| `ui/splash/SplashScreen.kt` | `Canvas` render, frame loop, gestures, brand text |
| `ui/splash/SplashViewModel.kt` | `SplashUiState` via `StateFlow` — the advance signal only |
| `ui/navigation/MaatramNavHost.kt` | *(edit)* add `SPLASH` route; make it `startDestination` |
| `ui/theme/Color.kt` | *(edit)* add `BlushPetalLight` / `BlushPetalDeep` |
| `src/test/.../PetalSystemTest.kt` | Physics unit tests |

## State model

```kotlin
data class Petal(
    var x: Float, var y: Float,       // position (px)
    var vx: Float, var vy: Float,     // velocity (px/s)
    var size: Float,
    var rotation: Float, var spin: Float,
    var swayPhase: Float,
    var alpha: Float,
    var tint: Color,
)
```

- **Fall:** baseline `vy` + `sin(swayPhase)` lateral drift (leaf-flutter).
- **Touch repulsion:** finger within radius `R` → impulse directed away from the
  finger, scaled `(1 - dist/R)`. Velocity damps back toward the fall baseline so
  petals **resume falling** rather than snapping back.
- **Recycle:** petal exits bottom → respawn at top with fresh randomised params.
  Fixed pool, **zero allocation mid-flight**.
- `SplashUiState(isReadyToAdvance: Boolean)`; ~2.2s then navigate with
  `popUpTo(SPLASH) { inclusive = true }` so Back never returns to the splash.

## 60fps requirements — this is where a naive version janks

These are non-negotiable:

1. Petals live in a **pre-allocated `Array<Petal>` (~45), mutated in place — NOT
   Compose `State`.** Recomposing 45 objects per frame is the classic failure.
2. Drive frames with `withFrameNanos` inside `LaunchedEffect`, using **real
   delta-time**. Never assume 16ms.
3. Redraw via a single tick `MutableFloatState` read inside `Canvas`.
4. **Reuse one `Path`** for the petal shape, transformed per petal via
   `withTransform`. Allocating a `Path`/`Color` per petal per frame = GC churn = jank.
5. Gestures via `pointerInput` + `awaitPointerEventScope`; hold touch position in a
   plain `var`, not state.

## Content

Centred over the petal field: "Maatram" (`displaySmall`) + tagline
"Action ⟹ Value" (`bodyLarge`, `onSurfaceVariant`). Use `stringResource`, not
literals — `R.string.app_name`, `R.string.tagline` already exist.

## Accessibility / platform

- **Reduced motion:** if the system animator duration scale is 0, render a
  **static** petal field. Required, not optional.
- Petals are decorative → no content description; text must stay legible.
- **Android 12+ system splash** shows before this screen. `windowBackground` is
  already cream so it hands off with no colour flash — do NOT add
  `core-splashscreen`.

## Dependencies

**None.** Compose Canvas + geometry only.

## Tests

`PetalSystemTest` — pure functions, no Robolectric:
- petal below bottom edge recycles to the top
- touch within radius pushes a petal away from the finger
- with no touch, horizontal velocity decays back toward the sway baseline
- `update` is delta-time-scaled (2× dt ⇒ ~2× displacement)

## Verify before returning (all four clean)

```bash
./gradlew compileDebugKotlin
./gradlew lint
./gradlew testDebugUnitTest
./gradlew assembleDebug
```

Report honestly: you cannot see the rendered UI. Confirm "compiles and passes
tests" — the look and the 60fps feel are the user's to confirm on device.
