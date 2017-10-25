package net.chris.demo.guardian.network.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

import net.chris.demo.guardian.model.ContentModel

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
        "status",
        "userTier",
        "total",
        "startIndex",
        "pageSize",
        "currentPage",
        "pages",
        "orderBy",
        "results"
)
class ContentResponse() {

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
    @JsonProperty("startIndex")
    @get:JsonProperty("startIndex")
    @set:JsonProperty("startIndex")
    var startIndex: Long = 0
    @JsonProperty("pageSize")
    @get:JsonProperty("pageSize")
    @set:JsonProperty("pageSize")
    var pageSize: Long = 0
    @JsonProperty("currentPage")
    @get:JsonProperty("currentPage")
    @set:JsonProperty("currentPage")
    var currentPage: Long = 0
    @JsonProperty("pages")
    @get:JsonProperty("pages")
    @set:JsonProperty("pages")
    var pages: Long = 0
    @JsonProperty("orderBy")
    @get:JsonProperty("orderBy")
    @set:JsonProperty("orderBy")
    var orderBy: String? = null
    @JsonProperty("results")
    @get:JsonProperty("results")
    @set:JsonProperty("results")
    var results: List<ContentModel>? = null

    /**
     * @param total
     * @param startIndex
     * @param results
     * @param orderBy
     * @param status
     * @param pages
     * @param pageSize
     * @param currentPage
     * @param userTier
     */
    constructor(status: String,
                userTier: String,
                total: Long,
                startIndex: Long,
                pageSize: Long,
                currentPage: Long,
                pages: Long,
                orderBy: String,
                results: List<ContentModel>) : this() {
        this.status = status
        this.userTier = userTier
        this.total = total
        this.startIndex = startIndex
        this.pageSize = pageSize
        this.currentPage = currentPage
        this.pages = pages
        this.orderBy = orderBy
        this.results = results
    }

    fun save() {
        if (results != null) {
            for (contentModel in results!!) {
                contentModel.save()
            }
        }
    }

    companion object {
        @JsonCreator
        fun create(@JsonProperty("status") status: String,
                   @JsonProperty("userTier") userTier: String,
                   @JsonProperty("total") total: Long,
                   @JsonProperty("startIndex") startIndex: Long,
                   @JsonProperty("pageSize") pageSize: Long,
                   @JsonProperty("currentPage") currentPage: Long,
                   @JsonProperty("pages") pages: Long,
                   @JsonProperty("orderBy") orderBy: String,
                   @JsonProperty("results") results: List<ContentModel>): ContentResponse {
            return ContentResponse(status, userTier, total, startIndex, pageSize, currentPage, pages, orderBy, results)
        }
    }

}

