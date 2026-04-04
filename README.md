# Renvest

Renvest is a platform that helps small businesses manage digital loyalty programs and promotions to increase customer retention and repeat sales.

This repository contains the native **Android** client (`:app`), Gradle project name `renvest-platform`, application id `com.business.renvest`.

## Requirements

- **JDK 17** (Android Gradle Plugin 8.x)
- **Android Studio** Koala or newer recommended
- Android **SDK** with `compileSdk 36` as configured in the module

## Build and run

```bash
./gradlew assembleDebug
```

Install the debug APK on a device or emulator, or run from Android Studio.

## Architecture

The app follows a small layered structure aligned with the reference **CustomApplication** layout:

| Layer | Package | Role |
|--------|---------|------|
| Application | `com.business.renvest.app` | `RenvestApp`, shared `AuthRepository` wiring |
| Screens | `com.business.renvest.screens.*` | Activities (launch, auth, dashboard, profile, feature stubs) |
| Data | `com.business.renvest.data` | `RenvestResult`, repositories, local/remote contracts |
| Utils | `com.business.renvest.utils` | Activity extensions (edge-to-edge, navigation, toasts), form helpers |

Session state is persisted via `SessionLocalDataSource` / `AuthRepositoryImpl`. Remote APIs are not wired yet; see `data.remote.ApiService`.

More detail: [docs/architecture.md](docs/architecture.md).

## UI and design system

- **Theme:** `Theme.Renvest` (Material 3 DayNight, no action bar) in `res/values/themes.xml` and `values-night/themes.xml`.
- **Color roles:** Prefer theme attributes (`?attr/colorPrimary`, `?attr/colorOnSurface`, `?attr/colorSurface`) and `res/values/colors.xml` tokens.
- **Components:**
  - Text fields: `Widget.Renvest.TextInputLayout` (outlined, shared corner radius).
  - Primary actions: `Widget.Renvest.Button.Primary`; secondary: `Widget.Renvest.Button.Outlined`; text links: `Widget.Renvest.Button.Text`.
- **Typography:**
  - Marketing-style auth headers: `TextAppearance.Renvest.ScreenTitle.Login` / `ScreenSubtitle.Login` (centered login headline + tagline).
  - Form-style headers: `TextAppearance.Renvest.ScreenTitle.Form` / `ScreenSubtitle.Form` (register and similar).
  - Body and metrics: `TextAppearance.Material3.*` for labels (`LabelLarge`), values (`HeadlineSmall`), and page copy (`BodyLarge`).
- **Spacing:** `padding_screen_*`, `spacing_section`, `spacing_field`, `spacing_small`, `spacing_xs` in `res/values/dimens.xml`.
- **Dashboard metric cards:** `Widget.Renvest.DashboardMetricCard` for consistent outlined cards.

Edge-to-edge: call `enableEdgeToEdge()` then `applyEdgeToEdgeInsets(R.id.root)` on screens that use a root `LinearLayout` with `@id/root`.

## Git workflow

- **Default branch:** `main` (create `staging` if you need a pre-production line).
- **Branches:** `feature/*`, `refactor/*`, `fix/*` from the trunk branch.
- **Commits:** [Conventional Commits](https://www.conventionalcommits.org/) — e.g. `feat: add loyalty stub screen`, `refactor: extract auth repository`, `fix: clear session on logout`.
- **Pull requests:** Keep them small and focused (one phase or slice per PR when possible).

## License

Add your license here.
