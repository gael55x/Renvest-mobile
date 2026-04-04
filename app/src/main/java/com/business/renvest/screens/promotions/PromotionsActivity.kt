package com.business.renvest.screens.promotions

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.utils.applyEdgeToEdgeInsets

class PromotionsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_feature_stub)
        applyEdgeToEdgeInsets(R.id.root)
        findViewById<TextView>(R.id.text_stub_title).setText(R.string.feature_promotions_title)
    }
}
