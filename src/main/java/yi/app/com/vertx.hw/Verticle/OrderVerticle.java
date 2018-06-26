package yi.app.com.vertx.hw.Verticle;

import io.reactivex.Completable;
import io.vertx.core.Future;
import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.RoutingContext;
import io.vertx.reactivex.ext.web.handler.BodyHandler;
import io.vertx.redis.RedisOptions;
import yi.app.com.vertx.hw.Constants;
import yi.app.com.vertx.hw.DTO.Order;
import yi.app.com.vertx.hw.DTO.OrderService;
import yi.app.com.vertx.hw.Verticle.internal.RestAPIVerticle;
import yi.app.com.vertx.hw.service.RedisOrderService;

import java.util.Objects;

public class OrderVerticle extends RestAPIVerticle {

    private static final Logger logger = LoggerFactory.getLogger(OrderVerticle.class);

    private static final String HOST = "0.0.0.0";
    private static final int PORT = 8087;

    private OrderService service;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());
        router.get(Constants.EMALL_LIST_ALL_ITEMS).handler(this::handleGetAll);
        router.post(Constants.EMALL_CREATE_ITEM).handler(this::handleCreateTodo);

        String host = config().getString("http.address", HOST);
        int port = config().getInteger("http.port", PORT);

        initService().andThen(createHttpServer(router, host, port))
            .subscribe(startFuture::complete, startFuture::fail);
    }

    private void handleCreateTodo(RoutingContext context) {
        try {
            JsonObject rawEntity = context.getBodyAsJson();
            if (!Objects.isNull(rawEntity)) {
                final Order order = wrapObject(new Order(rawEntity), context);
                // Call async service then send response back to client.
                sendResponse(context, service.insert(order), Json::encodePrettily, this::created);
                return;
            }
            badRequest(context);
        } catch (DecodeException ex) {
            badRequest(context, ex);
        }
    }

    private void handleGetAll(RoutingContext context) {
        sendResponse(context, service.getAll(), Json::encodePrettily);
    }

    private Completable initService() {
        RedisOptions config = new RedisOptions()
            .setHost(config().getString("redis.host", "127.0.0.1"))
            .setPort(config().getInteger("redis.port", 6379));
        service = new RedisOrderService(vertx, config);

        return service.initData();
    }

    /**
     * Wrap the todo entity with appropriate id and URL.
     *
     * @param order    a order entity
     * @param context RoutingContext
     * @return the wrapped order entity
     */
    private Order wrapObject(Order order, RoutingContext context) {
        int id = order.getId();
        if (id > Order.getIncId()) {
            Order.setIncIdWith(id);
        } else if (id == 0)
            order.setIncId();
        order.setUrl(context.request().absoluteURI() + "/" + order.getId());
        return order;
    }
}
