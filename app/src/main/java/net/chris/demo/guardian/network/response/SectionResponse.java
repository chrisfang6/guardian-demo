package net.chris.demo.guardian.network.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import net.chris.demo.guardian.model.SectionModel;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "userTier",
        "total",
        "results"
})
public class SectionResponse implements Parcelable {

    @JsonProperty("status")
    private String status;
    @JsonProperty("userTier")
    private String userTier;
    @JsonProperty("total")
    private long total;
    @JsonProperty("results")
    private List<SectionModel> results;

    public final static Parcelable.Creator<SectionResponse> CREATOR = new Creator<SectionResponse>() {

        public SectionResponse createFromParcel(Parcel in) {
            return new SectionResponse(in);
        }

        public SectionResponse[] newArray(int size) {
            return (new SectionResponse[size]);
        }

    };

    public SectionResponse() {
        super();
    }

    protected SectionResponse(Parcel in) {
        super();
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.userTier = ((String) in.readValue((String.class.getClassLoader())));
        this.total = ((long) in.readValue((long.class.getClassLoader())));
        in.readList(this.results, (SectionModel.class.getClassLoader()));
    }

    /**
     * @param total
     * @param results
     * @param status
     * @param userTier
     */
    public SectionResponse(String status, String userTier, long total, List<SectionModel> results) {
        super();
        this.status = status;
        this.userTier = userTier;
        this.total = total;
        this.results = results;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("userTier")
    public String getUserTier() {
        return userTier;
    }

    @JsonProperty("userTier")
    public void setUserTier(String userTier) {
        this.userTier = userTier;
    }

    @JsonProperty("total")
    public long getTotal() {
        return total;
    }

    @JsonProperty("total")
    public void setTotal(long total) {
        this.total = total;
    }

    @JsonProperty("results")
    public List<SectionModel> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<SectionModel> results) {
        this.results = results;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(userTier);
        dest.writeValue(total);
        dest.writeList(results);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void save() {
        if (results != null) {
            for (SectionModel sectionModel : results) {
                sectionModel.save();
            }
        }
    }
}