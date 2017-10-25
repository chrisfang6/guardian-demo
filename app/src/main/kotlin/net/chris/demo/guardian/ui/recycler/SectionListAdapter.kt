package net.chris.demo.guardian.ui.recycler

import android.content.Context
import android.database.Cursor
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

import net.chris.demo.guardian.R
import net.chris.demo.guardian.database.Columns.Companion.ID
import net.chris.demo.guardian.database.Columns.Companion.WEB_TITLE


class SectionListAdapter : RecyclerView.Adapter<SectionViewHolder>() {

    private var inflater: LayoutInflater? = null
    var cursor: Cursor? = null
    var selectedId: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SectionViewHolder {
        return SectionViewHolder(getLayoutInflater(parent.context).inflate(R.layout.layout_list_item_section, parent, false),
                SectionListItem.Builder().build())
    }

    override fun onBindViewHolder(holder: SectionViewHolder, position: Int) {
        holder.bind(getItem(position, holder.item))
    }

    private fun getItem(position: Int, item: SectionListItem): SectionListItem {
        if (cursor?.moveToPosition(position) == true) {
            item.id = cursor?.getString(cursor!!.getColumnIndex(ID))
            item.sectionName = cursor?.getString(cursor!!.getColumnIndex(WEB_TITLE))
            item.isSelected = item.id != null && item.id == selectedId
            return item
        }
        return SectionListItem.Builder().build()
    }

    override fun getItemCount(): Int {
        return cursor?.count ?: 0
    }

    private fun getLayoutInflater(context: Context): LayoutInflater {
        if (inflater == null) {
            inflater = LayoutInflater.from(context)
        }
        return inflater ?: LayoutInflater.from(context)
    }
}
