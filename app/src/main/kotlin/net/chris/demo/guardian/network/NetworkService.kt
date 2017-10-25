package net.chris.demo.guardian.network


import net.chris.demo.guardian.network.response.ContentResponseWrapper
import net.chris.demo.guardian.network.response.SectionResponseWrapper

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface NetworkService {

    @GET("search")
    fun fetchContent(@QueryMap options: Map<String, String>): Call<ContentResponseWrapper>

    @GET("sections")
    fun fetchSection(@QueryMap options: Map<String, String>): Call<SectionResponseWrapper>
}
