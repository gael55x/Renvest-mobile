package com.business.renvest.screens.dashboard

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.business.renvest.R
import com.business.renvest.screens.aiadvisor.AiEngagementAdvisorActivity
import com.business.renvest.screens.customers.CustomersActivity
import com.business.renvest.screens.loyalty.LoyaltyActivity
import com.business.renvest.screens.promotions.PromotionsActivity
import com.business.renvest.utils.authStore
import com.business.renvest.utils.renvestDb
import com.business.renvest.utils.setTextViewText
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.startActivity
import com.business.renvest.utils.toast
import com.business.renvest.utils.toastComingSoon
import com.google.android.material.card.MaterialCardView

class DashboardActivity : AppCompatActivity(), DashboardContract.View {

    private lateinit var presenter: DashboardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_dashboard, R.id.root)

        presenter = DashboardPresenter(this, DashboardModel(authStore(), renvestDb()))
        presenter.onViewReady(this)

        val framelayoutHeaderNotification = findViewById<View>(R.id.framelayoutHeaderNotification)
        val textviewPerfViewReport = findViewById<TextView>(R.id.textviewPerfViewReport)
        val materialcardHeroRevenue = findViewById<MaterialCardView>(R.id.materialcardHeroRevenue)
        val materialcardPerfCellMembers = findViewById<MaterialCardView>(R.id.materialcardPerfCellMembers)
        val materialcardPerfCellRating = findViewById<MaterialCardView>(R.id.materialcardPerfCellRating)
        val materialcardPerfCellTicket = findViewById<MaterialCardView>(R.id.materialcardPerfCellTicket)
        val materialcardPerfCellChurn = findViewById<MaterialCardView>(R.id.materialcardPerfCellChurn)
        val materialcardAiInsight = findViewById<MaterialCardView>(R.id.materialcardAiInsight)

        framelayoutHeaderNotification.setOnClickListener {
            presenter.onNotificationClicked()
        }

        textviewPerfViewReport.setOnClickListener {
            presenter.onPerfViewReportClicked()
        }

        materialcardHeroRevenue.setOnClickListener {
            /* static hero; no navigation */
        }

        materialcardPerfCellMembers.setOnClickListener {
            presenter.onPerfCellMembersClicked()
        }

        materialcardPerfCellRating.setOnClickListener {
            presenter.onPerfCellRatingClicked()
        }

        materialcardPerfCellTicket.setOnClickListener {
            presenter.onPerfCellTicketClicked()
        }

        materialcardPerfCellChurn.setOnClickListener {
            presenter.onPerfCellChurnClicked()
        }

        materialcardAiInsight.setOnClickListener {
            presenter.onCardAiInsightClicked()
        }

        setupMainBottomNavigation(R.id.navHome)
    }

    override fun setGreeting(text: String) {
        setTextViewText(R.id.textviewGreeting, text)
    }

    override fun setBusinessName(text: String) {
        setTextViewText(R.id.textviewBusinessName, text)
    }

    override fun bindDashboardMetrics(model: DashboardBindModel) {
        findViewById<TextView>(R.id.textviewAvatarInitials).text = model.avatarInitials
        findViewById<TextView>(R.id.textviewDashboardRevenueValue).text = model.revenueValue
        findViewById<TextView>(R.id.textviewDashboardRevenueGrowth).text = model.revenueSubline
        findViewById<TextView>(R.id.textviewHeroVisitsValue).text = model.visitsValue
        findViewById<TextView>(R.id.textviewHeroMembersValue).text = model.membersValue
        findViewById<TextView>(R.id.textviewHeroReturnValue).text = model.returnValue
        findViewById<TextView>(R.id.textviewPerfMembersValue).text = model.perfMembers
        findViewById<TextView>(R.id.textviewPerfMembersTrend).isVisible = model.perfMembersTrendVisible
        findViewById<TextView>(R.id.textviewPerfRatingValue).text = model.perfRating
        findViewById<TextView>(R.id.textviewPerfTicketValue).text = model.perfTicket
        findViewById<TextView>(R.id.textviewPerfTicketTrend).isVisible = model.perfTicketTrendVisible
        findViewById<TextView>(R.id.textviewPerfChurnValue).text = model.perfChurn
        findViewById<TextView>(R.id.textviewDashboardAiHeadline).text = model.aiTitle
        findViewById<TextView>(R.id.textviewDashboardAiBody).text = model.aiBody
    }

    override fun showComingSoon() {
        toastComingSoon()
    }

    override fun navigateToCustomers() {
        startActivity(CustomersActivity::class.java)
    }

    override fun navigateToLoyalty() {
        startActivity(LoyaltyActivity::class.java)
    }

    override fun navigateToPromotions() {
        startActivity(PromotionsActivity::class.java)
    }

    override fun navigateToAiAdvisor() {
        startActivity(AiEngagementAdvisorActivity::class.java)
    }
}
