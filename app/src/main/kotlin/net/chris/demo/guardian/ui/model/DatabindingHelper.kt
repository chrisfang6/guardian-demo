package net.chris.demo.guardian.ui.model

import android.databinding.BindingAdapter
import android.view.View
import android.widget.LinearLayout

@BindingAdapter("bind:layout_weight")
fun setLayoutWeight(view: View?, weight: Int) {
    if (view == null) {
        return
    }
    var layoutParams: LinearLayout.LayoutParams? = view.layoutParams as LinearLayout.LayoutParams
    if (layoutParams == null) {
        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
    }
    layoutParams.weight = weight.toFloat()
    view.layoutParams = layoutParams
}