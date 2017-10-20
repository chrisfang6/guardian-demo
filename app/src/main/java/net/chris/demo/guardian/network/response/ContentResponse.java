package net.chris.demo.guardian.network.response;

import android.support.annotation.NonNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import net.chris.demo.guardian.model.ContentModel;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "userTier",
        "total",
        "startIndex",
        "pageSize",
        "currentPage",
        "pages",
        "orderBy",
        "results"
})
public class ContentResponse {

    @JsonProperty("status")
    private String status;
    @JsonProperty("userTier")
    private String userTier;
    @JsonProperty("total")
    private long total;
    @JsonProperty("startIndex")
    private long startIndex;
    @JsonProperty("pageSize")
    private long pageSize;
    @JsonProperty("currentPage")
    private long currentPage;
    @JsonProperty("pages")
    private long pages;
    @JsonProperty("orderBy")
    private String orderBy;
    @JsonProperty("results")
    private List<ContentModel> results;

    public ContentResponse() {
        super();
    }

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
    public ContentResponse(@NonNull final String status,
                           @NonNull final String userTier,
                           @NonNull final long total,
                           @NonNull final long startIndex,
                           @NonNull final long pageSize,
                           @NonNull final long currentPage,
                           @NonNull final long pages,
                           @NonNull final String orderBy,
                           @NonNull final List<ContentModel> results) {
        super();
        this.status = status;
        this.userTier = userTier;
        this.total = total;
        this.startIndex = startIndex;
        this.pageSize = pageSize;
        this.currentPage = currentPage;
        this.pages = pages;
        this.orderBy = orderBy;
        this.results = results;
    }

    @JsonCreator
    public static ContentResponse create(@JsonProperty("status") final String status,
                                         @JsonProperty("userTier") final String userTier,
                                         @JsonProperty("total") final long total,
                                         @JsonProperty("startIndex") final long startIndex,
                                         @JsonProperty("pageSize") final long pageSize,
                                         @JsonProperty("currentPage") final long currentPage,
                                         @JsonProperty("pages") final long pages,
                                         @JsonProperty("orderBy") final String orderBy,
                                         @JsonProperty("results") final List<ContentModel> results) {
        return new ContentResponse(status, userTier, total, startIndex, pageSize, currentPage, pages, orderBy, results);
    }

    public void save() {
        if (results != null) {
            for (ContentModel contentModel : results) {
                contentModel.save();
            }
        }
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

    @JsonProperty("startIndex")
    public long getStartIndex() {
        return startIndex;
    }

    @JsonProperty("startIndex")
    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    @JsonProperty("pageSize")
    public long getPageSize() {
        return pageSize;
    }

    @JsonProperty("pageSize")
    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    @JsonProperty("currentPage")
    public long getCurrentPage() {
        return currentPage;
    }

    @JsonProperty("currentPage")
    public void setCurrentPage(long currentPage) {
        this.currentPage = currentPage;
    }

    @JsonProperty("pages")
    public long getPages() {
        return pages;
    }

    @JsonProperty("pages")
    public void setPages(long pages) {
        this.pages = pages;
    }

    @JsonProperty("orderBy")
    public String getOrderBy() {
        return orderBy;
    }

    @JsonProperty("orderBy")
    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    @JsonProperty("results")
    public List<ContentModel> getResults() {
        return results;
    }

    @JsonProperty("results")
    public void setResults(List<ContentModel> results) {
        this.results = results;
    }

}

