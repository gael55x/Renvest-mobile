package com.business.renvest.screens.activityfeed

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.applyEdgeToEdgeInsets
import com.business.renvest.utils.authRepository
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.toast

class ActivityFeedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_feed)
        applyEdgeToEdgeInsets(R.id.root)

        val stored = authRepository().getBusinessName(this).trim()
        findViewById<TextView>(R.id.text_header_business).text =
            if (stored.isNotEmpty()) stored else getString(R.string.default_business_display)

        setupMainBottomNavigation(R.id.nav_activity)

        val coming = getString(R.string.coming_soon)
        val stub = View.OnClickListener { toast(coming) }
        findViewById<View>(R.id.button_feed_calendar).setOnClickListener(stub)
        findViewById<View>(R.id.button_feed_export).setOnClickListener(stub)
    }
}
