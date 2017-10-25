package net.chris.demo.guardian.network.response

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("response")
class SectionResponseWrapper() {

    @JsonProperty("response")
    @get:JsonProperty("response")
    @set:JsonProperty("response")
    var response: SectionResponse? = null

    constructor(response: SectionResponse) : this() {
        this.response = response
    }

    companion object {
        @JsonCreator
        fun create(@JsonProperty("response") response: SectionResponse): SectionResponseWrapper {
            return SectionResponseWrapper(response)
        }
    }

}
