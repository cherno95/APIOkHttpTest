package request;

import com.google.gson.Gson;
import model.post.CreateRq;
import model.put.UpdateRq;
import okhttp3.*;

import java.io.IOException;


public class TestRqOkHttp {
    private final OkHttpClient client;
    private final MediaType JSON = MediaType.parse("application/json");

    public TestRqOkHttp(OkHttpClient client) {
        this.client = client;
    }

    public Response postCreate(String host, CreateRq postIn) throws IOException {
        String postStr = new Gson().toJson(postIn);

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(host)
                .addPathSegment("api")
                .addPathSegment("users")
                .build();
        RequestBody body = RequestBody.create(postStr, JSON);

        Request request = new Request.Builder()
                .url(httpUrl)
                .post(body)
                .build();
        return client.newCall(request).execute();
    }

    public Response putCreate(String host, UpdateRq putIn, String id) throws IOException {
        String putStr = new Gson().toJson(putIn);

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(host)
                .addPathSegment("api")
                .addPathSegment("users")
                .addPathSegment(id)
                .build();
        RequestBody body = RequestBody.create(putStr, JSON);

        Request request = new Request.Builder()
                .url(httpUrl)
                .put(body)
                .build();
        return client.newCall(request).execute();
    }


    public Response getListUsers(String host, String page) throws IOException {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(host)
                .addPathSegment("api")
                .addPathSegment("users")
                .addQueryParameter("page", page)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .build();
        return client.newCall(request).execute();
    }

    public Response getSingleUser(String host, String id) throws IOException {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(host)
                .addPathSegment("api")
                .addPathSegment("users")
                .addPathSegment(id)
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .get()
                .build();
        return client.newCall(request).execute();
    }

    public Response deleteCreate(String host, String id) throws IOException {
        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("https")
                .host(host)
                .addPathSegment("api")
                .addPathSegment("users")
                .build();

        Request request = new Request.Builder()
                .url(httpUrl)
                .delete()
                .build();
        return client.newCall(request).execute();
    }
}