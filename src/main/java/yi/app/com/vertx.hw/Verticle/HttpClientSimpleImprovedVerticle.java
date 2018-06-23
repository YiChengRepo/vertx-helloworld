package yi.app.com.vertx.hw.Verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.client.WebClient;
import yi.app.com.vertx.hw.Constants;


// @YiChengRepo
//  This is a improved version of the previous example of HttpClientSimpleVerticle
// where it looks like the so call call back hell
//  in this example we use vertex future to avoid that
public class HttpClientSimpleImprovedVerticle extends AbstractVerticle {

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Future<Void> stringFuture = createHttpServer();
        stringFuture.setHandler(startFuture);
    }

    private Future<Void> createHttpServer() {
        Future<Void> future = Future.future();
        HttpServer httpServer = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.get("/").handler(this::handleHttp);
        httpServer
            .requestHandler(router::accept)
            .listen(8086, ar -> {
                if(ar.succeeded()) {
                    System.out.println("HTTP server started on port 8086");
                    future.complete();
                } else {
                    future.fail(ar.cause());
                    System.out.println(ar.cause());
                }
        });
        return future;
    }

    private void handleHttp(RoutingContext context) {
        Future<String> stringFuture = makeHttpCall();
        stringFuture.setHandler(ar -> {
            if(ar.succeeded()) {
                context.response()
                    .putHeader("content-type", "text/plain")
                    .end(stringFuture.result());
            } else {
                context.response()
                    .setStatusCode(500)
                    .end(stringFuture.cause().toString());
            }
        });
    }

    private Future<String> makeHttpCall() {
        Future<String> future = Future.future();
        WebClient webClient = WebClient.create(vertx);
        webClient.get(Constants.MOCK_JSON_SERVER_PORT,
            Constants.MOCK_JSON_SERVER_HOST,
            Constants.MOCK_JSON_SERVER_URL)
            .timeout(10000)
            .send(ar -> {
                if(ar.succeeded()) {
                    String response = ar.result().body().toString();
                    System.out.println(response);
                    future.complete(response);
                } else {
                    future.fail(ar.cause());
                    System.out.println(ar.cause());
                }
            });
        return future;
    }
}
