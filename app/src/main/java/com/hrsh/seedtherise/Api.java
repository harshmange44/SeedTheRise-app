package com.hrsh.seedtherise;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface Api {

    String BASE_URL = "https://seed-the-rise.herokuapp.com/api/";

    @GET("instances/")
    Call<List<InstanceNameResult>> getInstanceNameData();

    @GET("instances/{instance_name}")
    Call<List<InstanceResult>> getInstanceData(@Path(value="instance_name") String instance_name);

    @GET("instances/sensordata/{instance_name}")
    Call<SensorDataResult> getSensorData(@Path(value="instance_name") String instance_name);
}