package net.chris.demo.guardian.database

import android.database.Cursor
import com.raizlabs.android.dbflow.kotlinextensions.select
import com.raizlabs.android.dbflow.sql.language.SQLOperator
import io.reactivex.Observable
import net.chris.demo.guardian.model.ContentModel
import net.chris.demo.guardian.model.ContentModel_Table
import net.chris.demo.guardian.model.SectionModel
import java.util.*

class DatabaseHelper {

    val sectionCursor: Cursor?
        get() = select.from(SectionModel::class.java).cursorList().cursor()

    fun getContentCursor(section: String?): Cursor? {
        val andConditions = ArrayList<SQLOperator>(1)
        if (section != null && !section.isEmpty()) {
            andConditions.add(ContentModel_Table.sectionId.`is`(section))
        }
        return select.from(ContentModel::class.java).where().andAll(andConditions).cursorList().cursor()
    }

    fun getEarliestTime(section: String?): Observable<String> {
        val andConditions = ArrayList<SQLOperator>(1)
        if (section != null && !section.isEmpty()) {
            andConditions.add(ContentModel_Table.sectionId.`is`(section))
        }
        return Observable.defer { Observable.just(select.from(ContentModel::class.java)) }
                .map { it.where().andAll(andConditions) }
                .map { it.orderBy(ContentModel_Table.webPublicationDate, true) }
                .map { it.querySingle() }
                .map { it?.webPublicationDate.toString() }
                .onErrorReturnItem("")

    }

    fun getLatestTime(section: String?): Observable<String> {
        val andConditions = ArrayList<SQLOperator>(1)
        if (section != null && !section.isEmpty()) {
            andConditions.add(ContentModel_Table.sectionId.`is`(section))
        }
        return Observable.defer { Observable.just(select.from(ContentModel::class.java)) }
                .map { it.where().andAll(andConditions) }
                .map { it.orderBy(ContentModel_Table.webPublicationDate, false) }
                .map { it.querySingle() }
                .map { it?.webPublicationDate.toString() }
                .onErrorReturnItem("")
    }

}
