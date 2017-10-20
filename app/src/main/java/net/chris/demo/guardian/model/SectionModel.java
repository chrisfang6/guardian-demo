package net.chris.demo.guardian.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ConflictAction;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import net.chris.demo.guardian.database.GuardianDatabase;

import java.util.List;

import static net.chris.demo.guardian.database.Columns.API_URL;
import static net.chris.demo.guardian.database.Columns.ID;
import static net.chris.demo.guardian.database.Columns.WEB_TITLE;
import static net.chris.demo.guardian.database.Columns.WEB_URL;

@Table(database = GuardianDatabase.class, cachingEnabled = true, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "webTitle",
        "webUrl",
        "apiUrl",
        "editions"
})
public class SectionModel extends BaseModel implements Parcelable {

    @PrimaryKey
    @Column(name = ID)
    @JsonProperty("id")
    private String id;
    @Column(name = WEB_TITLE)
    @JsonProperty("webTitle")
    private String webTitle;
    @Column(name = WEB_URL)
    @JsonProperty("webUrl")
    private String webUrl;
    @Column(name = API_URL)
    @JsonProperty("apiUrl")
    private String apiUrl;
    @JsonProperty("editions")
    private List<EditionModel> editions;

    public final static Parcelable.Creator<SectionModel> CREATOR = new Creator<SectionModel>() {

        public SectionModel createFromParcel(Parcel in) {
            return new SectionModel(in);
        }

        public SectionModel[] newArray(int size) {
            return (new SectionModel[size]);
        }

    };

    public SectionModel() {
        super();
    }

    protected SectionModel(Parcel in) {
        super();
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.webTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.webUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.apiUrl = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.editions, (EditionModel.class.getClassLoader()));
    }

    /**
     * @param editions
     * @param id
     * @param webUrl
     * @param apiUrl
     * @param webTitle
     */
    public SectionModel(@NonNull final String id,
                        @NonNull final String webTitle,
                        @NonNull final String webUrl,
                        @NonNull final String apiUrl,
                        @NonNull final List<EditionModel> editions) {
        super();
        this.id = id;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
        this.apiUrl = apiUrl;
        this.editions = editions;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
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

    @JsonProperty("editions")
    public List<EditionModel> getEditions() {
        return editions;
    }

    @JsonProperty("editions")
    public void setEditions(List<EditionModel> editions) {
        this.editions = editions;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(webTitle);
        dest.writeValue(webUrl);
        dest.writeValue(apiUrl);
        dest.writeList(editions);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}