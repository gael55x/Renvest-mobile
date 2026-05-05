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
    private lateinit var textviewHeaderBusiness: TextView
    private lateinit var circularprogressindicatorEngagementScore: CircularProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_ai_advisor, R.id.root)

        textviewHeaderBusiness = findViewById(R.id.textviewHeaderBusiness)
        circularprogressindicatorEngagementScore = findViewById(R.id.circularprogressindicatorEngagementScore)

        presenter = AiEngagementAdvisorPresenter(this, AiEngagementAdvisorModel(authStore()))
        presenter.onViewReady(this)

        val stub = View.OnClickListener { presenter.onStubInteraction() }
        val buttonAiRefresh = findViewById<View>(R.id.buttonAiRefresh)
        val buttonAiActivatePromo = findViewById<View>(R.id.buttonAiActivatePromo)
        val buttonAiViewData = findViewById<View>(R.id.buttonAiViewData)
        buttonAiRefresh.setOnClickListener(stub)
        buttonAiActivatePromo.setOnClickListener(stub)
        buttonAiViewData.setOnClickListener(stub)
    }

    override fun setHeaderBusinessName(text: String) {
        textviewHeaderBusiness.text = text
    }

    override fun setupNav(selectedItemId: Int) {
        setupMainBottomNavigation(selectedItemId)
    }

    override fun setEngagementProgress(percent: Int) {
        circularprogressindicatorEngagementScore.setProgressCompat(percent, false)
    }

    override fun showComingSoon() {
        toast(getString(R.string.coming_soon))
    }
}
