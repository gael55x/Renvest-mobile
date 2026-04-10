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

The selected screens for this submission are `login`, `register`, and `dashboard`. These were separated into distinct feature slices so that the structure of the application clearly matches the user-facing screens being implemented. This makes the vertical slicing architecture easier to explain and shows that each screen contains its own View and Presenter while still sharing a small and controlled data layer.

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

### RenvestResult.kt

```kotlin
package com.business.renvest.data

sealed class RenvestResult<out T> {
    data class Ok<T>(val value: T) : RenvestResult<T>()

    sealed class Err : RenvestResult<Nothing>() {
        data class Storage(val reason: String) : Err()
        data class Network(val reason: String) : Err()
        data class Validation(val reason: String) : Err()
    }
}

fun RenvestResult<*>.notifyErrorIfNotOk(notify: (String) -> Unit) {
    when (this) {
        is RenvestResult.Ok -> Unit
        is RenvestResult.Err.Storage -> notify(reason)
        is RenvestResult.Err.Network -> notify(reason)
        is RenvestResult.Err.Validation -> notify(reason)
    }
}
```

### RenvestContext.kt

```kotlin
package com.business.renvest.utils

import android.content.Context
import com.business.renvest.app.RenvestApp
import com.business.renvest.data.repository.AuthStore

fun Context.requireRenvestApp(): RenvestApp =
    applicationContext as? RenvestApp ?: error("Application is not RenvestApp")

fun Context.authStore(): AuthStore = requireRenvestApp().authStore

fun Context.displayBusinessName(): String = authStore().businessDisplayName(this)
```

### ActivityExtensions.kt

```kotlin
package com.business.renvest.utils

import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding

fun AppCompatActivity.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.startActivity(target: Class<*>) {
    startActivity(Intent(this, target))
}

fun AppCompatActivity.startActivityClearTask(target: Class<*>) {
    startActivity(
        Intent(this, target).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        },
    )
}

private fun AppCompatActivity.applyEdgeToEdgeInsets(@IdRes rootViewId: Int) {
    val root = findViewById<View>(rootViewId)
    ViewCompat.setOnApplyWindowInsetsListener(root) { view, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.updatePadding(insets.left, insets.top, insets.right, insets.bottom)
        windowInsets
    }
}

fun AppCompatActivity.setupRenvestContent(@LayoutRes layoutRes: Int, @IdRes rootViewId: Int) {
    enableEdgeToEdge()
    setContentView(layoutRes)
    applyEdgeToEdgeInsets(rootViewId)
}

fun AppCompatActivity.bindHeaderBusinessName(@IdRes textViewId: Int) {
    findViewById<TextView>(textViewId).text = displayBusinessName()
}
```

### MainBottomNavigation.kt

```kotlin
package com.business.renvest.utils

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.screens.activityfeed.ActivityFeedActivity
import com.business.renvest.screens.customers.CustomersActivity
import com.business.renvest.screens.dashboard.DashboardActivity
import com.business.renvest.screens.profile.ProfileActivity
import com.business.renvest.screens.promotions.PromotionsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

fun AppCompatActivity.setupMainBottomNavigation(@IdRes selectedItemId: Int) {
    val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)
    bottomNav.selectedItemId = selectedItemId
    bottomNav.getOrCreateBadge(R.id.nav_activity).apply {
        isVisible = true
        number = 3
    }
    bottomNav.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> navigateMainTab(DashboardActivity::class.java, selectedItemId == R.id.nav_home)
            R.id.nav_customers -> navigateMainTab(CustomersActivity::class.java, selectedItemId == R.id.nav_customers)
            R.id.nav_promos -> navigateMainTab(PromotionsActivity::class.java, selectedItemId == R.id.nav_promos)
            R.id.nav_activity -> navigateMainTab(ActivityFeedActivity::class.java, selectedItemId == R.id.nav_activity)
            R.id.nav_profile -> navigateMainTab(ProfileActivity::class.java, selectedItemId == R.id.nav_profile)
            else -> false
        }
    })
}

private fun AppCompatActivity.navigateMainTab(target: Class<*>, alreadySelected: Boolean): Boolean {
    if (alreadySelected) return true
    startActivity(target)
    return true
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

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/root"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="?android:attr/colorBackground"
    android:clipToPadding="false"
    android:fillViewport="true"
    android:overScrollMode="ifContentScrolls">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center_horizontal"
        android:orientation="vertical"
        android:paddingHorizontal="@dimen/padding_screen_horizontal"
        android:paddingTop="@dimen/grid_4"
        android:paddingBottom="@dimen/padding_screen_vertical">

        <FrameLayout
            android:layout_width="@dimen/logo_size"
            android:layout_height="@dimen/logo_size"
            android:background="@drawable/bg_logo_rounded">

            <ImageView
                android:layout_width="@dimen/grid_4"
                android:layout_height="@dimen/grid_4"
                android:layout_gravity="center"
                android:contentDescription="@string/app_name"
                app:srcCompat="@drawable/ic_renvest_mark"
                tools:ignore="ImageContrastCheck" />
        </FrameLayout>

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_section"
            android:gravity="center_horizontal"
            android:text="@string/app_name"
            android:textAppearance="@style/TextAppearance.Renvest.Display" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_small"
            android:gravity="center_horizontal"
            android:text="@string/tagline"
            android:textAppearance="@style/TextAppearance.Renvest.Body" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_section"
            android:labelFor="@id/input_email"
            android:text="@string/label_email"
            android:textAppearance="@style/TextAppearance.Renvest.Label" />

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/input_email_layout"
            style="@style/Widget.Renvest.TextInput.Outlined"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_xs"
            app:placeholderText="@string/hint_email"
            app:startIconDrawable="@drawable/ic_mail_24">

            <com.google.android.material.textfield.TextInputEditText
                android:id="@+id/input_email"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:inputType="textEmailAddress"
                android:maxLines="1"
                tools:ignore="TextContrastCheck" />
        </com.google.android.material.textfield.TextInputLayout>

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_field"
            android:labelFor="@id/input_password"
            android:text="@string/label_password"
            android:textAppearance="@style/TextAppearance.Renvest.Label" />

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/input_password_layout"
            style="@style/Widget.Renvest.TextInput.Outlined"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_xs"
            app:endIconMode="password_toggle"
            app:placeholderText="@string/hint_password"
            app:startIconDrawable="@drawable/ic_lock_24">

            <com.google.android.material.textfield.TextInputEditText
                android:id="@+id/input_password"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:inputType="textPassword"
                android:maxLines="1" />
        </com.google.android.material.textfield.TextInputLayout>

        <TextView
            android:id="@+id/text_forgot_password"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_small"
            android:gravity="end"
            android:text="@string/forgot_password"
            android:textAppearance="@style/TextAppearance.Renvest.Link" />

        <com.google.android.material.button.MaterialButton
            android:id="@+id/button_login"
            style="@style/Widget.Renvest.Button.Primary"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_section"
            android:text="@string/action_login" />

        <com.google.android.material.button.MaterialButton
            android:id="@+id/button_go_register"
            style="@style/Widget.Renvest.Button.Text"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_small"
            android:text="@string/link_register"
            app:rippleColor="?attr/colorPrimary" />
    </LinearLayout>
</ScrollView>
```

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

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/root"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="?android:attr/colorBackground"
    android:clipToPadding="false"
    android:fillViewport="true"
    android:overScrollMode="ifContentScrolls">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        android:paddingHorizontal="@dimen/padding_screen_horizontal"
        android:paddingTop="@dimen/padding_screen_vertical"
        android:paddingBottom="@dimen/padding_screen_vertical">

        <LinearLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:gravity="center_vertical"
            android:orientation="horizontal"
            android:paddingVertical="@dimen/spacing_small">

            <ImageButton
                android:id="@+id/button_back"
                android:layout_width="40dp"
                android:layout_height="40dp"
                android:background="?attr/selectableItemBackgroundBorderless"
                android:contentDescription="@string/back"
                app:srcCompat="@drawable/ic_arrow_back_24" />

            <TextView
                android:id="@+id/text_back_nav"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:background="?attr/selectableItemBackgroundBorderless"
                android:paddingHorizontal="@dimen/spacing_small"
                android:text="@string/back"
                android:textAppearance="@style/TextAppearance.Renvest.Body"
                android:textColor="@color/text_primary" />
        </LinearLayout>

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_small"
            android:text="@string/register_headline"
            android:textAppearance="@style/TextAppearance.Renvest.ScreenTitle.Form" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_small"
            android:text="@string/register_subtitle_long"
            android:textAppearance="@style/TextAppearance.Renvest.Body" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_section"
            android:labelFor="@id/input_business"
            android:text="@string/label_business_name"
            android:textAppearance="@style/TextAppearance.Renvest.Label" />

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/input_business_layout"
            style="@style/Widget.Renvest.TextInput.Outlined"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_xs"
            app:placeholderText="@string/hint_business_name"
            app:startIconDrawable="@drawable/ic_store_24">

            <com.google.android.material.textfield.TextInputEditText
                android:id="@+id/input_business"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:inputType="text|textCapWords"
                android:maxLines="1" />
        </com.google.android.material.textfield.TextInputLayout>

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_field"
            android:labelFor="@id/input_owner"
            android:text="@string/label_owner_name"
            android:textAppearance="@style/TextAppearance.Renvest.Label" />

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/input_owner_layout"
            style="@style/Widget.Renvest.TextInput.Outlined"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_xs"
            app:placeholderText="@string/hint_owner_name"
            app:startIconDrawable="@drawable/ic_person_24">

            <com.google.android.material.textfield.TextInputEditText
                android:id="@+id/input_owner"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:inputType="textPersonName|textCapWords"
                android:maxLines="1" />
        </com.google.android.material.textfield.TextInputLayout>

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_field"
            android:labelFor="@id/input_email"
            android:text="@string/label_email"
            android:textAppearance="@style/TextAppearance.Renvest.Label" />

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/input_email_layout"
            style="@style/Widget.Renvest.TextInput.Outlined"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_xs"
            app:placeholderText="@string/hint_email"
            app:startIconDrawable="@drawable/ic_mail_24">

            <com.google.android.material.textfield.TextInputEditText
                android:id="@+id/input_email"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:inputType="textEmailAddress"
                android:maxLines="1"
                tools:ignore="TextContrastCheck" />
        </com.google.android.material.textfield.TextInputLayout>

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_field"
            android:labelFor="@id/input_password"
            android:text="@string/label_password"
            android:textAppearance="@style/TextAppearance.Renvest.Label" />

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/input_password_layout"
            style="@style/Widget.Renvest.TextInput.Outlined"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_xs"
            app:endIconMode="password_toggle"
            app:placeholderText="@string/hint_password"
            app:startIconDrawable="@drawable/ic_lock_24">

            <com.google.android.material.textfield.TextInputEditText
                android:id="@+id/input_password"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:inputType="textPassword"
                android:maxLines="1" />
        </com.google.android.material.textfield.TextInputLayout>

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_field"
            android:labelFor="@id/input_confirm_password"
            android:text="@string/label_confirm_password"
            android:textAppearance="@style/TextAppearance.Renvest.Label" />

        <com.google.android.material.textfield.TextInputLayout
            android:id="@+id/input_confirm_password_layout"
            style="@style/Widget.Renvest.TextInput.Outlined"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_xs"
            app:endIconMode="password_toggle"
            app:placeholderText="@string/hint_confirm_password"
            app:startIconDrawable="@drawable/ic_shield_24">

            <com.google.android.material.textfield.TextInputEditText
                android:id="@+id/input_confirm_password"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:inputType="textPassword"
                android:maxLines="1" />
        </com.google.android.material.textfield.TextInputLayout>

        <com.google.android.material.button.MaterialButton
            android:id="@+id/button_register"
            style="@style/Widget.Renvest.Button.Primary"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_section"
            android:text="@string/action_create_account" />

        <com.google.android.material.button.MaterialButton
            android:id="@+id/button_go_login"
            style="@style/Widget.Renvest.Button.Text"
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_small"
            android:text="@string/link_already_login"
            app:rippleColor="?attr/colorPrimary" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="@dimen/spacing_section"
            android:gravity="center_horizontal"
            android:text="@string/terms_footer"
            android:textAppearance="@style/TextAppearance.Renvest.Caption" />
    </LinearLayout>
</ScrollView>
```

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

```xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:id="@+id/root"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:background="?android:attr/colorBackground"
    android:orientation="vertical">

    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center_vertical"
        android:orientation="horizontal"
        android:paddingHorizontal="@dimen/padding_screen_horizontal"
        android:paddingTop="@dimen/padding_screen_vertical"
        android:paddingBottom="@dimen/spacing_field">

        <LinearLayout
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_weight="1"
            android:orientation="vertical">

            <TextView
                android:id="@+id/text_greeting"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:textAppearance="@style/TextAppearance.Renvest.Body"
                tools:text="@string/greeting_morning" />

            <TextView
                android:id="@+id/text_business_name"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="@dimen/spacing_xs"
                android:textAppearance="@style/TextAppearance.Renvest.Title"
                tools:text="@string/default_business_display" />
        </LinearLayout>

        <FrameLayout
            android:id="@+id/header_notification"
            android:layout_width="@dimen/header_icon_size"
            android:layout_height="@dimen/header_icon_size"
            android:layout_marginEnd="@dimen/spacing_small"
            android:background="@drawable/bg_circle_surface_stroke">

            <ImageView
                android:layout_width="@dimen/grid_3"
                android:layout_height="@dimen/grid_3"
                android:layout_gravity="center"
                android:contentDescription="@string/nav_activity"
                app:srcCompat="@drawable/ic_notifications_24" />

            <View
                android:layout_width="8dp"
                android:layout_height="8dp"
                android:layout_gravity="top|end"
                android:layout_marginTop="6dp"
                android:layout_marginEnd="6dp"
                android:background="@drawable/bg_dot_red" />
        </FrameLayout>

        <FrameLayout
            android:layout_width="@dimen/avatar_size"
            android:layout_height="@dimen/avatar_size"
            android:background="@drawable/bg_circle_primary">

            <TextView
                android:id="@+id/text_avatar_initials"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_gravity="center"
                android:text="@string/avatar_initials"
                android:textAppearance="@style/TextAppearance.Material3.LabelLarge"
                android:textColor="@color/on_primary"
                android:textStyle="bold" />
        </FrameLayout>
    </LinearLayout>

    <androidx.core.widget.NestedScrollView
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1"
        android:clipToPadding="false"
        android:fillViewport="true"
        android:overScrollMode="ifContentScrolls"
        android:paddingHorizontal="@dimen/padding_screen_horizontal"
        android:paddingBottom="@dimen/spacing_field">

        <LinearLayout
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:orientation="vertical">

            <com.google.android.material.card.MaterialCardView
                android:id="@+id/card_hero_revenue"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                app:cardBackgroundColor="@color/dashboard_hero"
                app:cardCornerRadius="@dimen/card_corner_large"
                app:cardElevation="0dp"
                app:strokeWidth="0dp">

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="vertical"
                    android:padding="@dimen/grid_3">

                    <TextView
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:letterSpacing="0.05"
                        android:text="@string/dashboard_revenue_label"
                        android:textAllCaps="true"
                        android:textAppearance="@style/TextAppearance.Material3.LabelSmall"
                        android:textColor="@color/dashboard_hero_subtle" />

                    <TextView
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_marginTop="@dimen/spacing_small"
                        android:text="@string/dashboard_revenue_value"
                        android:textAppearance="@style/TextAppearance.Material3.DisplaySmall"
                        android:textColor="@color/on_dashboard_hero"
                        android:textStyle="bold" />

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:layout_marginTop="@dimen/spacing_small"
                        android:background="@drawable/bg_hero_pill"
                        android:paddingHorizontal="@dimen/spacing_field"
                        android:paddingVertical="@dimen/spacing_xs"
                        android:text="@string/dashboard_revenue_growth"
                        android:textAppearance="@style/TextAppearance.Material3.LabelMedium"
                        android:textColor="@color/on_dashboard_hero" />

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_marginTop="@dimen/grid_3"
                        android:orientation="horizontal">

                        <LinearLayout
                            android:layout_width="0dp"
                            android:layout_height="wrap_content"
                            android:layout_weight="1"
                            android:background="@drawable/bg_hero_stat_cell"
                            android:gravity="center"
                            android:orientation="vertical"
                            android:padding="@dimen/spacing_small">

                            <TextView
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                android:text="@string/dashboard_stat_visits_value"
                                android:textAppearance="@style/TextAppearance.Material3.TitleMedium"
                                android:textColor="@color/on_dashboard_hero"
                                android:textStyle="bold" />

                            <TextView
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                android:layout_marginTop="@dimen/spacing_xs"
                                android:gravity="center"
                                android:text="@string/dashboard_stat_visits_label"
                                android:textAppearance="@style/TextAppearance.Material3.LabelSmall"
                                android:textColor="@color/dashboard_hero_subtle" />
                        </LinearLayout>

                        <Space
                            android:layout_width="@dimen/spacing_small"
                            android:layout_height="1dp" />

                        <LinearLayout
                            android:layout_width="0dp"
                            android:layout_height="wrap_content"
                            android:layout_weight="1"
                            android:background="@drawable/bg_hero_stat_cell"
                            android:gravity="center"
                            android:orientation="vertical"
                            android:padding="@dimen/spacing_small">

                            <TextView
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                android:text="@string/dashboard_stat_members_value"
                                android:textAppearance="@style/TextAppearance.Material3.TitleMedium"
                                android:textColor="@color/on_dashboard_hero"
                                android:textStyle="bold" />

                            <TextView
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                android:layout_marginTop="@dimen/spacing_xs"
                                android:gravity="center"
                                android:text="@string/dashboard_stat_members_label"
                                android:textAppearance="@style/TextAppearance.Material3.LabelSmall"
                                android:textColor="@color/dashboard_hero_subtle" />
                        </LinearLayout>

                        <Space
                            android:layout_width="@dimen/spacing_small"
                            android:layout_height="1dp" />

                        <LinearLayout
                            android:layout_width="0dp"
                            android:layout_height="wrap_content"
                            android:layout_weight="1"
                            android:background="@drawable/bg_hero_stat_cell"
                            android:gravity="center"
                            android:orientation="vertical"
                            android:padding="@dimen/spacing_small">

                            <TextView
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                android:text="@string/dashboard_stat_return_value"
                                android:textAppearance="@style/TextAppearance.Material3.TitleMedium"
                                android:textColor="@color/on_dashboard_hero"
                                android:textStyle="bold" />

                            <TextView
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                android:layout_marginTop="@dimen/spacing_xs"
                                android:gravity="center"
                                android:text="@string/dashboard_stat_return_label"
                                android:textAppearance="@style/TextAppearance.Material3.LabelSmall"
                                android:textColor="@color/dashboard_hero_subtle" />
                        </LinearLayout>
                    </LinearLayout>
                </LinearLayout>
            </com.google.android.material.card.MaterialCardView>

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="@dimen/grid_3"
                android:gravity="center_vertical"
                android:orientation="horizontal">

                <TextView
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:text="@string/perf_section_title"
                    android:textAppearance="@style/TextAppearance.Renvest.SectionTitle" />

                <TextView
                    android:id="@+id/text_perf_view_report"
                    android:layout_width="wrap_content"
                    android:layout_height="wrap_content"
                    android:text="@string/perf_view_report"
                    android:textAppearance="@style/TextAppearance.Renvest.Link" />
            </LinearLayout>

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="@dimen/spacing_field"
                android:orientation="horizontal">

                <com.google.android.material.card.MaterialCardView
                    android:id="@+id/perf_cell_members"
                    style="@style/Widget.Renvest.StatCard"
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_marginEnd="@dimen/spacing_small"
                    android:layout_weight="1"
                    android:clickable="true"
                    android:focusable="true">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="vertical"
                        android:padding="@dimen/spacing_field">

                        <FrameLayout
                            android:layout_width="36dp"
                            android:layout_height="36dp"
                            android:background="@color/perf_icon_bg_people">

                            <ImageView
                                android:layout_width="20dp"
                                android:layout_height="20dp"
                                android:layout_gravity="center"
                                app:srcCompat="@drawable/ic_person_24" />
                        </FrameLayout>

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="@dimen/spacing_field"
                            android:text="@string/perf_members_value"
                            android:textAppearance="@style/TextAppearance.Renvest.Title" />

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="@dimen/spacing_xs"
                            android:text="@string/perf_members_label"
                            android:textAppearance="@style/TextAppearance.Renvest.Caption" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="@dimen/spacing_small"
                            android:background="@drawable/bg_pill_positive"
                            android:paddingHorizontal="@dimen/spacing_small"
                            android:paddingVertical="@dimen/spacing_xs"
                            android:text="@string/perf_members_trend"
                            android:textAppearance="@style/TextAppearance.Material3.LabelSmall"
                            android:textColor="@color/primary" />
                    </LinearLayout>
                </com.google.android.material.card.MaterialCardView>

                <com.google.android.material.card.MaterialCardView
                    android:id="@+id/perf_cell_rating"
                    style="@style/Widget.Renvest.StatCard"
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:clickable="true"
                    android:focusable="true">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="vertical"
                        android:padding="@dimen/spacing_field">

                        <FrameLayout
                            android:layout_width="36dp"
                            android:layout_height="36dp"
                            android:background="@color/perf_icon_bg_star">

                            <ImageView
                                android:layout_width="20dp"
                                android:layout_height="20dp"
                                android:layout_gravity="center"
                                app:srcCompat="@drawable/ic_star_24" />
                        </FrameLayout>

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="@dimen/spacing_field"
                            android:text="@string/perf_rating_value"
                            android:textAppearance="@style/TextAppearance.Renvest.Title" />

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="@dimen/spacing_xs"
                            android:text="@string/perf_rating_label"
                            android:textAppearance="@style/TextAppearance.Renvest.Caption" />
                    </LinearLayout>
                </com.google.android.material.card.MaterialCardView>
            </LinearLayout>

            <LinearLayout
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="@dimen/spacing_small"
                android:orientation="horizontal">

                <com.google.android.material.card.MaterialCardView
                    android:id="@+id/perf_cell_ticket"
                    style="@style/Widget.Renvest.StatCard"
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_marginEnd="@dimen/spacing_small"
                    android:layout_weight="1"
                    android:clickable="true"
                    android:focusable="true">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="vertical"
                        android:padding="@dimen/spacing_field">

                        <FrameLayout
                            android:layout_width="36dp"
                            android:layout_height="36dp"
                            android:background="@color/perf_icon_bg_money">

                            <ImageView
                                android:layout_width="20dp"
                                android:layout_height="20dp"
                                android:layout_gravity="center"
                                app:srcCompat="@drawable/ic_attach_money_24" />
                        </FrameLayout>

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="@dimen/spacing_field"
                            android:text="@string/perf_ticket_value"
                            android:textAppearance="@style/TextAppearance.Renvest.Title" />

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="@dimen/spacing_xs"
                            android:text="@string/perf_ticket_label"
                            android:textAppearance="@style/TextAppearance.Renvest.Caption" />

                        <TextView
                            android:layout_width="wrap_content"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="@dimen/spacing_small"
                            android:background="@drawable/bg_pill_warning"
                            android:paddingHorizontal="@dimen/spacing_small"
                            android:paddingVertical="@dimen/spacing_xs"
                            android:text="@string/perf_ticket_trend"
                            android:textAppearance="@style/TextAppearance.Material3.LabelSmall"
                            android:textColor="@color/warning_text" />
                    </LinearLayout>
                </com.google.android.material.card.MaterialCardView>

                <com.google.android.material.card.MaterialCardView
                    android:id="@+id/perf_cell_churn"
                    style="@style/Widget.Renvest.StatCard"
                    android:layout_width="0dp"
                    android:layout_height="wrap_content"
                    android:layout_weight="1"
                    android:clickable="true"
                    android:focusable="true">

                    <LinearLayout
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:orientation="vertical"
                        android:padding="@dimen/spacing_field">

                        <FrameLayout
                            android:layout_width="36dp"
                            android:layout_height="36dp"
                            android:background="@color/perf_icon_bg_chart">

                            <ImageView
                                android:layout_width="20dp"
                                android:layout_height="20dp"
                                android:layout_gravity="center"
                                app:srcCompat="@drawable/ic_trending_down_24" />
                        </FrameLayout>

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="@dimen/spacing_field"
                            android:text="@string/perf_churn_value"
                            android:textAppearance="@style/TextAppearance.Renvest.Title" />

                        <TextView
                            android:layout_width="match_parent"
                            android:layout_height="wrap_content"
                            android:layout_marginTop="@dimen/spacing_xs"
                            android:text="@string/perf_churn_label"
                            android:textAppearance="@style/TextAppearance.Renvest.Caption" />
                    </LinearLayout>
                </com.google.android.material.card.MaterialCardView>
            </LinearLayout>

            <TextView
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="@dimen/grid_3"
                android:text="@string/ai_section_title"
                android:textAppearance="@style/TextAppearance.Renvest.SectionTitle" />

            <com.google.android.material.card.MaterialCardView
                android:id="@+id/card_ai_insight"
                android:layout_width="match_parent"
                android:layout_height="wrap_content"
                android:layout_marginTop="@dimen/spacing_field"
                app:cardBackgroundColor="@color/ai_card_bg"
                app:cardCornerRadius="@dimen/card_corner_large"
                app:cardElevation="0dp"
                app:strokeWidth="0dp">

                <LinearLayout
                    android:layout_width="match_parent"
                    android:layout_height="wrap_content"
                    android:orientation="vertical"
                    android:padding="@dimen/grid_3">

                    <TextView
                        android:layout_width="wrap_content"
                        android:layout_height="wrap_content"
                        android:background="@drawable/bg_ai_chip"
                        android:paddingHorizontal="@dimen/spacing_field"
                        android:paddingVertical="@dimen/spacing_xs"
                        android:text="@string/ai_badge"
                        android:textAppearance="@style/TextAppearance.Material3.LabelSmall"
                        android:textColor="@color/on_primary" />

                    <TextView
                        android:layout_width="match_parent"
                        android:layout_height="wrap_content"
                        android:layout_marginTop="@dimen/spacing_field"
                        android:text="@string/ai_headline"
                        android:textAppearance="@style/TextAppearance.Material3.TitleLarge"
                        android:textColor="@color/on_dashboard_hero"
                        android:textStyle="bold" />
                </LinearLayout>
            </com.google.android.material.card.MaterialCardView>
        </LinearLayout>
    </androidx.core.widget.NestedScrollView>

    <com.google.android.material.bottomnavigation.BottomNavigationView
        android:id="@+id/bottom_navigation"
        style="@style/Widget.Renvest.BottomNavigation"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:itemIconTint="@color/bottom_nav_selector"
        app:itemTextColor="@color/bottom_nav_selector"
        app:labelVisibilityMode="labeled"
        app:menu="@menu/menu_nav_main" />
</LinearLayout>
```

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
