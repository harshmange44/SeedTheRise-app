package com.hrsh.seedtherise;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MLRetrofitClient {

    private static MLRetrofitClient instance = null;
    private Api myApi;

    private MLRetrofitClient() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Api.BASE_ML_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        myApi = retrofit.create(Api.class);
    }

    public static synchronized MLRetrofitClient getInstance() {
        if (instance == null) {
            instance = new MLRetrofitClient();
        }
        return instance;
    }

    public Api getMyApi() {
        return myApi;
    }
}

