package yi.app.com.vertx.hw.Verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Route;
import io.vertx.ext.web.Router;

public class RouterNextVertecle extends AbstractVerticle {
    public static final String KEY = "MY_KEY";

    @Override
    public void start() throws Exception {
        HttpServer server = vertx.createHttpServer();
        Router router = Router.router(vertx);

        Route route1 = router.route("/test/path/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            routingContext.put(KEY, "value 1 from route 1");
            response.setChunked(true);
            response.write("route1\n");
            System.out.println("routeNext -> route 1");
            routingContext.vertx().setTimer(2000, tid -> routingContext.next());
        });

        Route route2 = router.route("/test/path/").handler(routingContext -> {
            System.out.println("routeNext -> route 2");
            String value = routingContext.get(KEY);
            HttpServerResponse response = routingContext.response();
            response.write("value from previous route is: [ " + value + " ], route2\n");
            routingContext.next();
        });

        Route route3 = router.route("/test/path/").handler(routingContext -> {
            System.out.println("routeNext -> route 3");
            HttpServerResponse response = routingContext.response();
            response.write("route3\n");
            routingContext.vertx().setTimer(1000, tid ->  routingContext.next());
        });

        Route route4 = router.route("/test/path/").handler(routingContext -> {
            System.out.println("routeNext -> route 4");
            HttpServerResponse response = routingContext.response();
            response.write("route4 ");
            routingContext.response().end();
        });

        server.requestHandler(router::accept).listen(8082);
       System.out.println("HTTP server started on port 8082");
    }


}
