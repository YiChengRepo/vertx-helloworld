package yi.app.com.vertx.hw;

import io.vertx.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {
    public void start() {
        vertx.deployVerticle(HelloWorldVerticle.class.getName());
    }
}
