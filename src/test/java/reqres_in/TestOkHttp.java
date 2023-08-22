package reqres_in;

import com.google.gson.Gson;
import model.get.ListUsersRs;
import model.get.SingleUserRs;
import model.post.CreateRq;
import model.post.CreateRs;
import model.put.UpdateRq;
import model.put.UpdateRs;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import request.TestRqOkHttp;
import wrapper.TestWrapperOkHttpClient;

import java.io.IOException;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static request.PropertyLoader.loadProperty;

public class TestOkHttp {

    private final OkHttpClient client = new TestWrapperOkHttpClient();
    private static final Gson gson = new Gson();

    private static final String host = loadProperty("host");


    @Test
    @Tag("apiTest")
    @DisplayName("Позитивный кейс. Отправить валидный 'POST' запрос для создания пользователя. Ответ 201")
    public void postTestOne() throws IOException {
        Response response = new TestRqOkHttp(client).postCreate(host, new CreateRq("morpheus", "leader"));
        String responseBody = Objects.requireNonNull(response.body()).string();

        assertThat(response.code())
                .withFailMessage("HTTP response code is not 200, fail message: "
                        + responseBody)
                .isEqualTo(201);

        CreateRs postRs = gson.fromJson(responseBody, CreateRs.class);
        assertThat(postRs.getName()).isEqualTo("morpheus");
        assertThat(postRs.getJob()).isEqualTo("leader");
    }

    @Test
    @Tag("apiTest")
    @DisplayName("Позитивный кейс. Отправить валидный 'GET' запрос для получения списка пользователей. Ответ 200")
    public void getTestOne() throws IOException {
        Response response = new TestRqOkHttp(client).getListUsers(host,"2");
        String responseBody = Objects.requireNonNull(response.body()).string();

        assertThat(response.code())
                .withFailMessage("HTTP response code is not 200, fail message: "
                        + responseBody)
                .isEqualTo(200);

        ListUsersRs getRs = gson.fromJson(responseBody, ListUsersRs.class);
        assertThat(getRs.getPage()).isEqualTo(2);
        assertThat(getRs.getPer_page()).isEqualTo(6);
        assertThat(getRs.getTotal()).isEqualTo(12);
        assertThat(getRs.getTotal_pages()).isEqualTo(2);
        assertThat(getRs.getData().get(0).getId()).isEqualTo(7);
        assertThat(getRs.getData().get(0).getEmail()).isEqualTo("michael.lawson@reqres.in");
        assertThat(getRs.getData().get(0).getFirst_name()).isEqualTo("Michael");
        assertThat(getRs.getData().get(0).getLast_name()).isEqualTo("Lawson");
        assertThat(getRs.getData().get(0).getAvatar()).isEqualTo("https://reqres.in/img/faces/7-image.jpg");
    }

    @Test
    @Tag("apiTest")
    @DisplayName("Позитивный кейс. Отправить валидный 'GET' запрос для получения информации о пользователе. Ответ 200")
    public void getTestTwo() throws IOException {
        Response response = new TestRqOkHttp(client).getSingleUser(host, "2");
        String responseBody = Objects.requireNonNull(response.body()).string();

        assertThat(response.code())
                .withFailMessage("HTTP response code is not 200, fail message: "
                        + responseBody)
                .isEqualTo(200);

        SingleUserRs getRs = gson.fromJson(responseBody, SingleUserRs.class);
        assertThat(getRs.getData().getId()).isEqualTo(2);
        assertThat(getRs.getData().getEmail()).isEqualTo("janet.weaver@reqres.in");
        assertThat(getRs.getData().getFirst_name()).isEqualTo("Janet");
        assertThat(getRs.getData().getLast_name()).isEqualTo("Weaver");
        assertThat(getRs.getData().getAvatar()).isEqualTo("https://reqres.in/img/faces/2-image.jpg");
        assertThat(getRs.getSupport().getUrl()).isEqualTo("https://reqres.in/#support-heading");
        assertThat(getRs.getSupport().getText()).isEqualTo("To keep ReqRes free, contributions towards server costs are appreciated!");
    }

    @Test
    @Tag("apiTest")
    @DisplayName("E2E сценарий. Позитивный кейс. " +
            "Отправить валидный 'POST' запрос для создания пользователя. Ответ 201." +
            "Отправить валидный 'PUT' запрос на имземенение данных. Ответ 200." +
            "Отправить валидный 'DELETE' запрос для удаления пользователя. Ответ 204.")
    public void testThree() throws IOException {
        //POST
        Response responsePost = new TestRqOkHttp(client).postCreate(host, new CreateRq("Ivan", "QA"));
        String responseBodyPost = Objects.requireNonNull(responsePost.body()).string();

        assertThat(responsePost.code())
                .withFailMessage("HTTP response code is not 201, fail message: "
                        + responseBodyPost)
                .isEqualTo(201);

        CreateRs postRs = gson.fromJson(responseBodyPost, CreateRs.class);
        assertThat(postRs.getName()).isEqualTo("Ivan");
        assertThat(postRs.getJob()).isEqualTo("QA");


        //PUT
        Response responsePut = new TestRqOkHttp(client).putCreate(host, new UpdateRq("Vladislav", "DEV"), "2");
        String responseBodyPut = Objects.requireNonNull(responsePut.body()).string();

        assertThat(responsePut.code())
                .withFailMessage("HTTP response code is not 200, fail message: "
                        + responseBodyPut)
                .isEqualTo(200);

        UpdateRs putRs = gson.fromJson(responseBodyPut, UpdateRs.class);
        assertThat(putRs.getName()).isEqualTo("Vladislav");
        assertThat(putRs.getJob()).isEqualTo("DEV");


        //DELETE
        Response responseDelete = new TestRqOkHttp(client).deleteCreate(host, "2");
        String responseBodyDelete = Objects.requireNonNull(responseDelete.body()).string();

        assertThat(responseDelete.code())
                .withFailMessage("HTTP response code is not 204, fail message: "
                        + responseBodyDelete)
                .isEqualTo(204);
    }


    @Test
    @Tag("apiTest")
    @DisplayName("Негативный кейс. Отправить невалидный 'GET' запрос для получения информации о пользователе. Ответ 404")
    public void getTestFour() throws IOException {
        Response response = new TestRqOkHttp(client).getSingleUser(host,"23");
        String responseBody = Objects.requireNonNull(response.body()).string();

        assertThat(response.code())
                .withFailMessage("HTTP response code is not 200, fail message: "
                        + responseBody)
                .isEqualTo(404);
    }
}