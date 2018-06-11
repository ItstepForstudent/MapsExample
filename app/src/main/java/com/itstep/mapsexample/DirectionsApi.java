package com.itstep.mapsexample;

import com.itstep.mapsexample.models.DirectionResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DirectionsApi {

    @GET("/maps/api/directions/json")
    Call<DirectionResponse> getRoute(
            @Query("origin") String origin,
            @Query("destination") String destination,
            @Query("key") String apiKey,
            @Query("sensor") boolean sensor,
            @Query("language") String language
    );
}
