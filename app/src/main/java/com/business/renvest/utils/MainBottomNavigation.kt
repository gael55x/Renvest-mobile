package com.business.renvest.utils

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.screens.activityfeed.ActivityFeedActivity
import com.business.renvest.screens.customers.CustomersActivity
import com.business.renvest.screens.dashboard.DashboardActivity
import com.business.renvest.screens.profile.ProfileActivity
import com.business.renvest.screens.promotions.PromotionsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView

fun AppCompatActivity.setupMainBottomNavigation(@IdRes selectedItemId: Int) {
    val bottomnavigationMain = findViewById<BottomNavigationView>(R.id.bottomnavigationMain)
    bottomnavigationMain.selectedItemId = selectedItemId
    bottomnavigationMain.getOrCreateBadge(R.id.navActivity).apply {
        isVisible = true
        number = 3
    }
    bottomnavigationMain.setOnItemSelectedListener(NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navHome -> navigateMainTab(DashboardActivity::class.java, selectedItemId == R.id.navHome)
            R.id.navCustomers -> navigateMainTab(CustomersActivity::class.java, selectedItemId == R.id.navCustomers)
            R.id.navPromos -> navigateMainTab(PromotionsActivity::class.java, selectedItemId == R.id.navPromos)
            R.id.navActivity -> navigateMainTab(ActivityFeedActivity::class.java, selectedItemId == R.id.navActivity)
            R.id.navProfile -> navigateMainTab(ProfileActivity::class.java, selectedItemId == R.id.navProfile)
            else -> false
        }
    })
}

private fun AppCompatActivity.navigateMainTab(target: Class<*>, alreadySelected: Boolean): Boolean {
    if (alreadySelected) return true
    startActivity(target)
    return true
}
