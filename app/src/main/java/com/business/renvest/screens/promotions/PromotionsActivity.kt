package com.business.renvest.screens.promotions

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.authStore
import com.business.renvest.utils.setupMainBottomNavigation
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.toast

class PromotionsActivity : AppCompatActivity(), PromotionsContract.View {

    private lateinit var presenter: PromotionsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_promotions, R.id.root)

        presenter = PromotionsPresenter(this, authStore())
        presenter.onViewReady(this)

        val stub = View.OnClickListener { presenter.onStubInteraction() }
        findViewById<View>(R.id.buttonNewPromo).setOnClickListener(stub)
        findViewById<View>(R.id.buttonPromo1Edit).setOnClickListener(stub)
        findViewById<View>(R.id.buttonPromo1Pause).setOnClickListener(stub)
        findViewById<View>(R.id.textviewPromo1Details).setOnClickListener(stub)
        findViewById<View>(R.id.buttonPromo2Edit).setOnClickListener(stub)
        findViewById<View>(R.id.buttonPromo2Pause).setOnClickListener(stub)
        findViewById<View>(R.id.textviewPromo2Details).setOnClickListener(stub)
    }

    override fun setHeaderBusinessName(text: String) {
        findViewById<TextView>(R.id.textviewHeaderBusiness).text = text
    }

    override fun setupNav(selectedItemId: Int) {
        setupMainBottomNavigation(selectedItemId)
    }

    override fun showComingSoon() {
        toast(getString(R.string.coming_soon))
    }
}
