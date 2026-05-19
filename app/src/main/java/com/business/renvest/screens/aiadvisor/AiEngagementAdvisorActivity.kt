package com.business.renvest.screens.aiadvisor

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.authStore
import com.business.renvest.utils.renvestDb
import com.business.renvest.utils.setTextViewText
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.startActivity

class AiEngagementAdvisorActivity : AppCompatActivity(), AiEngagementAdvisorContract.View {

    private lateinit var presenter: AiEngagementAdvisorPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_ai_advisor, R.id.root)

        presenter = AiEngagementAdvisorPresenter(this, AiEngagementAdvisorModel(authStore(), renvestDb()))
        presenter.onViewReady(this)

        findViewById<android.view.View>(R.id.buttonAiActivatePromo).setOnClickListener {
            presenter.onActivatePromoClicked(this)
        }
        findViewById<android.view.View>(R.id.buttonAiViewData).setOnClickListener {
            presenter.onViewDataClicked(this)
        }
    }

    override fun setHeaderBusinessName(text: String) {
        setTextViewText(R.id.textviewHeaderBusiness, text)
    }

    override fun setupNav(selectedItemId: Int) {
        setupMainBottomNavigation(selectedItemId)
    }

    override fun bindLocalAdvisor(title: String, body: String) {
        findViewById<TextView>(R.id.textviewAiSummaryTitle).text = title
        findViewById<TextView>(R.id.textviewAiSummaryBody).text = body
    }

    override fun navigateTo(target: Class<*>) {
        startActivity(target)
    }
}
