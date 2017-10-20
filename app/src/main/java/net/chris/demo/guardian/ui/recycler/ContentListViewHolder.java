package net.chris.demo.guardian.ui.recycler;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.chris.demo.guardian.BR;

public class ContentListViewHolder extends RecyclerView.ViewHolder {

    private final ContentListItem item;
    private ViewDataBinding binding;

    public ContentListViewHolder(@NonNull View itemView, @NonNull ContentListItem item) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        this.item = item;
    }

    public void bind(ContentListItem item) {
        binding.setVariable(BR.item, item);
        binding.executePendingBindings();
    }

    public ContentListItem getItem() {
        return item;
    }
}
