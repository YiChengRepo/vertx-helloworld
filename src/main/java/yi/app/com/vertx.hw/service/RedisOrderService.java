package yi.app.com.vertx.hw.service;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.vertx.core.json.Json;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.redis.RedisClient;
import io.vertx.redis.RedisOptions;
import yi.app.com.vertx.hw.Constants;
import yi.app.com.vertx.hw.DTO.Order;
import yi.app.com.vertx.hw.DTO.OrderService;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class RedisOrderService implements OrderService {

  private final Vertx vertx;
  private final RedisOptions config;
  private final RedisClient redis;

  public RedisOrderService(Vertx vertx, RedisOptions config) {
    this.vertx = vertx;
    this.config = config;
    this.redis = RedisClient.create(vertx, config);
  }

  @Override
  public Completable initData() {
    Order sample = new Order(Math.abs(ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE)),
      "order ", false, 1, "order/item");
    return insert(sample).toCompletable();
  }

  @Override
  public Single<List<Order>> getAll() {
    return redis.rxHvals(Constants.REDIS_EMALL_KEY)
      .map(e -> e.stream()
        .map(x -> new Order((String) x))
        .collect(Collectors.toList())
      );
  }

    @Override
    public Single<Order> insert(Order order) {
        final String encoded = Json.encodePrettily(order);
        return redis.rxHset(Constants.REDIS_EMALL_KEY, String.valueOf(order.getId()), encoded)
            .map(e -> order);
    }
}
