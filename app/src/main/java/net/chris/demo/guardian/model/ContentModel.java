package net.chris.demo.guardian.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import net.chris.demo.guardian.database.GuardianDatabase;
import net.chris.demo.guardian.system.DateTimeStruct;

import static net.chris.demo.guardian.database.Columns.API_URL;
import static net.chris.demo.guardian.database.Columns.ID;
import static net.chris.demo.guardian.database.Columns.SECTION_ID;
import static net.chris.demo.guardian.database.Columns.SECTION_NAME;
import static net.chris.demo.guardian.database.Columns.WEB_PUBLICATION_DATE;
import static net.chris.demo.guardian.database.Columns.WEB_TITLE;
import static net.chris.demo.guardian.database.Columns.WEB_URL;

@Table(database = GuardianDatabase.class, cachingEnabled = true, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
@JsonPropertyOrder({
        "id",
        "sectionId",
        "sectionName",
        "webPublicationDate",
        "webTitle",
        "webUrl",
        "apiUrl"
})
public class ContentModel extends BaseModel implements Parcelable {

    @PrimaryKey
    @Column(name = ID)
    @JsonProperty("id")
    private String id;
    @Column(name = SECTION_ID)
    @JsonProperty("sectionId")
    private String sectionId;
    @Column(name = SECTION_NAME)
    @JsonProperty("sectionName")
    private String sectionName;
    @Column(name = WEB_PUBLICATION_DATE)
    @JsonProperty("webPublicationDate")
    private DateTimeStruct webPublicationDate;
    @Column(name = WEB_TITLE)
    @JsonProperty("webTitle")
    private String webTitle;
    @Column(name = WEB_URL)
    @JsonProperty("webUrl")
    private String webUrl;
    @Column(name = API_URL)
    @JsonProperty("apiUrl")
    private String apiUrl;

    public final static Parcelable.Creator<ContentModel> CREATOR = new Creator<ContentModel>() {

        public ContentModel createFromParcel(Parcel in) {
            return new ContentModel(in);
        }

        public ContentModel[] newArray(int size) {
            return (new ContentModel[size]);
        }

    };

    public ContentModel() {
        super();
    }

    protected ContentModel(Parcel in) {
        super();
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.sectionId = ((String) in.readValue((String.class.getClassLoader())));
        this.sectionName = ((String) in.readValue((String.class.getClassLoader())));
        this.webPublicationDate = ((DateTimeStruct) in.readValue((DateTimeStruct.class.getClassLoader())));
        this.webTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.webUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.apiUrl = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * @param id
     * @param webUrl
     * @param sectionId
     * @param apiUrl
     * @param sectionName
     * @param webTitle
     * @param webPublicationDate
     */
    public ContentModel(@NonNull final String id,
                        @NonNull final String sectionId,
                        @NonNull final String sectionName,
                        @NonNull final DateTimeStruct webPublicationDate,
                        @NonNull final String webTitle,
                        @NonNull final String webUrl,
                        @NonNull final String apiUrl) {
        super();
        this.id = id;
        this.sectionId = sectionId;
        this.sectionName = sectionName;
        this.webPublicationDate = webPublicationDate;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
        this.apiUrl = apiUrl;
    }

    @JsonCreator
    public static ContentModel create(@JsonProperty("id") final String id,
                                      @JsonProperty("sectionId") final String sectionId,
                                      @JsonProperty("sectionName") final String sectionName,
                                      @JsonProperty("webPublicationDate") final DateTimeStruct webPublicationDate,
                                      @JsonProperty("webTitle") final String webTitle,
                                      @JsonProperty("webUrl") final String webUrl,
                                      @JsonProperty("apiUrl") String apiUrl) {
        return new ContentModel(id, sectionId, sectionName, webPublicationDate, webTitle, webUrl, apiUrl);
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("sectionId")
    public String getSectionId() {
        return sectionId;
    }

    @JsonProperty("sectionId")
    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    @JsonProperty("sectionName")
    public String getSectionName() {
        return sectionName;
    }

    @JsonProperty("sectionName")
    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    @JsonProperty("webPublicationDate")
    public DateTimeStruct getWebPublicationDate() {
        return webPublicationDate;
    }

    @JsonProperty("webPublicationDate")
    public void setWebPublicationDate(DateTimeStruct webPublicationDate) {
        this.webPublicationDate = webPublicationDate;
    }

    @JsonProperty("webTitle")
    public String getWebTitle() {
        return webTitle;
    }

    @JsonProperty("webTitle")
    public void setWebTitle(String webTitle) {
        this.webTitle = webTitle;
    }

    @JsonProperty("webUrl")
    public String getWebUrl() {
        return webUrl;
    }

    @JsonProperty("webUrl")
    public void setWebUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    @JsonProperty("apiUrl")
    public String getApiUrl() {
        return apiUrl;
    }

    @JsonProperty("apiUrl")
    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(sectionId);
        dest.writeValue(sectionName);
        dest.writeValue(webPublicationDate);
        dest.writeValue(webTitle);
        dest.writeValue(webUrl);
        dest.writeValue(apiUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}