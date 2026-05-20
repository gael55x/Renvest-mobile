package com.business.renvest.screens.onboarding

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.business.renvest.R
import com.business.renvest.screens.activityfeed.ActivityFeedActivity
import com.business.renvest.screens.customers.CustomersActivity
import com.business.renvest.screens.dashboard.DashboardActivity
import com.business.renvest.screens.loyalty.LoyaltyActivity
import com.business.renvest.screens.promotions.PromotionsActivity
import com.business.renvest.utils.authStore
import com.business.renvest.utils.setupRenvestContent
import com.business.renvest.utils.startActivity
import com.business.renvest.utils.startActivityClearTask
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.progressindicator.LinearProgressIndicator

class OnboardingActivity : AppCompatActivity(), OnboardingContract.View {

    private lateinit var presenter: OnboardingPresenter
    private lateinit var adapter: OnboardingStepsAdapter
    private lateinit var progressLabel: TextView
    private lateinit var progressBar: LinearProgressIndicator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupRenvestContent(R.layout.activity_onboarding, R.id.root)

        progressLabel = findViewById(R.id.textviewOnboardingProgressLabel)
        progressBar = findViewById(R.id.progressOnboarding)

        adapter = OnboardingStepsAdapter { step ->
            presenter.onStepClicked(this, step)
        }
        findViewById<RecyclerView>(R.id.recyclerviewOnboardingSteps).apply {
            layoutManager = LinearLayoutManager(this@OnboardingActivity)
            adapter = this@OnboardingActivity.adapter
            itemAnimator = null
        }

        presenter = OnboardingPresenter(this, OnboardingModel(authStore()))
        presenter.onViewReady(this)

        findViewById<MaterialButton>(R.id.buttonOnboardingFinish).setOnClickListener {
            presenter.onFinishClicked(this)
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.onViewReady(this)
    }

    override fun bindProgress(completed: Int, total: Int) {
        progressLabel.text = getString(R.string.onboarding_progress_format, completed, total)
        progressBar.progress = if (total == 0) 0 else (completed * 100 / total)
    }

    override fun bindSteps(steps: List<OnboardingStepUi>) {
        bindProgress(steps.count { it.done }, steps.size)
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
        private val card = itemView as MaterialCardView
        private val icon = itemView.findViewById<ImageView>(R.id.imageviewOnboardingStepIcon)
        private val title = itemView.findViewById<TextView>(R.id.textviewOnboardingStepTitle)
        private val body = itemView.findViewById<TextView>(R.id.textviewOnboardingStepBody)
        private val status = itemView.findViewById<TextView>(R.id.textviewOnboardingStepStatus)

        fun bind(step: OnboardingStepUi, onClick: (OnboardingStepUi) -> Unit) {
            val context = itemView.context
            title.text = step.title
            body.text = step.description
            body.isVisible = step.description.isNotBlank()

            icon.setImageResource(stepIconRes(step.target))
            val primary = ContextCompat.getColor(context, R.color.primary)
            val muted = ContextCompat.getColor(context, R.color.text_secondary)

            val strokeDonePx = context.resources.getDimensionPixelSize(R.dimen.onboarding_step_stroke_done)
            val strokeTodoPx = context.resources.getDimensionPixelSize(R.dimen.onboarding_step_stroke_todo)
            val outline = ContextCompat.getColor(context, R.color.outline_soft)

            if (step.done) {
                status.text = context.getString(R.string.onboarding_step_done)
                status.setTextColor(primary)
                status.setBackgroundResource(R.drawable.bg_onboarding_status_done)
                status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_check_24, 0, 0, 0)
                status.compoundDrawableTintList = ColorStateList.valueOf(primary)
                card.strokeColor = primary
                card.strokeWidth = strokeDonePx
            } else {
                status.text = context.getString(R.string.onboarding_step_todo)
                status.setTextColor(primary)
                status.setBackgroundResource(R.drawable.bg_onboarding_status_todo)
                status.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_right_24, 0)
                status.compoundDrawableTintList = ColorStateList.valueOf(primary)
                card.strokeColor = outline
                card.strokeWidth = strokeTodoPx
            }

            title.setTextColor(if (step.done) muted else ContextCompat.getColor(context, R.color.text_primary))
            itemView.setOnClickListener { onClick(step) }
        }

        private fun stepIconRes(target: OnboardingTarget): Int = when (target) {
            OnboardingTarget.Customers -> R.drawable.ic_person_24
            OnboardingTarget.Promotions -> R.drawable.ic_promo_star_24
            OnboardingTarget.Activity -> R.drawable.ic_calendar_24
            OnboardingTarget.Loyalty -> R.drawable.ic_star_24
        }
    }
}
