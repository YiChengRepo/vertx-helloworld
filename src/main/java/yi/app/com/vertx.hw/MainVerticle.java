package yi.app.com.vertx.hw;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;

public class MainVerticle extends AbstractVerticle {

    public void start() {
        Future<String> hwFuture = Future.future();
        vertx.deployVerticle(HelloWorldVerticle.class.getName(), hwFuture);

        Future<String> routerFuture = Future.future();
        vertx.deployVerticle(RouterVerticle.class.getName(), routerFuture);

        CompositeFuture.all(hwFuture, routerFuture).setHandler(r -> {
            if(r.succeeded()) {
                System.out.println("all verticles deployed successfully !");
            } else {
                System.out.println("deployment went wrong");
                vertx.close();
            }
        });



    }
}
