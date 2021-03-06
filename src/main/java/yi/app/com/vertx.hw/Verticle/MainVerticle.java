package yi.app.com.vertx.hw.Verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.impl.CompositeFutureImpl;
import yi.app.com.vertx.hw.Constants;

public class MainVerticle extends AbstractVerticle {

    public void start() {
        Future<String> hwFuture = Future.future();
        vertx.deployVerticle(HelloWorldVerticle.class.getName(), hwFuture);

        Future<String> routerFuture = Future.future();
        vertx.deployVerticle(RouterSimpleVerticle.class.getName(), routerFuture);

        Future<String> routerNextFuture = Future.future();
        vertx.deployVerticle(RouterNextVertecle.class.getName(), routerNextFuture);

        Future<String> routerGetParamFuture = Future.future();
        vertx.deployVerticle(GetParamsVertecle.class.getName(), routerGetParamFuture);

        Future<String> orderClientSideVerticleFuture = Future.future();
        vertx.deployVerticle(OrderClientSideVerticle.class.getName(), orderClientSideVerticleFuture);

        Future<String> httpClientSimpleVerticleFuture = Future.future();
        vertx.deployVerticle(HttpClientSimpleVerticle.class.getName(), httpClientSimpleVerticleFuture);

        Future<String> httpClientSimpleImprovedVerticleFuture = Future.future();
        vertx.deployVerticle(HttpClientSimpleImprovedVerticle.class.getName(), httpClientSimpleImprovedVerticleFuture);

        Future<String> orderVerticleFuture = Future.future();
        vertx.deployVerticle(OrderVerticle.class.getName(), orderVerticleFuture);


        registerConsumerOrderItem();

       //compositeFuture can only support up to 6 future !,
        //        CompositeFuture.all(
         CompositeFutureImpl.all(
            hwFuture,
            routerFuture,
            routerNextFuture,
            routerGetParamFuture,
            orderClientSideVerticleFuture,
            httpClientSimpleVerticleFuture,
            httpClientSimpleImprovedVerticleFuture,
            orderVerticleFuture
            ).setHandler(r -> {
            if(r.succeeded()) {
                System.out.println("all verticles deployed successfully !");
            } else {
                System.out.println("deployment went wrong");
                vertx.close();
            }
        });



    }

    private void registerConsumerOrderItem() {
        vertx.eventBus().consumer(Constants.TOPIC_ORDER_LAPTOP, msg -> {
            String receivedMsg = msg.body().toString();
            System.out.println("topic [" + Constants.TOPIC_ORDER_LAPTOP + "] consuming message: " + receivedMsg);
            msg.reply(String.format("your order [%s] is processed !", receivedMsg));
        });
    }
}
