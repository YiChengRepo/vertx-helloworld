package yi.app.com.vertx.hw;

import io.vertx.core.AbstractVerticle;

public class HelloWorldVerticle extends AbstractVerticle {

    long couter = 0;

    @Override
    public void start() throws Exception {
        vertx.createHttpServer().requestHandler(req -> {
              ++couter;
              req.response()
                .putHeader("content-type", "text/plain")
                .end("Hello from Vert.x!");
            System.out.println("hw verticle request: " + couter);
            }).listen(8080);
        System.out.println("HTTP server started on port 8080");
    }
}
