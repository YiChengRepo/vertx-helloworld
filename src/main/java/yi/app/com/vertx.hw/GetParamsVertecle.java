package yi.app.com.vertx.hw;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class GetParamsVertecle extends AbstractVerticle {

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.route("/testpath/:producttype/:productid/").handler(routingContext -> {

            System.out.println("route get params");
            HttpServerRequest httpServerRequest = routingContext.request();
            String prodcutType = httpServerRequest.getParam("producttype");
            String productId = httpServerRequest.getParam("productid");

            HttpServerResponse response = routingContext.response();
            response.putHeader("content-type", "text/plain");
            response.end(prodcutType + ": " + productId + "\n");
            System.out.println("router verticle request");
        });

        server.requestHandler(router::accept).listen(8083);
        System.out.println("HTTP server started on port 8083");
    }


}
