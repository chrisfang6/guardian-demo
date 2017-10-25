package net.chris.demo.guardian.model

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.ConflictAction
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import net.chris.demo.guardian.database.Columns.Companion.API_URL
import net.chris.demo.guardian.database.Columns.Companion.CODE
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
        CODE
)
class EditionModel() : BaseModel(), Parcelable {

    @PrimaryKey
    @Column(name = ID)
    var id: String? = null
    @Column(name = WEB_TITLE)
    var webTitle: String? = null
    @Column(name = WEB_URL)
    var webUrl: String? = null
    @Column(name = API_URL)
    var apiUrl: String? = null
    @Column(name = CODE)
    var code: String? = null

    constructor(id: String,
                webTitle: String,
                webUrl: String,
                apiUrl: String,
                code: String) : this() {
        this.id = id
        this.webTitle = webTitle
        this.webUrl = webUrl
        this.apiUrl = apiUrl
        this.code = code
    }

    constructor(`in`: Parcel) : this() {
        this.id = `in`.readValue(String::class.java.classLoader) as String
        this.webTitle = `in`.readValue(String::class.java.classLoader) as String
        this.webUrl = `in`.readValue(String::class.java.classLoader) as String
        this.apiUrl = `in`.readValue(String::class.java.classLoader) as String
        this.code = `in`.readValue(String::class.java.classLoader) as String
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeValue(webTitle)
        dest.writeValue(webUrl)
        dest.writeValue(apiUrl)
        dest.writeValue(code)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        val CREATOR: Parcelable.Creator<EditionModel> = object : Parcelable.Creator<EditionModel> {

            override fun createFromParcel(`in`: Parcel): EditionModel {
                return EditionModel(`in`)
            }

            override fun newArray(size: Int): Array<EditionModel?> {
                return arrayOfNulls(size)
            }

        }

        @JsonCreator
        fun create(@JsonProperty(ID) id: String,
                   @JsonProperty(WEB_TITLE) webTitle: String,
                   @JsonProperty(WEB_URL) webUrl: String,
                   @JsonProperty(API_URL) apiUrl: String,
                   @JsonProperty(CODE) code: String): EditionModel {
            return EditionModel(id, webTitle, webUrl, apiUrl, code)
        }
    }


}