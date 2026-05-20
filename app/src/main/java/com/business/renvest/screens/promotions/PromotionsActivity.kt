package com.business.renvest.screens.promotions

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.business.renvest.R
import com.business.renvest.utils.authStore
import com.business.renvest.utils.renvestDb
import com.business.renvest.utils.setTextViewText
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.showValidatedFormDialog
import com.business.renvest.utils.toast
import com.business.renvest.utils.toastComingSoon
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class PromotionsActivity : AppCompatActivity(), PromotionsContract.View {

    private lateinit var presenter: PromotionsPresenter
    private lateinit var promotionsAdapter: PromotionsAdapter
    private lateinit var textviewPromotionsEmpty: TextView
    private lateinit var tabPromoFilter: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_promotions, R.id.root)

        textviewPromotionsEmpty = findViewById(R.id.textviewPromotionsEmpty)
        tabPromoFilter = findViewById(R.id.tabPromoFilter)

        promotionsAdapter = PromotionsAdapter(
            onItemClick = { presenter.onPromotionEditClicked(this, it) },
            onEditClick = { presenter.onPromotionEditClicked(this, it) },
            onPauseClick = { presenter.onPromotionPauseClicked(this, it) },
            onDetailsClick = { item ->
                if (item.enrolledCount > 0 && item.redeemedCount < item.enrolledCount) {
                    presenter.onPromotionRecordRedemptionClicked(this, item)
                } else {
                    presenter.onPromotionEditClicked(this, item)
                }
            },
            onLongClick = { presenter.onPromotionLongPressed(this, it) },
        )
        findViewById<RecyclerView>(R.id.recyclerviewPromotions).apply {
            layoutManager = LinearLayoutManager(this@PromotionsActivity)
            adapter = promotionsAdapter
        }

        presenter = PromotionsPresenter(
            this,
            PromotionsModel(authStore(), renvestDb()),
            lifecycleScope,
        )
        presenter.restoreState(savedInstanceState)
        presenter.onViewReady(this)

        findViewById<View>(R.id.buttonNewPromo).setOnClickListener {
            presenter.onNewPromoClicked(this)
        }

        tabPromoFilter.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val f = when (tab?.position) {
                    1 -> PromoFilter.ACTIVE
                    2 -> PromoFilter.PAUSED
                    else -> PromoFilter.ALL
                }
                presenter.onTabSelected(this@PromotionsActivity, f)
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) = Unit
            override fun onTabReselected(tab: TabLayout.Tab?) = Unit
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        presenter.saveState(outState)
    }

    override fun setHeaderBusinessName(text: String) {
        setTextViewText(R.id.textviewHeaderBusiness, text)
    }

    override fun setupNav(selectedItemId: Int, activityBadgeCount: Int) {
        setupMainBottomNavigation(selectedItemId, activityBadgeCount)
    }

    override fun bindPromotionsHero(activePromotions: String, customerRecords: String, activityRecords: String) {
        findViewById<TextView>(R.id.textviewPromoHeroActive).text = activePromotions
        findViewById<TextView>(R.id.textviewPromoHeroCustomers).text = customerRecords
        findViewById<TextView>(R.id.textviewPromoHeroActivity).text = activityRecords
    }

    override fun displayPromotions(items: List<PromotionItem>) {
        promotionsAdapter.submitList(items)
    }

    override fun setPromotionsEmptyVisible(visible: Boolean) {
        textviewPromotionsEmpty.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun showToast(message: String) {
        toast(message)
    }

    override fun showNewPromotionDialog(onSubmit: PromotionFormSubmit) {
        showPromotionFormDialog(
            titleRes = R.string.dialog_add_promotion_title,
            item = null,
            onSubmit = onSubmit,
        )
    }

    override fun showEditPromotionDialog(item: PromotionItem, onSubmit: PromotionFormSubmit) {
        showPromotionFormDialog(
            titleRes = R.string.dialog_edit_promotion_title,
            item = item,
            onSubmit = onSubmit,
        )
    }

    override fun showDeletePromotionConfirm(title: String, onConfirm: () -> Unit) {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_delete_promotion_title)
            .setMessage(getString(R.string.dialog_delete_promotion_message, title))
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_delete) { _, _ -> onConfirm() }
            .show()
    }

    override fun showComingSoon() {
        toastComingSoon()
    }

    override fun selectPromoFilter(filter: PromoFilter) {
        val position = when (filter) {
            PromoFilter.ACTIVE -> 1
            PromoFilter.PAUSED -> 2
            else -> 0
        }
        tabPromoFilter.getTabAt(position)?.select()
    }

    private fun showPromotionFormDialog(
        titleRes: Int,
        item: PromotionItem?,
        onSubmit: PromotionFormSubmit,
    ) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_promotion, null, false)
        val titleLayout = dialogView.findViewById<TextInputLayout>(R.id.textinputPromoTitle)
        val rewardLayout = dialogView.findViewById<TextInputLayout>(R.id.textinputPromoReward)
        val expiryLayout = dialogView.findViewById<TextInputLayout>(R.id.textinputPromoExpiry)
        val enrolledField = dialogView.findViewById<TextInputEditText>(R.id.edittextPromoEnrolled)
        val redeemedField = dialogView.findViewById<TextInputEditText>(R.id.edittextPromoRedeemed)
        if (item != null) {
            titleLayout.editText?.setText(item.title)
            rewardLayout.editText?.setText(item.reward)
            expiryLayout.editText?.setText(item.expiry)
            enrolledField.setText(item.enrolledCount.toString())
            redeemedField.setText(item.redeemedCount.toString())
        }
        showValidatedFormDialog(titleRes, dialogView, titleLayout, rewardLayout, expiryLayout) {
            onSubmit(
                titleLayout.editText?.text?.toString().orEmpty(),
                rewardLayout.editText?.text?.toString().orEmpty(),
                expiryLayout.editText?.text?.toString().orEmpty(),
                enrolledField.text?.toString()?.toIntOrNull() ?: 0,
                redeemedField.text?.toString()?.toIntOrNull() ?: 0,
            )
        }
    }
}
