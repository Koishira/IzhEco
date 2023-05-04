package com.example.izheco.update;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AppUpdateInterface {
    @GET("version.txt")
    Call<AppFileData> getLatestAppInfo(@Query("extraKey") String ExtraKey,
                                       @Query("extraValue") String ExtraValue);
}
