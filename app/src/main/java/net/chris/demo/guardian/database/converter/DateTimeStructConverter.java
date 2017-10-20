package net.chris.demo.guardian.database.converter;

import com.raizlabs.android.dbflow.converter.TypeConverter;

import net.chris.demo.guardian.system.DateTimeStruct;

/**
 * Converts {@link DateTimeStruct}
 */
@com.raizlabs.android.dbflow.annotation.TypeConverter
public final class DateTimeStructConverter extends TypeConverter<String, DateTimeStruct> {

    public DateTimeStructConverter() {
    }

    @Override
    public String getDBValue(DateTimeStruct model) {
        return model == null ? null : model.toString();
    }

    @Override
    public DateTimeStruct getModelValue(String data) {
        return data == null ? null : new DateTimeStruct(data);
    }
}
