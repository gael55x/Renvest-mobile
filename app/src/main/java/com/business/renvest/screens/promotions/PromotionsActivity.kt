package com.business.renvest.screens.promotions

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
            onSecondaryStub = { presenter.onStubInteraction() },
        )
        val recyclerviewPromotions = findViewById<RecyclerView>(R.id.recyclerviewPromotions)
        recyclerviewPromotions.apply {
            layoutManager = LinearLayoutManager(this@PromotionsActivity)
            adapter = promotionsAdapter
        }

        presenter = PromotionsPresenter(this, PromotionsModel(authStore(), renvestDb()))
        presenter.onViewReady(this)

        setClickListeners(
            View.OnClickListener { presenter.onStubInteraction() },
            R.id.buttonNewPromo,
        )
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

    override fun showComingSoon() {
        toastComingSoon()
    }
}
