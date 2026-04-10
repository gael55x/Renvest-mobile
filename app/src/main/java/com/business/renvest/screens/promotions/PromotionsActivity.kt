package com.business.renvest.screens.promotions

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.authRepository
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast

class PromotionsActivity : AppCompatActivity(), PromotionsContract.View {

    private lateinit var presenter: PromotionsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_promotions, R.id.root)

        presenter = PromotionsPresenter(this, authRepository())
        presenter.onViewReady(this)

        val stub = View.OnClickListener { presenter.onStubInteraction() }
        findViewById<View>(R.id.button_new_promo).setOnClickListener(stub)
        findViewById<View>(R.id.button_promo1_edit).setOnClickListener(stub)
        findViewById<View>(R.id.button_promo1_pause).setOnClickListener(stub)
        findViewById<View>(R.id.text_promo1_details).setOnClickListener(stub)
        findViewById<View>(R.id.button_promo2_edit).setOnClickListener(stub)
        findViewById<View>(R.id.button_promo2_pause).setOnClickListener(stub)
        findViewById<View>(R.id.text_promo2_details).setOnClickListener(stub)
    }

    override fun setHeaderBusinessName(text: String) {
        findViewById<TextView>(R.id.text_header_business).text = text
    }

    override fun setupNav(selectedItemId: Int) {
        setupMainBottomNavigation(selectedItemId)
    }

    override fun showComingSoon() {
        toast(getString(R.string.coming_soon))
    }
}
