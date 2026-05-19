package com.business.renvest.screens.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.business.renvest.R
import com.business.renvest.screens.activityfeed.ActivityFeedActivity
import com.business.renvest.screens.customers.CustomersActivity
import com.business.renvest.screens.dashboard.DashboardActivity
import com.business.renvest.screens.loyalty.LoyaltyActivity
import com.business.renvest.screens.promotions.PromotionsActivity
import com.business.renvest.utils.authStore
import com.business.renvest.utils.renvestDb
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.startActivity
import com.business.renvest.utils.startActivityClearTask
import com.google.android.material.button.MaterialButton

class OnboardingActivity : AppCompatActivity(), OnboardingContract.View {

    private lateinit var presenter: OnboardingPresenter
    private lateinit var adapter: OnboardingStepsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_onboarding, R.id.root)

        adapter = OnboardingStepsAdapter { step ->
            presenter.onStepClicked(this, step)
        }
        findViewById<RecyclerView>(R.id.recyclerviewOnboardingSteps).apply {
            layoutManager = LinearLayoutManager(this@OnboardingActivity)
            adapter = this@OnboardingActivity.adapter
        }

        presenter = OnboardingPresenter(this, OnboardingModel(authStore(), renvestDb()))
        presenter.onViewReady(this)

        findViewById<MaterialButton>(R.id.buttonOnboardingFinish).setOnClickListener {
            presenter.onFinishClicked(this)
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewReady(this)
    }

    override fun bindSteps(steps: List<OnboardingStepUi>) {
        adapter.submit(steps)
    }

    override fun navigateToDashboard() {
        startActivityClearTask(DashboardActivity::class.java)
        finish()
    }

    override fun navigateToCustomers() {
        startActivity(CustomersActivity::class.java)
    }

    override fun navigateToPromotions() {
        startActivity(PromotionsActivity::class.java)
    }

    override fun navigateToActivity() {
        startActivity(ActivityFeedActivity::class.java)
    }

    override fun navigateToLoyalty() {
        startActivity(LoyaltyActivity::class.java)
    }
}

private class OnboardingStepsAdapter(
    private val onStepClick: (OnboardingStepUi) -> Unit,
) : RecyclerView.Adapter<OnboardingStepsAdapter.Holder>() {

    private var items: List<OnboardingStepUi> = emptyList()

    fun submit(list: List<OnboardingStepUi>) {
        items = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding_step, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(items[position], onStepClick)
    }

    override fun getItemCount(): Int = items.size

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title = itemView.findViewById<TextView>(R.id.textviewOnboardingStepTitle)
        private val body = itemView.findViewById<TextView>(R.id.textviewOnboardingStepBody)
        private val status = itemView.findViewById<TextView>(R.id.textviewOnboardingStepStatus)

        fun bind(step: OnboardingStepUi, onClick: (OnboardingStepUi) -> Unit) {
            title.text = step.title
            body.text = step.description
            status.text = itemView.context.getString(
                if (step.done) R.string.onboarding_step_done else R.string.onboarding_step_todo,
            )
            itemView.setOnClickListener { onClick(step) }
        }
    }
}
