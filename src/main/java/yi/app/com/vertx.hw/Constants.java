package yi.app.com.vertx.hw;

public final class Constants {

    private Constants() {}

    public static final String API_GW_GET_ITEM = "/items/:itemId";
    public static final String ITEM = "itemId";

    public static final String EMALL_GET_ITEM = "/items/:itemId";
    public static final String EMALL_LIST_ALL_ITEMS = "/items";
    public static final String EMALL_CREATE_ITEM = "/items";
    public static final String EMALL_UPDATE_ITEM = "/items/:itemId";
    public static final String EMALL_DELETE_ITEM = "/itemss/:itemId";

    public static final String REDIS_EMALL_KEY = "VERT_EMALL";


    public static final String TOPIC_ORDER_LAPTOP = "yi.app.com.topic.order.laptop";

}
