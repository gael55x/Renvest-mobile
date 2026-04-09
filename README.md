# Renvest

Renvest is a platform that helps small businesses manage digital loyalty programs and promotions to increase customer retention and repeat sales.

This repository contains the native **Android** client (`:app`), Gradle project name `renvest-platform`, application id `com.business.renvest`. The GitHub remote for this codebase is [**Renvest-mobile**](https://github.com/gael55x/Renvest-mobile) (`gael55x/Renvest-mobile`).

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
| Data | `com.business.renvest.data` | `RenvestResult`, `AuthRepository` / `AuthRepositoryImpl` (SharedPreferences) |
| Utils | `com.business.renvest.utils` | Activity extensions (`setupRenvestContent`, navigation, toasts), form helpers, `displayBusinessName` |

Session state is persisted inside [`AuthRepositoryImpl`](app/src/main/java/com/business/renvest/data/repository/AuthRepositoryImpl.kt) (same preference file and keys as before). There is no remote client yet; when you add HTTP, introduce something like Retrofit/Ktor under `data` and map failures to `RenvestResult.Err.Network` / `Validation`.

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

Edge-to-edge: use `setupRenvestContent(R.layout.*, R.id.root)` from [`ActivityExtensions`](app/src/main/java/com/business/renvest/utils/ActivityExtensions.kt) so `enableEdgeToEdge()`, `setContentView`, and system-bar insets on `@id/root` stay consistent.

## Git workflow

- **Default branch:** `main` (create `staging` if you need a pre-production line).
- **Branches:** `feature/*`, `refactor/*`, `fix/*` from the trunk branch.
- **Commits:** [Conventional Commits](https://www.conventionalcommits.org/) — use a type prefix and imperative description, e.g. `feat: add loyalty stub screen`, `refactor: extract auth repository`, `fix: clear session on logout`, `docs: update README`, `chore: bump AGP`.
- **Pull requests:** Keep them small and focused (one phase or slice per PR when possible).

### Remote and push

`origin` should point at this GitHub repository:

```bash
git remote set-url origin https://github.com/gael55x/Renvest-mobile.git
git push -u origin main
```

If you prefer a repo named **renvest-platform**, create that repository on GitHub first (empty or with a README), then:

```bash
git remote set-url origin https://github.com/<owner>/renvest-platform.git
git push -u origin main
```

If the remote already has an initial commit, integrate it before pushing:

```bash
git pull origin main --no-rebase --allow-unrelated-histories
# resolve any conflicts, then:
git push -u origin main
```

Day to day:

```bash
git checkout -b feature/your-change
git add -A
git commit -m "feat: describe the user-visible change"
git push -u origin feature/your-change
```

## License

Add your license here.
