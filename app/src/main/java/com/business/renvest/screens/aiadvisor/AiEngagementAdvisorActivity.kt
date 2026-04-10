package com.business.renvest.screens.aiadvisor

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.authRepository
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast
import com.google.android.material.progressindicator.CircularProgressIndicator

class AiEngagementAdvisorActivity : AppCompatActivity(), AiEngagementAdvisorContract.View {

    private lateinit var presenter: AiEngagementAdvisorPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_ai_advisor, R.id.root)

        presenter = AiEngagementAdvisorPresenter(this, authRepository())
        presenter.onViewReady(this)

        val stub = View.OnClickListener { presenter.onStubInteraction() }
        findViewById<View>(R.id.button_ai_refresh).setOnClickListener(stub)
        findViewById<View>(R.id.button_ai_activate_promo).setOnClickListener(stub)
        findViewById<View>(R.id.button_ai_view_data).setOnClickListener(stub)
    }

    override fun setHeaderBusinessName(text: String) {
        findViewById<TextView>(R.id.text_header_business).text = text
    }

    override fun setupNav(selectedItemId: Int) {
        setupMainBottomNavigation(selectedItemId)
    }

    override fun setEngagementProgress(percent: Int) {
        findViewById<CircularProgressIndicator>(R.id.indicator_engagement_score)
            .setProgressCompat(percent, false)
    }

    override fun showComingSoon() {
        toast(getString(R.string.coming_soon))
    }
}
