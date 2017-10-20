package net.chris.demo.guardian.network.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "response"
})
public class ContentResponseWrapper {

    @JsonProperty("response")
    private ContentResponse response;

    public ContentResponseWrapper() {
    }

    /**
     * @param response
     */
    public ContentResponseWrapper(ContentResponse response) {
        super();
        this.response = response;
    }

    @JsonCreator
    public static ContentResponseWrapper create(@JsonProperty("response") ContentResponse response) {
        return new ContentResponseWrapper(response);
    }

    @JsonProperty("response")
    public ContentResponse getResponse() {
        return response;
    }

    @JsonProperty("response")
    public void setResponse(ContentResponse response) {
        this.response = response;
    }

}
