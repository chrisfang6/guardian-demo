package net.chris.demo.guardian.ui.recycler

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import net.chris.demo.guardian.R
import net.chris.demo.guardian.database.Columns.Companion.ID
import net.chris.demo.guardian.database.Columns.Companion.SECTION_NAME
import net.chris.demo.guardian.database.Columns.Companion.WEB_PUBLICATION_DATE
import net.chris.demo.guardian.database.Columns.Companion.WEB_TITLE
import net.chris.demo.guardian.database.Columns.Companion.WEB_URL
import net.chris.demo.guardian.system.DateTimeStruct
import org.joda.time.format.DateTimeFormat


class ContentListAdapter : RecyclerView.Adapter<ContentListViewHolder>() {

    private val formatter = DateTimeFormat.forPattern("dd-MM-yyyy HH:mm:ss")

    private var inflater: LayoutInflater? = null
    var cursor: Cursor? = null
    private var selectedId: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentListViewHolder {
        return ContentListViewHolder(getLayoutInflater(parent.context).inflate(R.layout.layout_list_item_content, parent, false),
                ContentListItem.Builder().build())
    }

    override fun onBindViewHolder(holder: ContentListViewHolder, position: Int) {
        holder.bind(getItem(position, holder.item))
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }

    private fun getItem(position: Int, item: ContentListItem): ContentListItem {
        if (cursor?.moveToPosition(position) == true) {
            item.id = cursor?.getString(cursor!!.getColumnIndex(ID))
            item.sectionName = cursor?.getString(cursor!!.getColumnIndex(SECTION_NAME))
            item.webPublicationDate = DateTimeStruct.create(cursor?.getString(cursor!!.getColumnIndex(WEB_PUBLICATION_DATE))).toString(formatter)
            item.webTitle = cursor?.getString(cursor!!.getColumnIndex(WEB_TITLE))
            item.webUrl = cursor?.getString(cursor!!.getColumnIndex(WEB_URL))
            item.isSelected = item.id != null && item.id == selectedId
            return item
        }
        return ContentListItem.Builder().build()
    }

    private fun getLayoutInflater(context: Context): LayoutInflater {
        if (inflater == null) {
            inflater = LayoutInflater.from(context)
        }
        return inflater ?: LayoutInflater.from(context)
    }

    fun setSelectedId(selectedId: String) {
        this.selectedId = selectedId
    }
}
