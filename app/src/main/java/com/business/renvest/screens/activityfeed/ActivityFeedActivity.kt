package com.business.renvest.screens.activityfeed

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.authStore
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast

class ActivityFeedActivity : AppCompatActivity(), ActivityFeedContract.View {

    private lateinit var presenter: ActivityFeedPresenter
    private lateinit var textviewHeaderBusiness: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_feed, R.id.root)

        textviewHeaderBusiness = findViewById(R.id.textviewHeaderBusiness)

        presenter = ActivityFeedPresenter(this, ActivityFeedModel(authStore()))
        presenter.onViewReady(this)

        val stub = View.OnClickListener { presenter.onStubInteraction() }
        val buttonFeedCalendar = findViewById<View>(R.id.buttonFeedCalendar)
        val buttonFeedExport = findViewById<View>(R.id.buttonFeedExport)
        buttonFeedCalendar.setOnClickListener(stub)
        buttonFeedExport.setOnClickListener(stub)
    }

    override fun setHeaderBusinessName(text: String) {
        textviewHeaderBusiness.text = text
    }

    override fun setupNav(selectedItemId: Int) {
        setupMainBottomNavigation(selectedItemId)
    }

    override fun showComingSoon() {
        toast(getString(R.string.coming_soon))
    }
}
