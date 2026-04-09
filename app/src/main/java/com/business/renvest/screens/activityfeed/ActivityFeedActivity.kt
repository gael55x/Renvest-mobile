package com.business.renvest.screens.activityfeed

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.bindHeaderBusinessName
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast

class ActivityFeedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_feed, R.id.root)
        bindHeaderBusinessName(R.id.text_header_business)

        setupMainBottomNavigation(R.id.nav_activity)

        val coming = getString(R.string.coming_soon)
        val stub = View.OnClickListener { toast(coming) }
        findViewById<View>(R.id.button_feed_calendar).setOnClickListener(stub)
        findViewById<View>(R.id.button_feed_export).setOnClickListener(stub)
    }
}
