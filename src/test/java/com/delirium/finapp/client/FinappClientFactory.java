package com.delirium.finapp.client;

import retrofit.JacksonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by style on 08/12/2015.
 */
public class FinappClientFactory {

    public static FinappService createClient(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(StringConverterFactory.create())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

        FinappService service = retrofit.create(FinappService.class);
        return service;
    }

    public static FinappService createClient() {
        return createClient("http://localhost:9999/finapp");
    }
}
