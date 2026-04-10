package com.business.renvest.screens.activityfeed

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.authRepository
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast

class ActivityFeedActivity : AppCompatActivity(), ActivityFeedContract.View {

    private lateinit var presenter: ActivityFeedPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_feed, R.id.root)

        presenter = ActivityFeedPresenter(this, ActivityFeedModel(authRepository()))
        presenter.onViewReady(this)

        val stub = View.OnClickListener { presenter.onStubInteraction() }
        findViewById<View>(R.id.button_feed_calendar).setOnClickListener(stub)
        findViewById<View>(R.id.button_feed_export).setOnClickListener(stub)
    }

    override fun setHeaderBusinessName(text: String) {
        findViewById<TextView>(R.id.text_header_business).text = text
    }

    override fun setupNav(selectedItemId: Int) {
        setupMainBottomNavigation(selectedItemId)
    }

    override fun showComingSoon() {
        toast(getString(R.string.coming_soon))
    }
}
