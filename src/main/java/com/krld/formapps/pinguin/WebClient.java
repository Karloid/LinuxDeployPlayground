package com.krld.formapps.pinguin;

import com.google.gson.Gson;
import com.krld.formapps.functions.Action2;
import com.krld.formapps.functions.Action3;
import okhttp3.*;

import java.io.IOException;
import java.util.HashMap;

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

    public void sendPing(Action3<Call, Response, Exception> completeCallback, String hostname, String sessionUUID, int index) {
        try {
            HashMap<String, Object> params = new HashMap<>();
            params.put("sessionUUID", sessionUUID);
            params.put("hostname", hostname);
            params.put("index", index);

            post("http://" + hostname + ":9090/ping", new Gson().toJson(params), completeCallback);
        } catch (Exception e) {
            e.printStackTrace();
            if (completeCallback != null) {
                completeCallback.call(null, null, e);
            }
        }
    }

    private void post(String url, String json, Action3<Call, Response, Exception> completeCallback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (completeCallback != null) {
                    completeCallback.call(call, null, e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (completeCallback != null) {
                    completeCallback.call(call, response, null);
                }
            }
        });
    }
}
