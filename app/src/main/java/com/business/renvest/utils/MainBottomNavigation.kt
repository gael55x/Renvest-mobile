package com.business.renvest.utils

import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import com.business.renvest.R
import com.business.renvest.data.local.localDataCounts
import com.business.renvest.screens.activityfeed.ActivityFeedActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.business.renvest.screens.customers.CustomersActivity
import com.business.renvest.screens.dashboard.DashboardActivity
import com.business.renvest.screens.profile.ProfileActivity
import com.business.renvest.screens.promotions.PromotionsActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

fun AppCompatActivity.setupMainBottomNavigation(
    @IdRes selectedItemId: Int,
    scope: CoroutineScope,
) {
    scope.launch {
        val badge = withContext(Dispatchers.IO) { renvestDb().localDataCounts().activityEvents }
        withContext(Dispatchers.Main) {
            setupMainBottomNavigation(selectedItemId, badge)
        }
    }
}

fun AppCompatActivity.setupMainBottomNavigation(@IdRes selectedItemId: Int, activityBadgeCount: Int) {
    val bottomnavigationMain = findViewById<BottomNavigationView>(R.id.bottomnavigationMain)
    bottomnavigationMain.selectedItemId = selectedItemId
    val badge = bottomnavigationMain.getOrCreateBadge(R.id.navActivity)
    if (activityBadgeCount > 0) {
        badge.isVisible = true
        badge.number = activityBadgeCount
    } else {
        badge.isVisible = false
        badge.clearNumber()
    }
    bottomnavigationMain.setOnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navHome -> navigateMainTab(DashboardActivity::class.java, selectedItemId == R.id.navHome)
            R.id.navCustomers -> navigateMainTab(CustomersActivity::class.java, selectedItemId == R.id.navCustomers)
            R.id.navPromos -> navigateMainTab(PromotionsActivity::class.java, selectedItemId == R.id.navPromos)
            R.id.navActivity -> navigateMainTab(ActivityFeedActivity::class.java, selectedItemId == R.id.navActivity)
            R.id.navProfile -> navigateMainTab(ProfileActivity::class.java, selectedItemId == R.id.navProfile)
            else -> false
        }
    }
}

private fun AppCompatActivity.navigateMainTab(target: Class<*>, alreadySelected: Boolean): Boolean {
    if (alreadySelected) return true
    startActivity(target)
    return true
}
