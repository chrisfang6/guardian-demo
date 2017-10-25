package net.chris.demo.guardian.model

import android.os.Parcel
import android.os.Parcelable
import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.raizlabs.android.dbflow.annotation.Column
import com.raizlabs.android.dbflow.annotation.ConflictAction
import com.raizlabs.android.dbflow.annotation.PrimaryKey
import com.raizlabs.android.dbflow.annotation.Table
import com.raizlabs.android.dbflow.structure.BaseModel
import net.chris.demo.guardian.database.Columns.Companion.API_URL
import net.chris.demo.guardian.database.Columns.Companion.ID
import net.chris.demo.guardian.database.Columns.Companion.SECTION_ID
import net.chris.demo.guardian.database.Columns.Companion.SECTION_NAME
import net.chris.demo.guardian.database.Columns.Companion.WEB_PUBLICATION_DATE
import net.chris.demo.guardian.database.Columns.Companion.WEB_TITLE
import net.chris.demo.guardian.database.Columns.Companion.WEB_URL
import net.chris.demo.guardian.database.GuardianDatabase
import net.chris.demo.guardian.system.DateTimeStruct

@Table(database = GuardianDatabase::class, cachingEnabled = true, insertConflict = ConflictAction.REPLACE, updateConflict = ConflictAction.REPLACE)
@JsonPropertyOrder(
        ID,
        SECTION_ID,
        SECTION_NAME,
        WEB_PUBLICATION_DATE,
        WEB_TITLE,
        WEB_URL,
        API_URL
)
class ContentModel() : BaseModel(), Parcelable {

    @PrimaryKey
    @Column(name = ID)
    var id: String? = null
    @Column(name = SECTION_ID)
    var sectionId: String? = null
    @Column(name = SECTION_NAME)
    var sectionName: String? = null
    @Column(name = WEB_PUBLICATION_DATE)
    var webPublicationDate: DateTimeStruct? = null
    @Column(name = WEB_TITLE)
    var webTitle: String? = null
    @Column(name = WEB_URL)
    var webUrl: String? = null
    @Column(name = API_URL)
    var apiUrl: String? = null

    constructor(id: String,
                sectionId: String,
                sectionName: String,
                webPublicationDate: DateTimeStruct,
                webTitle: String,
                webUrl: String,
                apiUrl: String) : this() {
        this.id = id
        this.sectionId = sectionId
        this.sectionName = sectionName
        this.webPublicationDate = webPublicationDate
        this.webTitle = webTitle
        this.webUrl = webUrl
        this.apiUrl = apiUrl
    }

    constructor(`in`: Parcel) : this() {
        this.id = `in`.readValue(String::class.java.classLoader) as String
        this.sectionId = `in`.readValue(String::class.java.classLoader) as String
        this.sectionName = `in`.readValue(String::class.java.classLoader) as String
        this.webPublicationDate = `in`.readValue(DateTimeStruct::class.java.classLoader) as DateTimeStruct
        this.webTitle = `in`.readValue(String::class.java.classLoader) as String
        this.webUrl = `in`.readValue(String::class.java.classLoader) as String
        this.apiUrl = `in`.readValue(String::class.java.classLoader) as String
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeValue(id)
        dest.writeValue(sectionId)
        dest.writeValue(sectionName)
        dest.writeValue(webPublicationDate)
        dest.writeValue(webTitle)
        dest.writeValue(webUrl)
        dest.writeValue(apiUrl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object {

        val CREATOR: Parcelable.Creator<ContentModel> = object : Parcelable.Creator<ContentModel> {

            override fun createFromParcel(`in`: Parcel): ContentModel {
                return ContentModel(`in`)
            }

            override fun newArray(size: Int): Array<ContentModel?> {
                return arrayOfNulls(size)
            }

        }

        @JsonCreator
        fun create(@JsonProperty(ID) id: String,
                   @JsonProperty(SECTION_ID) sectionId: String,
                   @JsonProperty(SECTION_NAME) sectionName: String,
                   @JsonProperty(WEB_PUBLICATION_DATE) webPublicationDate: DateTimeStruct,
                   @JsonProperty(WEB_TITLE) webTitle: String,
                   @JsonProperty(WEB_URL) webUrl: String,
                   @JsonProperty(API_URL) apiUrl: String): ContentModel {
            return ContentModel(id, sectionId, sectionName, webPublicationDate, webTitle, webUrl, apiUrl)
        }
    }


}