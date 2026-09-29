# FemCare 360 — Soft Care Guide & Feminine Wellness Companion

FemCare 360 is a soft, flat-illustrated feminine health and wellness Android application built with Jetpack Compose, Material Design 3, Room Database, and Clean Architecture.

## Key Features
- **Flat & Soft Feminine Design System**:
  - Soft palette (`#FFD6E7`, `#E8D7FF`, `#FFE5D4`, `#FFF9F5`, `#6B2D4B`) configured in `res/values/colors.xml`.
  - `SoftCareCard` components with 24dp rounded corners and light 4dp elevation shadow (`res/values/dimens.xml`).
  - `SoftGradientBackground` providing a calm cream-to-soft-pink vertical gradient.
  - Custom local Google Fonts (`Poppins` and `Outfit`).
- **5-Language Support + RTL Layout**:
  - English (`values`), Bangla (`values-bn`), Hindi (`values-hi`), Urdu (`values-ur`), and Arabic (`values-ar`).
  - Instant in-app Language Switcher Bar (`বাংলা`, `English`, `हिंदी`, `اردو`, `العربية`) with automatic RTL layout switching for Urdu and Arabic.
- **Flat 2D Vector Illustrations Across All 7 Modules**:
  1. **Bra Size Calculator**: `img_guide_underbust` & `img_guide_bust` (`res/layout/bra_size.xml` + Compose `Flat2DBraGuideSection`) + Live GPS Location Store Finder (`Amazon.sa` & `Noon.sa` for Saudi Arabia/Mecca, `Daraz` for Bangladesh).
  2. **Period & Moon Tracker**: `img_period_guide` + Auto cycle & ovulation prediction + smart notifications.
  3. **Breast Care & Hygiene**: `img_self_check` + Evidence-based monthly self-check educational guide.
  4. **Daily Fitness Routine**: `img_fitness_guide` + Guided 10-minute yoga, pelvic floor & posture timer.
  5. **Adult Wellness (18+ Lock)**: `img_wellness_guide` + PIN `1818` privacy gate with educational reproductive health topics.
  6. **Auto Diet & Water Reminder**: `img_water` + Daily 8-glass hydration tracker & cycle-synced nutrition guide.
  7. **Specialist Doctor Finder**: `img_doctor_guide` + Verified female specialists in Saudi Arabia and Bangladesh with direct Call, Map, and Bookmark support.
