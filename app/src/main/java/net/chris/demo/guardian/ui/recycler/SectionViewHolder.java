package net.chris.demo.guardian.ui.recycler;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.chris.demo.guardian.BR;

public class SectionViewHolder extends RecyclerView.ViewHolder {

    private final SectionListItem item;
    private final ViewDataBinding binding;

    public SectionViewHolder(View itemView, SectionListItem item) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        this.item = item;
    }

    public void bind(SectionListItem item) {
        binding.setVariable(BR.item, item);
        binding.executePendingBindings();
    }

    public SectionListItem getItem() {
        return item;
    }
}
