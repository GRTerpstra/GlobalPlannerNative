package com.example.globalplanner;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface UserClient {

    @POST("auth/login")
    Call<User> login(@Body Login login);

    @GET("appointment/type")
    Call<ResponseBody> getTypes(@Header("Authorization") String jwt);

    @POST("appointment")
    Call<ResponseBody> postAppointment(@Header("Authorization") String jwt, @Body AppointmentInfo appointmentInfo);
}


