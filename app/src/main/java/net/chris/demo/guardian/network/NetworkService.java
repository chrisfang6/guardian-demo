package net.chris.demo.guardian.network;


import net.chris.demo.guardian.network.response.ContentResponseWrapper;
import net.chris.demo.guardian.network.response.SectionResponseWrapper;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface NetworkService {

    @GET("search")
    Call<ContentResponseWrapper> fetchContent(@QueryMap Map<String, String> options);

    @GET("sections")
    Call<SectionResponseWrapper> fetchSection(@QueryMap Map<String, String> options);
}
