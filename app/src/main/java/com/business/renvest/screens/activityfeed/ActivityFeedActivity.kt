package com.business.renvest.screens.activityfeed

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.business.renvest.R
import com.business.renvest.utils.authStore
import com.business.renvest.utils.renvestDb
import com.business.renvest.utils.setClickListeners
import com.business.renvest.utils.setTextViewText
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toastComingSoon

class ActivityFeedActivity : AppCompatActivity(), ActivityFeedContract.View {

    private lateinit var presenter: ActivityFeedPresenter
    private lateinit var activityAdapter: ActivityFeedAdapter
    private lateinit var textviewActivityFeedEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_feed, R.id.root)

        textviewActivityFeedEmpty = findViewById(R.id.textviewActivityFeedEmpty)
        activityAdapter = ActivityFeedAdapter()
        findViewById<RecyclerView>(R.id.recyclerviewActivityFeed).apply {
            layoutManager = LinearLayoutManager(this@ActivityFeedActivity)
            adapter = activityAdapter
        }

        presenter = ActivityFeedPresenter(this, ActivityFeedModel(authStore(), renvestDb()))
        presenter.onViewReady(this)

        val stub = View.OnClickListener { presenter.onStubInteraction() }
        setClickListeners(stub, R.id.buttonFeedCalendar, R.id.buttonFeedExport)
    }

    override fun setHeaderBusinessName(text: String) {
        setTextViewText(R.id.textviewHeaderBusiness, text)
    }

    override fun setupNav(selectedItemId: Int) {
        setupMainBottomNavigation(selectedItemId)
    }

    override fun bindHeroMetrics(events: String, customers: String, promotions: String) {
        findViewById<TextView>(R.id.textviewFeedHeroEvents).text = events
        findViewById<TextView>(R.id.textviewFeedHeroCustomers).text = customers
        findViewById<TextView>(R.id.textviewFeedHeroPromotions).text = promotions
    }

    override fun bindActivityRows(items: List<ActivityEventRowUi>) {
        activityAdapter.submitList(items)
    }

    override fun setActivityEmptyVisible(visible: Boolean) {
        textviewActivityFeedEmpty.isVisible = visible
    }

    override fun showComingSoon() {
        toastComingSoon()
    }
}
