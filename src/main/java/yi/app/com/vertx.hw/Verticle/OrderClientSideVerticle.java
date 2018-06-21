package yi.app.com.vertx.hw.Verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import yi.app.com.vertx.hw.Constants;
import yi.app.com.vertx.hw.handler.OrderHandler;

public class OrderClientSideVerticle extends AbstractVerticle {
    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        router.route(Constants.API_GW_GET_ITEM).handler(new OrderHandler());
        server.requestHandler(router::accept).listen(8084);
        System.out.println("HTTP server started on port 8084");}
}
