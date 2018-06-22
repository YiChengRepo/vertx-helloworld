package yi.app.com.vertx.hw.DTO;

import io.reactivex.Completable;
import io.reactivex.Single;

import java.util.List;

/**
 * Reactive service interface of todo backend.
 *
 */

public interface OrderService {
  Completable initData();
  Single<List<Order>> getAll();
  Single<Order> insert(Order order);

}
