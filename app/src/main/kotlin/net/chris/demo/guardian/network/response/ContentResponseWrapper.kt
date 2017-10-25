package net.chris.demo.guardian.network.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("response")
class ContentResponseWrapper() {

    @JsonProperty("response")
    @get:JsonProperty("response")
    @set:JsonProperty("response")
    var response: ContentResponse? = null

    constructor(response: ContentResponse) : this() {
        this.response = response
    }

    companion object {
        @JsonCreator
        fun create(@JsonProperty("response") response: ContentResponse): ContentResponseWrapper {
            return ContentResponseWrapper(response)
        }
    }

}
