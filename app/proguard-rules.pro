# Maatram ProGuard rules.
# Compose + Hilt + Room ship their own consumer rules; keep this file for
# app-specific rules only.

# Keep the AccessibilityService entry point (referenced from the manifest only).
-keep class org.maatram.app.blocking.** { *; }
