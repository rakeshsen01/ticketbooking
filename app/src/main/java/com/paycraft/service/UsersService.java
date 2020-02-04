package com.paycraft.service;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UsersService {
    String API_VERSION = "api/v1";

    @GET(API_VERSION + "/users")
    Call<ResponseBody> getUsersBody(@Query("page") int page);

    @GET(API_VERSION + "/users")
    Call<ResponseBody> verifyOtp(@Query("otp") String verification);

    @GET(API_VERSION + "/users")
    Call<ResponseBody> getStations(@Query("station") int page);

    @GET(API_VERSION + "/users")
    Call<ResponseBody> generateTicketID(@Query("station") int page);
}
