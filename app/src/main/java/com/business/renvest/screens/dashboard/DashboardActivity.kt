package com.business.renvest.screens.dashboard

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.business.renvest.R
import com.business.renvest.screens.activityfeed.ActivityFeedActivity
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
import com.business.renvest.utils.toastComingSoon
import com.google.android.material.card.MaterialCardView

class DashboardActivity : AppCompatActivity(), DashboardContract.View {

    private lateinit var presenter: DashboardPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_dashboard, R.id.root)

        presenter = DashboardPresenter(this, DashboardModel(authStore(), renvestDb()), lifecycleScope)
        presenter.onViewReady(this)

        findViewById<View>(R.id.framelayoutHeaderNotification).setOnClickListener {
            presenter.onNotificationClicked()
        }
        findViewById<TextView>(R.id.textviewPerfViewReport).setOnClickListener {
            presenter.onPerfViewReportClicked()
        }
        findViewById<MaterialCardView>(R.id.materialcardPerfCellMembers).setOnClickListener {
            presenter.onPerfCellMembersClicked()
        }
        findViewById<MaterialCardView>(R.id.materialcardPerfCellRating).setOnClickListener {
            presenter.onPerfCellLoyaltyClicked()
        }
        findViewById<MaterialCardView>(R.id.materialcardPerfCellTicket).setOnClickListener {
            presenter.onPerfCellPromotionsClicked()
        }
        findViewById<MaterialCardView>(R.id.materialcardPerfCellChurn).setOnClickListener {
            presenter.onPerfCellActivityClicked()
        }
        findViewById<MaterialCardView>(R.id.materialcardAiInsight).setOnClickListener {
            presenter.onCardLocalInsightsClicked()
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
        findViewById<TextView>(R.id.textviewPerfRatingValue).text = model.perfLoyaltyPrograms
        findViewById<TextView>(R.id.textviewPerfTicketValue).text = model.perfPromotions
        findViewById<TextView>(R.id.textviewPerfTicketTrend).isVisible = model.perfPromotionsTrendVisible
        findViewById<TextView>(R.id.textviewPerfChurnValue).text = model.perfActivityEvents
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

    override fun navigateToActivityFeed() {
        startActivity(ActivityFeedActivity::class.java)
    }

    override fun navigateToLocalInsights() {
        startActivity(AiEngagementAdvisorActivity::class.java)
    }
}
