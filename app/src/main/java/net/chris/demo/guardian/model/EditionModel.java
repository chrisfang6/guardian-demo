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

import static net.chris.demo.guardian.database.Columns.API_URL;
import static net.chris.demo.guardian.database.Columns.CODE;
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
        "code"
})
public class EditionModel extends BaseModel implements Parcelable {

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
    @Column(name = CODE)
    @JsonProperty("code")
    private String code;

    public final static Parcelable.Creator<EditionModel> CREATOR = new Creator<EditionModel>() {

        public EditionModel createFromParcel(Parcel in) {
            return new EditionModel(in);
        }

        public EditionModel[] newArray(int size) {
            return (new EditionModel[size]);
        }

    };


    public EditionModel() {
        super();
    }

    protected EditionModel(Parcel in) {
        super();
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.webTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.webUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.apiUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.code = ((String) in.readValue((String.class.getClassLoader())));
    }

    /**
     * @param id
     * @param webUrl
     * @param apiUrl
     * @param code
     * @param webTitle
     */
    public EditionModel(@NonNull final String id,
                        @NonNull final String webTitle,
                        @NonNull final String webUrl,
                        @NonNull final String apiUrl,
                        @NonNull final String code) {
        super();
        this.id = id;
        this.webTitle = webTitle;
        this.webUrl = webUrl;
        this.apiUrl = apiUrl;
        this.code = code;
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

    @JsonProperty("code")
    public String getCode() {
        return code;
    }

    @JsonProperty("code")
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(webTitle);
        dest.writeValue(webUrl);
        dest.writeValue(apiUrl);
        dest.writeValue(code);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}