package net.chris.demo.guardian.model

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.JsonNode
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.ConflictAction
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import net.chris.demo.guardian.database.Columns.Companion.API_URL
import net.chris.demo.guardian.database.Columns.Companion.EDITIONS
import net.chris.demo.guardian.database.Columns.Companion.ID
import net.chris.demo.guardian.database.Columns.Companion.WEB_TITLE
import net.chris.demo.guardian.database.Columns.Companion.WEB_URL
import net.chris.demo.guardian.database.GuardianDatabase

@Table(database = GuardianDatabase::class, cachingEnabled = true, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
        ID,
        WEB_TITLE,
        WEB_URL,
        API_URL,
        EDITIONS
)
class SectionModel() : BaseModel(), Parcelable {

    @PrimaryKey
    @Column(name = ID)
    var id: String? = null
    @Column(name = WEB_TITLE)
    var webTitle: String? = null
    @Column(name = WEB_URL)
    var webUrl: String? = null
    @Column(name = API_URL)
    var apiUrl: String? = null
    @Column(name = EDITIONS)
    var editions: JsonNode? = null

//    @get:OneToMany(methods = arrayOf(OneToMany.Method.ALL))
//    var editions by oneToMany { select.from(EditionModel::class.java).where(EditionModel_Table.id.eq(id)) }

    constructor(id: String,
                webTitle: String,
                webUrl: String,
                apiUrl: String,
                editions: JsonNode) : this() {
        this.id = id
        this.webTitle = webTitle
        this.webUrl = webUrl
        this.apiUrl = apiUrl
        this.editions = editions
    }

    constructor (`in`: Parcel) : this() {
        this.id = `in`.readValue(String::class.java.classLoader) as String
        this.webTitle = `in`.readValue(String::class.java.classLoader) as String
        this.webUrl = `in`.readValue(String::class.java.classLoader) as String
        this.apiUrl = `in`.readValue(String::class.java.classLoader) as String
        this.editions = `in`.readValue(JsonNode::class.java.classLoader) as JsonNode
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeValue(webTitle)
        dest.writeValue(webUrl)
        dest.writeValue(apiUrl)
        dest.writeValue(editions)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        val CREATOR: Parcelable.Creator<SectionModel> = object : Parcelable.Creator<SectionModel> {

            override fun createFromParcel(`in`: Parcel): SectionModel {
                return SectionModel(`in`)
            }

            override fun newArray(size: Int): Array<SectionModel?> {
                return arrayOfNulls(size)
            }

        }

        @JsonCreator
        fun create(@JsonProperty(ID) id: String,
                   @JsonProperty(WEB_TITLE) webTitle: String,
                   @JsonProperty(WEB_URL) webUrl: String,
                   @JsonProperty(API_URL) apiUrl: String,
                   @JsonProperty(EDITIONS) editions: JsonNode): SectionModel {
            return SectionModel(id, webTitle, webUrl, apiUrl, editions)
        }
    }

}