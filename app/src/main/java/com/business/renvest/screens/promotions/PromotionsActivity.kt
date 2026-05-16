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
            onItemClick = { presenter.onPromotionItemClicked(it) },
            onEditClick = { presenter.onStubInteraction() },
            onPauseClick = { presenter.onPromotionPauseClicked(this, it) },
            onDetailsClick = { presenter.onStubInteraction() },
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
    }

    override fun setHeaderBusinessName(text: String) {
        setTextViewText(R.id.textviewHeaderBusiness, text)
    }

    override fun setupNav(selectedItemId: Int) {
        setupMainBottomNavigation(selectedItemId)
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

    override fun showComingSoon() {
        toastComingSoon()
    }
}
