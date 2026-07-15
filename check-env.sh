#!/usr/bin/env bash
ok()   { printf "  \033[32m✔\033[0m %s\n" "$1"; }
bad()  { printf "  \033[31m✘\033[0m %s\n" "$1"; FAIL=1; }
warn() { printf "  \033[33m!\033[0m %s\n" "$1"; }
FAIL=0

echo; echo "Maatram environment check"; echo "========================="

echo; echo "Java"
if command -v java >/dev/null 2>&1; then
  V=$(java -version 2>&1 | head -1)
  MAJOR=$(java -version 2>&1 | head -1 | sed -E 's/.*"([0-9]+).*/\1/')
  if [ "$MAJOR" -ge 17 ] 2>/dev/null; then ok "$V"; else bad "$V — need JDK 17+"; fi
else
  bad "java not found — install JDK 17+ (Android Studio bundles one)"
fi

echo; echo "Android SDK"
SDK="${ANDROID_HOME:-$ANDROID_SDK_ROOT}"
if [ -n "$SDK" ] && [ -d "$SDK" ]; then
  ok "SDK at $SDK"
  if ls "$SDK/platforms" >/dev/null 2>&1; then
    ok "platforms: $(ls "$SDK/platforms" | tr '\n' ' ')"
    ls "$SDK/platforms" | grep -q "android-35" || warn "android-35 missing — SDK Manager > API 35"
  else
    bad "no platforms installed"
  fi
  ls "$SDK/build-tools" >/dev/null 2>&1 \
    && ok "build-tools: $(ls "$SDK/build-tools" | tr '\n' ' ')" \
    || bad "no build-tools installed"
else
  bad "ANDROID_HOME / ANDROID_SDK_ROOT not set"
  echo "     macOS:   export ANDROID_HOME=\$HOME/Library/Android/sdk"
  echo "     Linux:   export ANDROID_HOME=\$HOME/Android/Sdk"
  echo "     Windows: setx ANDROID_HOME \"%LOCALAPPDATA%\\Android\\Sdk\""
fi

echo; echo "Tools"
for t in adb git gh; do
  command -v "$t" >/dev/null 2>&1 && ok "$t — $(command -v "$t")" || bad "$t not found"
done

echo; echo "Git / GitHub"
[ -n "$(git config user.name)" ]  && ok "git user.name  = $(git config user.name)"  || bad "git user.name not set"
[ -n "$(git config user.email)" ] && ok "git user.email = $(git config user.email)" || bad "git user.email not set"
if command -v gh >/dev/null 2>&1; then
  gh auth status >/dev/null 2>&1 && ok "gh authenticated" || bad "gh not authenticated — run: gh auth login"
fi

echo; echo "Device / emulator"
if command -v adb >/dev/null 2>&1; then
  D=$(adb devices | tail -n +2 | grep -c device || true)
  [ "$D" -gt 0 ] && ok "$D device(s) attached" || warn "no device — needed later to actually see the app"
fi

echo
if [ "$FAIL" = "1" ]; then
  echo "Not ready. Fix the ✘ items above first."; exit 1
else
  echo "Ready. Run: claude   then paste MASTER_PROMPT.md"
fi
