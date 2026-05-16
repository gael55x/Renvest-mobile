package com.business.renvest.utils

import android.content.Intent
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.business.renvest.R
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

fun AppCompatActivity.toastComingSoon() {
    toast(getString(R.string.coming_soon))
}

fun AppCompatActivity.setTextViewText(@IdRes textViewId: Int, text: String) {
    findViewById<TextView>(textViewId).text = text
}

fun AppCompatActivity.setClickListeners(listener: View.OnClickListener, @IdRes vararg viewIds: Int) {
    for (viewId in viewIds) {
        findViewById<View>(viewId).setOnClickListener(listener)
    }
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
    val viewRoot = findViewById<View>(rootViewId)
    ViewCompat.setOnApplyWindowInsetsListener(viewRoot) { view, windowInsets ->
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
