package com.business.renvest.screens.activityfeed

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.business.renvest.R
import com.business.renvest.utils.authStore
import com.business.renvest.utils.renvestDb
import com.business.renvest.utils.setTextViewText
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast
import android.content.DialogInterface
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import java.io.File

class ActivityFeedActivity : AppCompatActivity(), ActivityFeedContract.View {

    private lateinit var presenter: ActivityFeedPresenter
    private lateinit var activityAdapter: ActivityFeedAdapter
    private lateinit var textviewActivityFeedEmpty: TextView
    private lateinit var searchField: TextInputEditText

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

        searchField = findViewById(R.id.edittextSearchActivityInput)

        presenter = ActivityFeedPresenter(
            this,
            ActivityFeedModel(authStore(), renvestDb()),
            lifecycleScope,
        )
        presenter.restoreState(savedInstanceState)
        presenter.onViewReady(this)

        val logClick = View.OnClickListener { presenter.onLogEventClicked(this) }
        findViewById<View>(R.id.buttonFeedAddEvent).setOnClickListener(logClick)
        findViewById<View>(R.id.fabLogActivity).setOnClickListener(logClick)
        findViewById<View>(R.id.buttonFeedCalendar).setOnClickListener {
            presenter.onCalendarClicked(this)
        }
        findViewById<View>(R.id.buttonFeedExport).setOnClickListener {
            presenter.onExportClicked(this)
        }

        searchField.doAfterTextChanged { text ->
            presenter.onSearchQueryChanged(this, text?.toString().orEmpty())
        }

        findViewById<ChipGroup>(R.id.chipgroupActivityFeed).setOnCheckedStateChangeListener { group, checkedIds ->
            val id = checkedIds.firstOrNull() ?: return@setOnCheckedStateChangeListener
            val chip = group.findViewById<Chip>(id) ?: return@setOnCheckedStateChangeListener
            val category = when (chip.id) {
                R.id.chipFeedPoints -> ActivityFeedCategory.POINTS
                R.id.chipFeedRewards -> ActivityFeedCategory.REWARD
                R.id.chipFeedVisits -> ActivityFeedCategory.VISIT
                else -> ActivityFeedCategory.ALL
            }
            presenter.onCategorySelected(this, category)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.saveState(outState)
    }

    override fun restoreSearchQuery(query: String) {
        if (searchField.text?.toString() != query) {
            searchField.setText(query)
        }
    }

    override fun selectCategory(category: ActivityFeedCategory) {
        val chipId = when (category) {
            ActivityFeedCategory.POINTS -> R.id.chipFeedPoints
            ActivityFeedCategory.REWARD -> R.id.chipFeedRewards
            ActivityFeedCategory.VISIT -> R.id.chipFeedVisits
            ActivityFeedCategory.ALL -> R.id.chipFeedAll
        }
        findViewById<Chip>(chipId).isChecked = true
    }

    override fun setHeaderBusinessName(text: String) {
        setTextViewText(R.id.textviewHeaderBusiness, text)
    }

    override fun setupNav(selectedItemId: Int, activityBadgeCount: Int) {
        setupMainBottomNavigation(selectedItemId, activityBadgeCount)
    }

    override fun bindHeroMetrics(events: String, customers: String, promotions: String) {
        findViewById<TextView>(R.id.textviewFeedHeroEvents).text = events
        findViewById<TextView>(R.id.textviewFeedHeroCustomers).text = customers
        findViewById<TextView>(R.id.textviewFeedHeroPromotions).text = promotions
    }

    override fun bindActivityRows(items: List<ActivityEventRowUi>) {
        activityAdapter.submitList(items)
    }

    override fun bindActionsCount(label: String) {
        findViewById<TextView>(R.id.textviewActivityActionsCount).text = label
    }

    override fun setActivityEmptyVisible(visible: Boolean) {
        textviewActivityFeedEmpty.isVisible = visible
    }

    override fun showToast(message: String) {
        toast(message)
    }

    override fun showAddActivityEventDialog(
        customers: List<Pair<String, String>>,
        onSubmit: (ActivityLogType, String, String, String?) -> Unit,
    ) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_activity_event, null, false)
        val typeSpinner = dialogView.findViewById<Spinner>(R.id.spinnerActivityType)
        val customerSpinner = dialogView.findViewById<Spinner>(R.id.spinnerActivityCustomer)
        val titleLayout = dialogView.findViewById<TextInputLayout>(R.id.textinputActivityTitle)
        val titleField = dialogView.findViewById<TextInputEditText>(R.id.edittextActivityTitle)
        val subtitleField = dialogView.findViewById<TextInputEditText>(R.id.edittextActivitySubtitle)

        val typeLabels = listOf(
            getString(R.string.activity_type_visit),
            getString(R.string.activity_type_points),
            getString(R.string.activity_type_reward),
            getString(R.string.activity_type_custom),
        )
        val types = listOf(
            ActivityLogType.VISIT,
            ActivityLogType.POINTS,
            ActivityLogType.REWARD,
            ActivityLogType.CUSTOM,
        )
        typeSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, typeLabels)

        val labels = mutableListOf(getString(R.string.dialog_activity_customer_none))
        val ids = mutableListOf<String?>(null)
        customers.forEach { (id, name) ->
            ids.add(id)
            labels.add(name)
        }
        customerSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, labels)

        fun updateTitleVisibility() {
            val isCustom = types.getOrNull(typeSpinner.selectedItemPosition) == ActivityLogType.CUSTOM
            titleLayout.isVisible = isCustom
            if (!isCustom) titleField.text?.clear()
        }
        typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                updateTitleVisibility()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) = Unit
        }
        updateTitleVisibility()

        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_add_activity_title)
            .setView(dialogView)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_save) { _, _ ->
                val type = types.getOrElse(typeSpinner.selectedItemPosition) { ActivityLogType.CUSTOM }
                val customerId = ids.getOrNull(customerSpinner.selectedItemPosition)
                onSubmit(
                    type,
                    titleField.text?.toString().orEmpty(),
                    subtitleField.text?.toString().orEmpty(),
                    customerId,
                )
            }
            .create()
        dialog.setOnShowListener {
            val saveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE) ?: return@setOnShowListener
            val validate = {
                val type = types.getOrElse(typeSpinner.selectedItemPosition) { ActivityLogType.CUSTOM }
                val visitOk = type != ActivityLogType.VISIT || customerSpinner.selectedItemPosition > 0
                val customOk = type != ActivityLogType.CUSTOM ||
                    !titleField.text?.toString().orEmpty().trim().isNullOrEmpty()
                saveButton.isEnabled = visitOk && customOk
            }
            typeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    updateTitleVisibility()
                    validate()
                }
                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }
            customerSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) = validate()
                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }
            titleField.doAfterTextChanged { validate() }
            validate()
        }
        dialog.show()
    }

    override fun showDeleteActivityConfirm(title: String, onConfirm: () -> Unit) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_activity_title)
            .setMessage(getString(R.string.dialog_delete_activity_message, title))
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_delete) { _, _ -> onConfirm() }
            .show()
    }

    override fun showDateRangeDialog(todayOnly: Boolean, onSelected: (Boolean) -> Unit) {
        val options = arrayOf(
            getString(R.string.feed_range_all_time),
            getString(R.string.feed_range_today),
        )
        val checked = if (todayOnly) 1 else 0
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_feed_date_range_title)
            .setSingleChoiceItems(options, checked) { dialog, which ->
                onSelected(which == 1)
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel, null)
            .show()
    }

    override fun shareExportFile(file: File) {
        val uri = FileProvider.getUriForFile(this, "$packageName.fileprovider", file)
        startActivity(
            Intent(Intent.ACTION_SEND).apply {
                type = "application/json"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            },
        )
    }
}
