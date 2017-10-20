package net.chris.demo.guardian.database;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.raizlabs.android.dbflow.list.FlowCursorList;
import com.raizlabs.android.dbflow.sql.language.SQLCondition;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.Select;

import net.chris.demo.guardian.model.ContentModel;
import net.chris.demo.guardian.model.ContentModel_Table;
import net.chris.demo.guardian.model.SectionModel;
import net.chris.demo.guardian.system.DateTimeStruct;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

public class DatabaseHelper {

    public Cursor getContentCursor(@Nullable String section) {
        final List<SQLCondition> andConditions = new ArrayList<>(1);
        if (section != null && !section.isEmpty()) {
            andConditions.add(ContentModel_Table.sectionId.is(section));
        }
        return new FlowCursorList<>(SQLite.select().from(ContentModel.class).where().andAll(andConditions)).getCursor();
    }

    @NonNull
    public Observable<String> getEarliestTime(@Nullable String section) {
        final List<SQLCondition> andConditions = new ArrayList<>(1);
        if (section != null && !section.isEmpty()) {
            andConditions.add(ContentModel_Table.sectionId.is(section));
        }
        return Observable.defer(() -> Observable.just(new Select().from(ContentModel.class)
                .where().andAll(andConditions)
                .orderBy(ContentModel_Table.webPublicationDate, true)
                .querySingle()))
                .map(ContentModel::getWebPublicationDate)
                .map(DateTimeStruct::toString)
                .onErrorReturnItem("");
    }

    @NonNull
    public Observable<String> getLatestTime(@Nullable String section) {
        final List<SQLCondition> andConditions = new ArrayList<>(1);
        if (section != null && !section.isEmpty()) {
            andConditions.add(ContentModel_Table.sectionId.is(section));
        }
        return Observable.defer(() -> Observable.just(new Select().from(ContentModel.class)
                .where().andAll(andConditions)
                .orderBy(ContentModel_Table.webPublicationDate, false)
                .querySingle()))
                .map(ContentModel::getWebPublicationDate)
                .map(DateTimeStruct::toString)
                .onErrorReturnItem("");
    }

    public Cursor getSectionCursor() {
        return new FlowCursorList<>(SQLite.select().from(SectionModel.class)).getCursor();
    }

}
