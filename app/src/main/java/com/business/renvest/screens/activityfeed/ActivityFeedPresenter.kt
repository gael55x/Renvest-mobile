package com.business.renvest.screens.activityfeed

import android.content.Context
import android.os.Bundle
import com.business.renvest.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityFeedPresenter(
    private val view: ActivityFeedContract.View,
    private val model: ActivityFeedModel,
    private val scope: CoroutineScope,
) : ActivityFeedContract.Presenter {

    private var searchQuery: String = ""
    private var category: ActivityFeedCategory = ActivityFeedCategory.ALL
    private var todayOnly: Boolean = false

    override fun onViewReady(context: Context) {
        bindScreen(context)
    }

    fun restoreState(savedInstanceState: Bundle?) {
        if (savedInstanceState == null) return
        searchQuery = savedInstanceState.getString(KEY_SEARCH).orEmpty()
        category = ActivityFeedCategory.entries[
            savedInstanceState.getInt(KEY_CATEGORY, ActivityFeedCategory.ALL.ordinal)
        ]
        todayOnly = savedInstanceState.getBoolean(KEY_TODAY_ONLY, false)
        view.restoreSearchQuery(searchQuery)
        view.selectCategory(category)
    }

    fun saveState(outState: Bundle) {
        outState.putString(KEY_SEARCH, searchQuery)
        outState.putInt(KEY_CATEGORY, category.ordinal)
        outState.putBoolean(KEY_TODAY_ONLY, todayOnly)
    }

    override fun onLogEventClicked(context: Context) {
        scope.launch {
            val customers = withContext(Dispatchers.IO) { model.loadCustomersForPicker() }
            withContext(Dispatchers.Main) {
                view.showAddActivityEventDialog(customers) { type, customTitle, subtitle, customerId ->
                    onLogEventSubmitted(context, type, customTitle, subtitle, customerId)
                }
            }
        }
    }

    override fun onLogEventSubmitted(
        context: Context,
        type: ActivityLogType,
        customTitle: String,
        subtitle: String,
        customerId: String?,
    ) {
        val title = model.resolveTitle(type, customTitle)
        scope.launch {
            val ok = withContext(Dispatchers.IO) { model.addEvent(title, subtitle, customerId, type) }
            withContext(Dispatchers.Main) {
                if (ok) {
                    model.markOnboardingActivityStep(context)
                    bindScreen(context)
                    view.showToast(context.getString(R.string.activity_event_added_confirmation))
                } else {
                    view.showToast(context.getString(R.string.error_visit_requires_customer))
                }
            }
        }
    }

    override fun onActivityLongPressed(context: Context, row: ActivityEventRowUi) {
        view.showDeleteActivityConfirm(row.title) {
            scope.launch {
                withContext(Dispatchers.IO) { model.removeEvent(row.id) }
                withContext(Dispatchers.Main) { bindScreen(context) }
            }
        }
    }

    override fun onSearchQueryChanged(context: Context, query: String) {
        searchQuery = query
        bindScreen(context)
    }

    override fun onCategorySelected(context: Context, category: ActivityFeedCategory) {
        this.category = category
        bindScreen(context)
    }

    override fun onCalendarClicked(context: Context) {
        view.showDateRangeDialog(todayOnly) { selected ->
            onDateRangeSelected(context, selected)
        }
    }

    override fun onExportClicked(context: Context) {
        scope.launch {
            val file = withContext(Dispatchers.IO) { model.exportLocalData(context) }
            withContext(Dispatchers.Main) {
                view.shareExportFile(file)
                view.showToast(context.getString(R.string.export_saved_format, file.name))
            }
        }
    }

    override fun onDateRangeSelected(context: Context, todayOnly: Boolean) {
        this.todayOnly = todayOnly
        bindScreen(context)
    }

    private fun bindScreen(context: Context) {
        scope.launch {
            val counts = withContext(Dispatchers.IO) { model.localDataCounts() }
            val rows = withContext(Dispatchers.IO) {
                model.loadEvents(searchQuery, category, todayOnly)
            }
            val actionsLabel = context.getString(R.string.activity_actions_count_format, rows.size)
            withContext(Dispatchers.Main) {
                view.setHeaderBusinessName(model.businessDisplayName(context))
                view.setupNav(R.id.navActivity, counts.activityEvents)
                view.bindHeroMetrics(
                    events = counts.activityEvents.toString(),
                    customers = counts.customers.toString(),
                    promotions = counts.promotions.toString(),
                )
                view.bindActionsCount(actionsLabel)
                view.bindActivityRows(rows)
                view.setActivityEmptyVisible(rows.isEmpty())
            }
        }
    }

    companion object {
        private const val KEY_SEARCH = "activity_search"
        private const val KEY_CATEGORY = "activity_category"
        private const val KEY_TODAY_ONLY = "activity_today_only"
    }
}
