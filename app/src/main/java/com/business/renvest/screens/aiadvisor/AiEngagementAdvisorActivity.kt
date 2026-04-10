package com.business.renvest.screens.aiadvisor

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.authStore
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast
import com.google.android.material.progressindicator.CircularProgressIndicator

class AiEngagementAdvisorActivity : AppCompatActivity(), AiEngagementAdvisorContract.View {

    private lateinit var presenter: AiEngagementAdvisorPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_ai_advisor, R.id.root)

        presenter = AiEngagementAdvisorPresenter(this, authStore())
        presenter.onViewReady(this)

        val stub = View.OnClickListener { presenter.onStubInteraction() }
        findViewById<View>(R.id.buttonAiRefresh).setOnClickListener(stub)
        findViewById<View>(R.id.buttonAiActivatePromo).setOnClickListener(stub)
        findViewById<View>(R.id.buttonAiViewData).setOnClickListener(stub)
    }

    override fun setHeaderBusinessName(text: String) {
        findViewById<TextView>(R.id.textviewHeaderBusiness).text = text
    }

    override fun setupNav(selectedItemId: Int) {
        setupMainBottomNavigation(selectedItemId)
    }

    override fun setEngagementProgress(percent: Int) {
        findViewById<CircularProgressIndicator>(R.id.circularprogressindicatorEngagementScore)
            .setProgressCompat(percent, false)
    }

    override fun showComingSoon() {
        toast(getString(R.string.coming_soon))
    }
}
