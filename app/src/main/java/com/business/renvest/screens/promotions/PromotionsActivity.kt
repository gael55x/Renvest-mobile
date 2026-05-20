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
import com.business.renvest.utils.toast
import com.business.renvest.utils.toastComingSoon
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText

class PromotionsActivity : AppCompatActivity(), PromotionsContract.View {

    private lateinit var presenter: PromotionsPresenter
    private lateinit var promotionsAdapter: PromotionsAdapter
    private lateinit var textviewPromotionsEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_promotions, R.id.root)

        textviewPromotionsEmpty = findViewById(R.id.textviewPromotionsEmpty)

        promotionsAdapter = PromotionsAdapter(
            onItemClick = { presenter.onPromotionEditClicked(this, it) },
            onEditClick = { presenter.onPromotionEditClicked(this, it) },
            onPauseClick = { presenter.onPromotionPauseClicked(this, it) },
            onDetailsClick = { presenter.onPromotionEditClicked(this, it) },
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
        presenter.onViewReady(this)

        findViewById<View>(R.id.buttonNewPromo).setOnClickListener {
            presenter.onNewPromoClicked(this)
        }

        findViewById<TabLayout>(R.id.tabPromoFilter).addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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

    override fun showNewPromotionDialog(onSubmit: (String, String, String) -> Unit) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_promotion, null, false)
        val titleField = dialogView.findViewById<TextInputEditText>(R.id.edittextPromoTitle)
        val rewardField = dialogView.findViewById<TextInputEditText>(R.id.edittextPromoReward)
        val expiryField = dialogView.findViewById<TextInputEditText>(R.id.edittextPromoExpiry)
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_add_promotion_title)
            .setView(dialogView)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_save) { _, _ ->
                onSubmit(
                    titleField.text?.toString().orEmpty(),
                    rewardField.text?.toString().orEmpty(),
                    expiryField.text?.toString().orEmpty(),
                )
            }
            .show()
    }

    override fun showEditPromotionDialog(item: PromotionItem, onSubmit: (String, String, String) -> Unit) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_promotion, null, false)
        val titleField = dialogView.findViewById<TextInputEditText>(R.id.edittextPromoTitle)
        val rewardField = dialogView.findViewById<TextInputEditText>(R.id.edittextPromoReward)
        val expiryField = dialogView.findViewById<TextInputEditText>(R.id.edittextPromoExpiry)
        titleField.setText(item.title)
        rewardField.setText(item.reward)
        expiryField.setText(item.expiry)
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.dialog_edit_promotion_title)
            .setView(dialogView)
            .setNegativeButton(android.R.string.cancel, null)
            .setPositiveButton(R.string.action_save) { _, _ ->
                onSubmit(
                    titleField.text?.toString().orEmpty(),
                    rewardField.text?.toString().orEmpty(),
                    expiryField.text?.toString().orEmpty(),
                )
            }
            .show()
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
}
