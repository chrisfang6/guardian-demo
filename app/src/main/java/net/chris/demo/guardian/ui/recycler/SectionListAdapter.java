package net.chris.demo.guardian.ui.recycler;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import net.chris.demo.guardian.R;

import static net.chris.demo.guardian.database.Columns.ID;
import static net.chris.demo.guardian.database.Columns.WEB_TITLE;

public class SectionListAdapter extends RecyclerView.Adapter<SectionViewHolder> {

    private LayoutInflater inflater;
    private Cursor cursor;
    private String selectedId = null;

    @Override
    public SectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SectionViewHolder(getLayoutInflater(parent.getContext()).inflate(R.layout.layout_list_item_section, parent, false),
                new SectionListItem.Builder().build());
    }

    @Override
    public void onBindViewHolder(SectionViewHolder holder, int position) {
        holder.bind(getItem(position, holder.getItem()));
    }

    private SectionListItem getItem(int position, @NonNull SectionListItem item) {
        if (cursor != null && cursor.moveToPosition(position)) {
            item.setId(cursor.getString(cursor.getColumnIndex(ID)));
            item.setSectionName(cursor.getString(cursor.getColumnIndex(WEB_TITLE)));
            item.setSelected(item.getId().equals(selectedId));
            return item;
        }
        return new SectionListItem.Builder().build();
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    private LayoutInflater getLayoutInflater(@NonNull final Context context) {
        if (inflater == null) {
            inflater = LayoutInflater.from(context);
        }
        return inflater;
    }

    public Cursor getCursor() {
        return cursor;
    }

    public void setCursor(Cursor cursor) {
        this.cursor = cursor;
    }

    public String getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }
}
