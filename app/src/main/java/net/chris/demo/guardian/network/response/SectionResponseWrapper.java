package net.chris.demo.guardian.network.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "response"
})
public class SectionResponseWrapper {

    @JsonProperty("response")
    private SectionResponse response;

    public SectionResponseWrapper() {
    }

    /**
     * @param response
     */
    public SectionResponseWrapper(SectionResponse response) {
        super();
        this.response = response;
    }

    @JsonCreator
    public static SectionResponseWrapper create(@JsonProperty("response") SectionResponse response) {
        return new SectionResponseWrapper(response);
    }

    @JsonProperty("response")
    public SectionResponse getResponse() {
        return response;
    }

    @JsonProperty("response")
    public void setResponse(SectionResponse response) {
        this.response = response;
    }

}
