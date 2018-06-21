package yi.app.com.vertx.hw.handler;


import io.vertx.core.Handler;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import yi.app.com.vertx.hw.Constants;

import java.util.concurrent.ThreadLocalRandom;

public class OrderHandler implements Handler<RoutingContext> {
    private static final long MAX_PRICE = 9999;

    @Override
    public void handle(RoutingContext event) {
        String orderNo = event.request().getParam(Constants.ITEM);
        Order newOrder = new Order(orderNo, ThreadLocalRandom.current().nextLong(MAX_PRICE));

        //todo: need customer codec for sending object
//        event.vertx().eventBus().send(Constants.TOPIC_ORDER_LAPTOP, newOrder, cb -> {
        event.vertx().eventBus().send(Constants.TOPIC_ORDER_LAPTOP, "order: " + orderNo, cb -> {
            if (cb.succeeded()) {
                System.out.println("order client called ok");
                OrderReply or = new OrderReply(newOrder, cb.result().body().toString());
                event.response().end(JsonObject.mapFrom(or).encode());
            } else {
                System.out.println("order client call failed");
                event.response().setStatusCode(500).end("order failed");
            }
        });
    }


    private class Order {
        private String orderNo;
        private long price;

        public String getOrderNo() {
            return orderNo;
        }

        public long getPrice() {
            return price;
        }

        public Order(String orderNo, long price) {
            this.orderNo = orderNo;
            this.price = price;
        }
    }

    private class OrderReply {
        private Order  order;
        private String reply;

        public Order getOrder() {
            return order;
        }

        public String getReply() {
            return reply;
        }


        public OrderReply(Order order, String reply) {
            this.order = order;
            this.reply = reply;
        }
    }

}
