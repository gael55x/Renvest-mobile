package com.business.renvest.screens.aiadvisor

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.bindHeaderBusinessName
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast
import com.google.android.material.progressindicator.CircularProgressIndicator

class AiEngagementAdvisorActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_ai_advisor, R.id.root)
        bindHeaderBusinessName(R.id.text_header_business)

        findViewById<CircularProgressIndicator>(R.id.indicator_engagement_score).setProgressCompat(74, false)

        setupMainBottomNavigation(R.id.nav_home)

        val coming = getString(R.string.coming_soon)
        val stub = View.OnClickListener { toast(coming) }
        findViewById<View>(R.id.button_ai_refresh).setOnClickListener(stub)
        findViewById<View>(R.id.button_ai_activate_promo).setOnClickListener(stub)
        findViewById<View>(R.id.button_ai_view_data).setOnClickListener(stub)
    }
}
