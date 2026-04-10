# AMOLONG_MVP

This file is the Markdown version of `AMOLONG_MVP.pdf`.

## Focus of This Submission

For presentation, I separated the app into clearer feature slices:

```text
screens/
├── login/
│   ├── LoginActivity.kt
│   ├── LoginContract.kt
│   └── LoginPresenter.kt
├── register/
│   ├── RegisterActivity.kt
│   ├── RegisterContract.kt
│   └── RegisterPresenter.kt
└── dashboard/
    ├── DashboardActivity.kt
    ├── DashboardContract.kt
    └── DashboardPresenter.kt
```

I did this because `login` and `register` are honestly two different user flows already, so putting both inside one `auth` bucket made the project harder to present. Splitting them into separate slices makes the vertical slicing structure easier to understand. Each slice now has a clear View, Presenter, and shared data connection.

## How MVP Is Applied

- `Activity` = View
- `Presenter` = screen logic and user actions
- `Data` = shared store and app session storage
- `Contract` = rules between View and Presenter

So even if `login`, `register`, and `dashboard` are separate features, they still follow the same MVP setup.

## AndroidManifest.xml

This is the main registration for the three important screens plus the launcher screen.

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <application
        android:name=".app.RenvestApp"
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.Renvest"
        tools:targetApi="31">
        <activity
            android:name=".screens.launch.LaunchActivity"
            android:exported="true"
            android:theme="@android:style/Theme.NoDisplay">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
        <activity
            android:name=".screens.login.LoginActivity"
            android:exported="false" />
        <activity
            android:name=".screens.register.RegisterActivity"
            android:exported="false" />
        <activity
            android:name=".screens.dashboard.DashboardActivity"
            android:exported="false" />
    </application>

</manifest>
```

## Shared Data Layer

These files are shared by the three slices. This is the data side of MVP. I simplified it into one real data class plus `RenvestApp`, because that is enough to follow MVP without adding extra interface boilerplate.

### AuthStore.kt

```kotlin
package com.business.renvest.data.repository

import android.content.Context
import com.business.renvest.R
import com.business.renvest.data.RenvestResult

class AuthStore {

    private fun prefs(context: Context) =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun isLoggedIn(context: Context): Boolean =
        prefs(context).getBoolean(KEY_LOGGED_IN, false)

    fun getBusinessName(context: Context): String =
        prefs(context).getString(KEY_BUSINESS_NAME, "").orEmpty()

    fun getEmail(context: Context): String =
        prefs(context).getString(KEY_EMAIL, "").orEmpty()

    fun signInWithEmail(context: Context, email: String): RenvestResult<Unit> {
        prefs(context).edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putString(KEY_EMAIL, email.trim())
            .apply()
        return RenvestResult.Ok(Unit)
    }

    fun signUp(context: Context, businessName: String, email: String): RenvestResult<Unit> {
        prefs(context).edit()
            .putBoolean(KEY_LOGGED_IN, true)
            .putString(KEY_BUSINESS_NAME, businessName.trim())
            .putString(KEY_EMAIL, email.trim())
            .apply()
        return RenvestResult.Ok(Unit)
    }

    fun clearSession(context: Context): RenvestResult<Unit> {
        prefs(context).edit().clear().apply()
        return RenvestResult.Ok(Unit)
    }

    fun businessDisplayName(context: Context): String {
        val stored = getBusinessName(context).trim()
        return if (stored.isNotEmpty()) stored else context.getString(R.string.default_business_display)
    }

    companion object {
        private const val PREFS_NAME = "renvest_session"
        private const val KEY_LOGGED_IN = "logged_in"
        private const val KEY_BUSINESS_NAME = "business_name"
        private const val KEY_EMAIL = "email"
    }
}
```

### RenvestApp.kt

```kotlin
package com.business.renvest.app

import android.app.Application
import android.util.Log
import com.business.renvest.data.repository.AuthStore

class RenvestApp : Application() {

    val authStore: AuthStore by lazy { AuthStore() }

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
    }

    companion object {
        private const val TAG = "RenvestApp"
    }
}
```

## Feature Slice 1: Login

### Why this is its own slice

Login has its own UI, its own validation flow, and its own navigation target. Since the user journey here is different from registration, it makes more sense to isolate it as `screens/login`.

### LoginActivity.kt

This is the View. It reads input, wires click listeners, and sends actions to the Presenter.

```kotlin
package com.business.renvest.screens.login

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.screens.dashboard.DashboardActivity
import com.business.renvest.screens.register.RegisterActivity
import com.business.renvest.utils.authStore
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.startActivity
import com.business.renvest.utils.startActivityClearTask
import com.business.renvest.utils.toast
import com.business.renvest.utils.validateRequired
import com.business.renvest.utils.valueText
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var presenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_login, R.id.root)

        presenter = LoginPresenter(this, authStore())

        findViewById<TextView>(R.id.text_forgot_password).setOnClickListener {
            toast(getString(R.string.coming_soon))
        }

        val textInputLayoutEmail = findViewById<TextInputLayout>(R.id.input_email_layout)
        val textInputLayoutPassword = findViewById<TextInputLayout>(R.id.input_password_layout)
        val materialButtonLogin = findViewById<MaterialButton>(R.id.button_login)
        val materialButtonGoRegister = findViewById<MaterialButton>(R.id.button_go_register)

        materialButtonLogin.setOnClickListener {
            val requiredMessage = getString(R.string.error_field_required)
            val okEmail = textInputLayoutEmail.validateRequired(requiredMessage)
            val okPassword = textInputLayoutPassword.validateRequired(requiredMessage, trim = false)
            if (!okEmail || !okPassword) return@setOnClickListener
            presenter.onLoginSubmitted(this, textInputLayoutEmail.valueText())
        }

        materialButtonGoRegister.setOnClickListener {
            startActivity(RegisterActivity::class.java)
        }
    }

    override fun showToast(message: String) {
        toast(message)
    }

    override fun navigateToDashboard() {
        startActivityClearTask(DashboardActivity::class.java)
        finish()
    }
}
```

### LoginContract.kt

This file keeps the responsibilities clear between the View and Presenter.

```kotlin
package com.business.renvest.screens.login

import android.content.Context

interface LoginContract {
    interface View {
        fun showToast(message: String)
        fun navigateToDashboard()
    }

    interface Presenter {
        fun onLoginSubmitted(context: Context, email: String)
    }
}
```

### LoginPresenter.kt

This is the Presenter. It does not touch XML directly. It talks to the repository, checks the result, and tells the View what to do next.

```kotlin
package com.business.renvest.screens.login

import android.content.Context
import com.business.renvest.data.RenvestResult
import com.business.renvest.data.notifyErrorIfNotOk
import com.business.renvest.data.repository.AuthStore

class LoginPresenter(
    private val view: LoginContract.View,
    private val authStore: AuthStore,
) : LoginContract.Presenter {

    override fun onLoginSubmitted(context: Context, email: String) {
        when (val result = authStore.signInWithEmail(context, email)) {
            is RenvestResult.Ok -> view.navigateToDashboard()
            else -> result.notifyErrorIfNotOk { view.showToast(it) }
        }
    }
}
```

### Login XML

- Layout file: `app/src/main/res/layout/activity_login.xml`
- Purpose: email input, password input, login button, and register redirect
- In MVP terms, this XML is part of the View layer because it defines the UI that `LoginActivity` controls

## Feature Slice 2: Register

### Why this is its own slice

Register is not just “more auth stuff.” It has different inputs, different validation, and a different job. The screen collects business name and account details, checks password confirmation, then creates the session. That is enough reason to give it its own feature folder.

### RegisterActivity.kt

```kotlin
package com.business.renvest.screens.register

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.screens.dashboard.DashboardActivity
import com.business.renvest.screens.login.LoginActivity
import com.business.renvest.utils.authStore
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.startActivity
import com.business.renvest.utils.startActivityClearTask
import com.business.renvest.utils.toast
import com.business.renvest.utils.validateRequired
import com.business.renvest.utils.valueText
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class RegisterActivity : AppCompatActivity(), RegisterContract.View {

    private lateinit var presenter: RegisterPresenter
    private lateinit var textInputLayoutConfirm: TextInputLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_register, R.id.root)

        presenter = RegisterPresenter(this, authStore())

        val finishBack: () -> Unit = { finish() }
        findViewById<ImageButton>(R.id.button_back).setOnClickListener { finishBack() }
        findViewById<TextView>(R.id.text_back_nav).setOnClickListener { finishBack() }

        val textInputLayoutBusiness = findViewById<TextInputLayout>(R.id.input_business_layout)
        val textInputLayoutOwner = findViewById<TextInputLayout>(R.id.input_owner_layout)
        val textInputLayoutEmail = findViewById<TextInputLayout>(R.id.input_email_layout)
        val textInputLayoutPassword = findViewById<TextInputLayout>(R.id.input_password_layout)
        textInputLayoutConfirm = findViewById(R.id.input_confirm_password_layout)
        val materialButtonRegister = findViewById<MaterialButton>(R.id.button_register)
        val materialButtonGoLogin = findViewById<MaterialButton>(R.id.button_go_login)

        materialButtonGoLogin.setOnClickListener {
            startActivity(LoginActivity::class.java)
            finish()
        }

        materialButtonRegister.setOnClickListener {
            val requiredMessage = getString(R.string.error_field_required)
            val okBusiness = textInputLayoutBusiness.validateRequired(requiredMessage)
            val okOwner = textInputLayoutOwner.validateRequired(requiredMessage)
            val okEmail = textInputLayoutEmail.validateRequired(requiredMessage)
            val okPassword = textInputLayoutPassword.validateRequired(requiredMessage, trim = false)
            val okConfirm = textInputLayoutConfirm.validateRequired(requiredMessage, trim = false)
            if (!okBusiness || !okOwner || !okEmail || !okPassword || !okConfirm) return@setOnClickListener

            presenter.onRegisterSubmitted(
                this,
                textInputLayoutBusiness.valueText(),
                textInputLayoutEmail.valueText(),
                textInputLayoutPassword.valueText(trim = false),
                textInputLayoutConfirm.valueText(trim = false),
                getString(R.string.error_password_mismatch),
            )
        }
    }

    override fun showToast(message: String) {
        toast(message)
    }

    override fun navigateToDashboard() {
        startActivityClearTask(DashboardActivity::class.java)
        finish()
    }

    override fun setConfirmPasswordError(message: String) {
        textInputLayoutConfirm.error = message
    }

    override fun clearConfirmPasswordError() {
        textInputLayoutConfirm.error = null
    }
}
```

### RegisterContract.kt

```kotlin
package com.business.renvest.screens.register

import android.content.Context

interface RegisterContract {
    interface View {
        fun showToast(message: String)
        fun navigateToDashboard()
        fun setConfirmPasswordError(message: String)
        fun clearConfirmPasswordError()
    }

    interface Presenter {
        fun onRegisterSubmitted(
            context: Context,
            businessName: String,
            email: String,
            password: String,
            confirmPassword: String,
            passwordMismatchMessage: String,
        )
    }
}
```

### RegisterPresenter.kt

```kotlin
package com.business.renvest.screens.register

import android.content.Context
import com.business.renvest.data.RenvestResult
import com.business.renvest.data.notifyErrorIfNotOk
import com.business.renvest.data.repository.AuthStore

class RegisterPresenter(
    private val view: RegisterContract.View,
    private val authStore: AuthStore,
) : RegisterContract.Presenter {

    override fun onRegisterSubmitted(
        context: Context,
        businessName: String,
        email: String,
        password: String,
        confirmPassword: String,
        passwordMismatchMessage: String,
    ) {
        if (password != confirmPassword) {
            view.setConfirmPasswordError(passwordMismatchMessage)
            return
        }
        view.clearConfirmPasswordError()
        when (val result = authStore.signUp(context, businessName, email)) {
            is RenvestResult.Ok -> view.navigateToDashboard()
            else -> result.notifyErrorIfNotOk { view.showToast(it) }
        }
    }
}
```

### Register XML

- Layout file: `app/src/main/res/layout/activity_register.xml`
- Purpose: business sign-up form, password confirmation, and back/login navigation
- This screen is a stronger example of MVP because the View only collects values while the Presenter handles the password mismatch rule

## Feature Slice 3: Dashboard

### Why this is its own slice

Dashboard is the first screen after login or registration. It has a different purpose from both auth flows because it is already inside the main app experience. It displays greeting text, business name, performance cards, and navigation to other features. That is why it should be its own feature slice and not mixed into login or register.

### DashboardActivity.kt

```kotlin
package com.business.renvest.screens.dashboard

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.screens.aiadvisor.AiEngagementAdvisorActivity
import com.business.renvest.screens.customers.CustomersActivity
import com.business.renvest.screens.loyalty.LoyaltyActivity
import com.business.renvest.screens.promotions.PromotionsActivity
import com.business.renvest.utils.authStore
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.startActivity
import com.business.renvest.utils.toast
import com.google.android.material.card.MaterialCardView

class DashboardActivity : AppCompatActivity(), DashboardContract.View {

    private lateinit var presenter: DashboardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_dashboard, R.id.root)

        presenter = DashboardPresenter(this, authStore())
        presenter.onViewReady(this)

        findViewById<android.view.View>(R.id.header_notification).setOnClickListener {
            presenter.onNotificationClicked()
        }

        findViewById<TextView>(R.id.text_perf_view_report).setOnClickListener {
            presenter.onPerfViewReportClicked()
        }

        findViewById<MaterialCardView>(R.id.card_hero_revenue).setOnClickListener {
            /* static hero; no navigation */
        }

        findViewById<MaterialCardView>(R.id.perf_cell_members).setOnClickListener {
            presenter.onPerfCellMembersClicked()
        }

        findViewById<MaterialCardView>(R.id.perf_cell_rating).setOnClickListener {
            presenter.onPerfCellRatingClicked()
        }

        findViewById<MaterialCardView>(R.id.perf_cell_ticket).setOnClickListener {
            presenter.onPerfCellTicketClicked()
        }

        findViewById<MaterialCardView>(R.id.perf_cell_churn).setOnClickListener {
            presenter.onPerfCellChurnClicked()
        }

        findViewById<MaterialCardView>(R.id.card_ai_insight).setOnClickListener {
            presenter.onCardAiInsightClicked()
        }

        setupMainBottomNavigation(R.id.nav_home)
    }

    override fun setGreeting(text: String) {
        findViewById<TextView>(R.id.text_greeting).text = text
    }

    override fun setBusinessName(text: String) {
        findViewById<TextView>(R.id.text_business_name).text = text
    }

    override fun showComingSoon() {
        toast(getString(R.string.coming_soon))
    }

    override fun navigateToCustomers() {
        startActivity(CustomersActivity::class.java)
    }

    override fun navigateToLoyalty() {
        startActivity(LoyaltyActivity::class.java)
    }

    override fun navigateToPromotions() {
        startActivity(PromotionsActivity::class.java)
    }

    override fun navigateToAiAdvisor() {
        startActivity(AiEngagementAdvisorActivity::class.java)
    }
}
```

### DashboardContract.kt

```kotlin
package com.business.renvest.screens.dashboard

import android.content.Context

interface DashboardContract {
    interface View {
        fun setGreeting(text: String)
        fun setBusinessName(text: String)
        fun showComingSoon()
        fun navigateToCustomers()
        fun navigateToLoyalty()
        fun navigateToPromotions()
        fun navigateToAiAdvisor()
    }

    interface Presenter {
        fun onViewReady(context: Context)
        fun onNotificationClicked()
        fun onPerfViewReportClicked()
        fun onPerfCellMembersClicked()
        fun onPerfCellRatingClicked()
        fun onPerfCellTicketClicked()
        fun onPerfCellChurnClicked()
        fun onCardAiInsightClicked()
    }
}
```

### DashboardPresenter.kt

```kotlin
package com.business.renvest.screens.dashboard

import android.content.Context
import androidx.annotation.StringRes
import com.business.renvest.R
import com.business.renvest.data.repository.AuthStore
import java.util.Calendar

class DashboardPresenter(
    private val view: DashboardContract.View,
    private val authStore: AuthStore,
) : DashboardContract.Presenter {

    override fun onViewReady(context: Context) {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        view.setGreeting(context.getString(greetingResForHour(hour)))
        view.setBusinessName(authStore.businessDisplayName(context))
    }

    override fun onNotificationClicked() {
        view.showComingSoon()
    }

    override fun onPerfViewReportClicked() {
        view.showComingSoon()
    }

    override fun onPerfCellMembersClicked() {
        view.navigateToCustomers()
    }

    override fun onPerfCellRatingClicked() {
        view.navigateToLoyalty()
    }

    override fun onPerfCellTicketClicked() {
        view.navigateToPromotions()
    }

    override fun onPerfCellChurnClicked() {
        view.showComingSoon()
    }

    override fun onCardAiInsightClicked() {
        view.navigateToAiAdvisor()
    }

    @StringRes
    private fun greetingResForHour(hour: Int): Int = when {
        hour < 12 -> R.string.greeting_morning
        hour < 17 -> R.string.greeting_afternoon
        else -> R.string.greeting_evening
    }
}
```

### Dashboard XML

- Layout file: `app/src/main/res/layout/activity_dashboard.xml`
- Purpose: welcome header, business name display, metric cards, quick links, and bottom navigation
- In MVP terms, the XML defines the dashboard visuals while the Presenter decides the greeting text, the displayed business name, and what happens when the user taps a card

## Why This Structure Is Better for Presentation

This split is easier to explain because each slice already matches one screen:

- `login` handles sign-in only
- `register` handles sign-up only
- `dashboard` handles post-login home screen behavior

Instead of saying “here is the auth package and inside it there are two unrelated flows,” I can now present the project one feature at a time. That feels more aligned with vertical slicing because the folders now follow the actual screens the user sees in the app.

## Summary

The cleaned-up MVP structure is:

- `screens/login` for login
- `screens/register` for registration
- `screens/dashboard` for the main home screen
- `data/repository` for shared session data and storage logic
- `app/RenvestApp` for app-level repository setup

This makes the architecture easier to read, easier to present, and more obviously based on vertical slicing.
