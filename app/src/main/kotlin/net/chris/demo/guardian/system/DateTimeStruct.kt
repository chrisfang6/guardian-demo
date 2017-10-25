package net.chris.demo.guardian.system

import android.os.Parcel
import android.os.Parcelable

import com.fasterxml.jackson.annotation.JsonCreator

import org.joda.time.DateTime
import org.joda.time.DateTimeZone
import org.joda.time.format.DateTimeFormatter
import org.joda.time.format.ISODateTimeFormat

/**
 * @see {@link DateTime}
 */
class DateTimeStruct : Parcelable {

    var dateTime: DateTime = DateTime.now()
        set(dateTime) {
            field = dateTime.withZone(DateTimeZone.UTC)
        }

    constructor() : super() {
        dateTime = DateTime.now()
    }

    constructor(dateTime: DateTime?) : this() {
        this.dateTime = dateTime ?: DateTime.now().withZone(DateTimeZone.UTC)
    }

    constructor(dateTime: String?) : this() {
        this.dateTime = if (dateTime != null) DEFAULT_FORMATTER.parseDateTime(dateTime) else DateTime.now().withZone(DateTimeZone.UTC)
    }

    constructor(`in`: Parcel) : super() {
        dateTime = `in`.readValue(DateTime::class.java.classLoader) as DateTime
    }

    override fun toString(): String {
        return this.dateTime.toString(DEFAULT_FORMATTER)
    }

    fun toString(formatter: DateTimeFormatter): String {
        return this.dateTime.toString(formatter)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(this.dateTime)
    }

    companion object {

        private val DEFAULT_FORMATTER = ISODateTimeFormat.dateTimeNoMillis()

        @JsonCreator
        fun create(dateTime: String?): DateTimeStruct {
            return DateTimeStruct(dateTime)
        }

        fun now(): DateTimeStruct {
            return DateTimeStruct()
        }

        val CREATOR: Parcelable.Creator<DateTimeStruct> = object : Parcelable.Creator<DateTimeStruct> {

            override fun createFromParcel(`in`: Parcel): DateTimeStruct {
                return DateTimeStruct(`in`)
            }

            override fun newArray(size: Int): Array<DateTimeStruct?> {
                return arrayOfNulls(size)
            }

        }
    }


}
