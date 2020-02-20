package com.arjanvlek.oxygenupdater.setupwizard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.core.view.isVisible
import com.arjanvlek.oxygenupdater.R
import com.arjanvlek.oxygenupdater.adapters.ChooserOnboardingAdapter
import com.arjanvlek.oxygenupdater.internal.KotlinCallback
import com.arjanvlek.oxygenupdater.internal.addPlaceholderItemsForShimmer
import com.arjanvlek.oxygenupdater.models.SelectableModel
import com.arjanvlek.oxygenupdater.views.AbstractFragment
import kotlinx.android.synthetic.main.fragment_onboarding_chooser.*

abstract class ChooserOnboardingFragment : AbstractFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_onboarding_chooser, container, false).also {
            it.post {
                // placeholderItem's height is 2x 16dp padding + 24dp icon = 56dp
                addPlaceholderItemsForShimmer(inflater, container, it, R.layout.placeholder_onboarding_chooser, 56f)
            }
        }
    }

    abstract fun fetchData()

    /**
     * Sets up the [onboardingChooserRecyclerView]. This method must be called from child classes, and is thus annotated by [CallSuper]
     */
    @CallSuper
    open fun setupRecyclerView(data: List<SelectableModel>, initialSelectedIndex: Int = -1, onItemSelectedListener: KotlinCallback<SelectableModel> = {}) {
        // Do not load if app is in process of being exited when data arrives from server.
        if (activity == null || !isAdded) {
            return
        }

        onboardingChooserRecyclerView.let {
            it.adapter = ChooserOnboardingAdapter(context, data, initialSelectedIndex, onItemSelectedListener)

            // for better UX, scroll down to selection, if any
            if (initialSelectedIndex != -1) {
                it.layoutManager?.scrollToPosition(initialSelectedIndex)
            }
        }

        shimmerFrameLayout.isVisible = false
    }
}
