package com.business.renvest.screens.promotions

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.business.renvest.R
import com.business.renvest.utils.authStore
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast

class PromotionsActivity : AppCompatActivity(), PromotionsContract.View {

    private lateinit var presenter: PromotionsPresenter
    private lateinit var promotionsAdapter: PromotionsAdapter
    private lateinit var textviewHeaderBusiness: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_promotions, R.id.root)

        textviewHeaderBusiness = findViewById(R.id.textviewHeaderBusiness)

        promotionsAdapter = PromotionsAdapter(
            onItemClick = { presenter.onPromotionItemClicked(it) },
            onSecondaryStub = { presenter.onStubInteraction() },
        )
        val recyclerviewPromotions = findViewById<RecyclerView>(R.id.recyclerviewPromotions)
        recyclerviewPromotions.apply {
            layoutManager = LinearLayoutManager(this@PromotionsActivity)
            adapter = promotionsAdapter
        }

        presenter = PromotionsPresenter(this, PromotionsModel(authStore()))
        presenter.onViewReady(this)

        val buttonNewPromo = findViewById<View>(R.id.buttonNewPromo)
        buttonNewPromo.setOnClickListener {
            presenter.onStubInteraction()
        }
    }

    override fun setHeaderBusinessName(text: String) {
        textviewHeaderBusiness.text = text
    }

    override fun setupNav(selectedItemId: Int) {
        setupMainBottomNavigation(selectedItemId)
    }

    override fun displayPromotions(items: List<PromotionItem>) {
        promotionsAdapter.submitList(items)
    }

    override fun showComingSoon() {
        toast(getString(R.string.coming_soon))
    }
}
