package net.chris.demo.guardian.database.converter

import com.raizlabs.android.dbflow.converter.TypeConverter

import net.chris.demo.guardian.system.DateTimeStruct

/**
 * Converts [DateTimeStruct]
 */
@com.raizlabs.android.dbflow.annotation.TypeConverter
class DateTimeStructConverter : TypeConverter<String, DateTimeStruct>() {

    override fun getDBValue(model: DateTimeStruct?): String? {
        return model?.toString()
    }

    override fun getModelValue(data: String?): DateTimeStruct? {
        return if (data == null) null else DateTimeStruct(data)
    }
}
