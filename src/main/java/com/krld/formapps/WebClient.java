package com.krld.formapps;

import com.krld.support.functions.Action2;
import okhttp3.*;

import java.io.IOException;

public class WebClient {
    private static WebClient instance;

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    OkHttpClient client = new OkHttpClient();

    private WebClient() {
    }

    public synchronized static WebClient getInstance() {
        if (instance == null) {
            instance = new WebClient();
        }
        return instance;
    }

    public void sendPing(Action2<Call, Response> success, Action2<Call, IOException> failure, String hostname) {
        post("http://" + hostname + ":9090/ping", "lol", success, failure);
    }

    private void post(String url, String json, Action2<Call, Response> successCallback, Action2<Call, IOException> failCallback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (failCallback != null) {
                    failCallback.call(call, e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (successCallback != null) {
                    successCallback.call(call, response);
                }
            }
        });
    }
}
