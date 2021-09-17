package com.imran.latticeassignment.network;


import com.imran.latticeassignment.model.PincodeList;
import com.imran.latticeassignment.model.WeatherList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

@GET("/pincode/{PINCODE}/")
    Call<List<PincodeList>>
    getpincode(@Path("PINCODE") int value);

@GET("current.json")
  Call<WeatherList> getweather(
          @Query("key") String value,
          @Query("q") String city
);
}
