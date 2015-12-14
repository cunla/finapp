package com.delirium.finapp.client;

import com.squareup.okhttp.OkHttpClient;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by style on 08/12/2015.
 */
public class FinappClientFactory {


    public static FinappService createService(String baseUrl) {
        RestAdapter.Builder builder = new RestAdapter.Builder()
            .setEndpoint(baseUrl)
            .setClient(new OkClient(new OkHttpClient()));

        RestAdapter adapter = builder.build();
        return adapter.create(FinappService.class);
    }

    public static FinappService createService() {
        return createService("http://localhost:9999/finapp/");
    }
}
