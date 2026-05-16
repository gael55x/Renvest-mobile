package com.business.renvest.screens.activityfeed

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.business.renvest.R
import com.business.renvest.utils.authStore
import com.business.renvest.utils.renvestDb
import com.business.renvest.utils.setClickListeners
import com.business.renvest.utils.setTextViewText
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast
import com.business.renvest.utils.toastComingSoon
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText

class ActivityFeedActivity : AppCompatActivity(), ActivityFeedContract.View {

    private lateinit var presenter: ActivityFeedPresenter
    private lateinit var activityAdapter: ActivityFeedAdapter
    private lateinit var textviewActivityFeedEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_feed, R.id.root)

        textviewActivityFeedEmpty = findViewById(R.id.textviewActivityFeedEmpty)
        activityAdapter = ActivityFeedAdapter { row ->
            presenter.onActivityLongPressed(this, row)
        }
        findViewById<RecyclerView>(R.id.recyclerviewActivityFeed).apply {
            layoutManager = LinearLayoutManager(this@ActivityFeedActivity)
            adapter = activityAdapter
        }

        presenter = ActivityFeedPresenter(
            this,
            ActivityFeedModel(authStore(), renvestDb()),
            lifecycleScope,
        )
        presenter.onViewReady(this)

        val stub = View.OnClickListener { presenter.onStubInteraction() }
        setClickListeners(stub, R.id.buttonFeedCalendar, R.id.buttonFeedExport)
        findViewById<View>(R.id.buttonFeedAddEvent).setOnClickListener {
            presenter.onLogEventClicked(this)
        }
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

    override fun showToast(message: String) {
        toast(message)
    }

    override fun showAddActivityEventDialog(onSubmit: (String, String) -> Unit) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_activity_event, null, false)
        val titleField = dialogView.findViewById<TextInputEditText>(R.id.edittextActivityTitle)
        val subtitleField = dialogView.findViewById<TextInputEditText>(R.id.edittextActivitySubtitle)
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_add_activity_title)
            .setView(dialogView)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_save) { _, _ ->
                onSubmit(
                    titleField.text?.toString().orEmpty(),
                    subtitleField.text?.toString().orEmpty(),
                )
            }
            .show()
    }

    override fun showDeleteActivityConfirm(title: String, onConfirm: () -> Unit) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_activity_title)
            .setMessage(getString(R.string.dialog_delete_activity_message, title))
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_delete) { _, _ -> onConfirm() }
            .show()
    }

    override fun showComingSoon() {
        toastComingSoon()
    }
}
