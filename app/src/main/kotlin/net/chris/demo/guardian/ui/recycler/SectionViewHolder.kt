package net.chris.demo.guardian.ui.recycler

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.view.View

import net.chris.demo.guardian.BR

class SectionViewHolder(itemView: View, val item: SectionListItem) : RecyclerView.ViewHolder(itemView) {
    private val binding: ViewDataBinding = DataBindingUtil.bind(itemView)

    fun bind(item: SectionListItem) {
        binding.setVariable(BR.item, item)
        binding.executePendingBindings()
    }
}
