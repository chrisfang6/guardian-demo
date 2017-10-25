package net.chris.demo.guardian.network.response

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import net.chris.demo.guardian.model.SectionModel

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("status", "userTier", "total", "results")
class SectionResponse() : Parcelable {

    @JsonProperty("status")
    @get:JsonProperty("status")
    @set:JsonProperty("status")
    var status: String? = null
    @JsonProperty("userTier")
    @get:JsonProperty("userTier")
    @set:JsonProperty("userTier")
    var userTier: String? = null
    @JsonProperty("total")
    @get:JsonProperty("total")
    @set:JsonProperty("total")
    var total: Long = 0
    @JsonProperty("results")
    @get:JsonProperty("results")
    @set:JsonProperty("results")
    var results: List<SectionModel>? = null

    constructor(`in`: Parcel) : this() {
        this.status = `in`.readValue(String::class.java.classLoader) as String
        this.userTier = `in`.readValue(String::class.java.classLoader) as String
        this.total = `in`.readValue(Long::class.javaPrimitiveType?.classLoader) as Long
        `in`.readList(this.results, SectionModel::class.java.classLoader)
    }

    /**
     * @param total
     * @param results
     * @param status
     * @param userTier
     */
    constructor(status: String, userTier: String, total: Long, results: List<SectionModel>) : this() {
        this.status = status
        this.userTier = userTier
        this.total = total
        this.results = results
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(status)
        dest.writeValue(userTier)
        dest.writeValue(total)
        dest.writeList(results)
    }

    override fun describeContents(): Int {
        return 0
    }

    fun save() {
        if (results != null) {
            for (sectionModel in results!!) {
                sectionModel.save()
            }
        }
    }

    companion object {

        val CREATOR: Parcelable.Creator<SectionResponse> = object : Parcelable.Creator<SectionResponse> {

            override fun createFromParcel(`in`: Parcel): SectionResponse {
                return SectionResponse(`in`)
            }

            override fun newArray(size: Int): Array<SectionResponse?> {
                return arrayOfNulls(size)
            }

        }

        @JsonCreator
        fun create(@JsonProperty("status") status: String,
                   @JsonProperty("userTier") userTier: String,
                   @JsonProperty("total") total: Long,
                   @JsonProperty("results") results: List<SectionModel>): SectionResponse {
            return SectionResponse(status, userTier, total, results)
        }
    }
}