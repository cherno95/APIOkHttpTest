package wrapper;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static okhttp3.logging.HttpLoggingInterceptor.Level;

public class TestWrapperOkHttpClient extends OkHttpClient {

    @NotNull
    @Override
    public Call newCall(@NotNull Request user_request) {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(message -> {
            Logger logger = LoggerFactory.getLogger("OkHttpClient");
            if (message.startsWith("{") || message.startsWith("[")) {
                try {
                    String prettyPrintJson = new GsonBuilder().setPrettyPrinting().create().toJson(new JsonParser().parse(message));
                    logger.info("\n" + prettyPrintJson);
                } catch (JsonSyntaxException e) {
                    logger.info(message);
                }
            } else {
                logger.info(message);
            }
        });
        logging.setLevel(Level.BASIC)
                .setLevel(Level.HEADERS)
                .setLevel(Level.BODY);

        return newBuilder()
            //устанавливает время ожидания соединения в 10 секунд.
            .connectTimeout(10, TimeUnit.SECONDS)

            //устанавливает время ожидания чтения ответа сервера в 10 секунд.
            .readTimeout(10, TimeUnit.SECONDS)

            //устанавливает время ожидания записи данных на сервер в 10 секунд.
            .writeTimeout(10, TimeUnit.SECONDS)

            //включает автоматическое перенаправление на другие страницы в случае HTTP-кодов 3xx
            .followRedirects(true)

            //включает автоматическое переподключение в случае неудачного соединения
            .retryOnConnectionFailure(true)

             //Логи
            .addInterceptor(logging)

            //создает экземпляр OkHttpClient с заданными параметрами.
            .build()

            .newCall(user_request);
    }
}