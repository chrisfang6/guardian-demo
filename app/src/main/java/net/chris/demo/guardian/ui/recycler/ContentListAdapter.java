package net.chris.demo.guardian.ui.recycler;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import net.chris.demo.guardian.R;
import net.chris.demo.guardian.system.DateTimeStruct;

import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import static net.chris.demo.guardian.database.Columns.ID;
import static net.chris.demo.guardian.database.Columns.SECTION_NAME;
import static net.chris.demo.guardian.database.Columns.WEB_PUBLICATION_DATE;
import static net.chris.demo.guardian.database.Columns.WEB_TITLE;
import static net.chris.demo.guardian.database.Columns.WEB_URL;

public class ContentListAdapter extends RecyclerView.Adapter<ContentListViewHolder> {

    private DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    private LayoutInflater inflater;
    private Cursor cursor;
    private String selectedId;

    @Override
    public ContentListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ContentListViewHolder(getLayoutInflater(parent.getContext()).inflate(R.layout.layout_list_item_content, parent, false),
                new ContentListItem.Builder().build());
    }

    @Override
    public void onBindViewHolder(ContentListViewHolder holder, int position) {
        holder.bind(getItem(position, holder.getItem()));
    }

    @Override
    public int getItemCount() {
        return cursor != null ? cursor.getCount() : 0;
    }

    private ContentListItem getItem(int position, @NonNull ContentListItem item) {
        if (cursor != null && cursor.moveToPosition(position)) {
            item.setId(cursor.getString(cursor.getColumnIndex(ID)));
            item.setSectionName(cursor.getString(cursor.getColumnIndex(SECTION_NAME)));
            item.setWebPublicationDate(DateTimeStruct.create(cursor.getString(cursor.getColumnIndex(WEB_PUBLICATION_DATE))).toString(formatter));
            item.setWebTitle(cursor.getString(cursor.getColumnIndex(WEB_TITLE)));
            item.setWebUrl(cursor.getString(cursor.getColumnIndex(WEB_URL)));
            item.setSelected(item.getId().equals(selectedId));
            return item;
        }
        return new ContentListItem.Builder().build();
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

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }
}
