package net.chris.demo.guardian.system;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

/**
 * @see {@link DateTime}
 */
public class DateTimeStruct implements Parcelable {

    private static final DateTimeFormatter DEFAULT_FORMATTER = ISODateTimeFormat.dateTimeNoMillis();

    private DateTime dateTime;

    public DateTimeStruct() {
        super();
        setDateTime(DateTime.now());
    }

    public DateTimeStruct(DateTime dateTime) {
        super();
        setDateTime((dateTime != null) ? dateTime : DateTime.now());
    }

    public DateTimeStruct(String dateTime) {
        super();
        setDateTime((dateTime != null) ? DEFAULT_FORMATTER.parseDateTime(dateTime) : DateTime.now());
    }

    public DateTimeStruct(Parcel in) {
        super();
        setDateTime(((DateTime) in.readValue((DateTime.class.getClassLoader()))));
    }

    @JsonCreator
    public static DateTimeStruct create(final String dateTime) {
        return new DateTimeStruct(dateTime);
    }

    public static DateTimeStruct now() {
        return new DateTimeStruct();
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(DateTime dateTime) {
        this.dateTime = dateTime.withZone(DateTimeZone.UTC);
    }

    @Override
    public String toString() {
        return dateTime.toString(DEFAULT_FORMATTER);
    }

    public String toString(@NonNull DateTimeFormatter formatter) {
        return dateTime.toString(formatter);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.dateTime);
    }

    public final static Parcelable.Creator<DateTimeStruct> CREATOR = new Creator<DateTimeStruct>() {

        public DateTimeStruct createFromParcel(Parcel in) {
            return new DateTimeStruct(in);
        }

        public DateTimeStruct[] newArray(int size) {
            return (new DateTimeStruct[size]);
        }

    };


}
