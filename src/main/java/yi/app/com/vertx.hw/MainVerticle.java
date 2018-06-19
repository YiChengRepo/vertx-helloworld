package yi.app.com.vertx.hw;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.CompositeFuture;
import io.vertx.core.Future;

public class MainVerticle extends AbstractVerticle {

    public void start() {
        Future<String> hwFuture = Future.future();
        vertx.deployVerticle(HelloWorldVerticle.class.getName(), hwFuture);

        Future<String> routerFuture = Future.future();
        vertx.deployVerticle(RouterVerticleSimple.class.getName(), routerFuture);

        Future<String> routerNextFuture = Future.future();
        vertx.deployVerticle(RouterNextVertecle.class.getName(), routerNextFuture);

        Future<String> routerGetParamFuture = Future.future();
        vertx.deployVerticle(GetParamsVertecle.class.getName(), routerGetParamFuture);

        CompositeFuture.all(hwFuture, routerFuture, routerNextFuture, routerGetParamFuture).setHandler(r -> {
            if(r.succeeded()) {
                System.out.println("all verticles deployed successfully !");
            } else {
                System.out.println("deployment went wrong");
                vertx.close();
            }
        });



    }
}
