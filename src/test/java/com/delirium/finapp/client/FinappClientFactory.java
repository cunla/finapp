package com.delirium.finapp.client;

import com.squareup.okhttp.OkHttpClient;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;

/**
 * Created by style on 08/12/2015.
 */
public class FinappClientFactory {


    public static FinappService createService(String baseUrl) {
        OkHttpClient client = new OkHttpClient();
        CookieManager cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        client.setCookieHandler(cookieManager);

        RestAdapter.Builder builder = new RestAdapter.Builder()
            .setEndpoint(baseUrl)
            .setConverter(new JacksonConverter())
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setClient(new OkClient(client));

        RestAdapter adapter = builder.build();
        return adapter.create(FinappService.class);
    }

    public static FinappService createService() {
        return createService("http://localhost:9999/finapp/");
    }
}
