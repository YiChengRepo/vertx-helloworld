package yi.app.com.vertx.hw.Verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.client.WebClient;
import yi.app.com.vertx.hw.Constants;

public class HttpClientSimpleVerticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(req -> {
            WebClient webClient = WebClient.create(vertx);
            webClient.get(Constants.MOCK_JSON_SERVER_PORT,
                Constants.MOCK_JSON_SERVER_HOST,
                Constants.MOCK_JSON_SERVER_URL)
                .timeout(10000)
                .send(ar -> {
                    if(ar.succeeded()) {
                        String response = ar.result().body().toString();
                        System.out.println(response);
                        req.response()
                            .putHeader("content-type", "text/plain")
                            .end(response);
                    } else {
                        req.response()
                            .setStatusCode(500)
                            .putHeader("content-type", "text/plain")
                            .end(ar.cause().toString());
                        System.out.println(ar.cause());
                    }
                });
        }).listen(8085);
        System.out.println("HTTP server started on port 8085");


    }
}
