package system.service;

/**
 * Created by Peter Mösenthin.
 */
public class NotificationService {

    private static final NotificationService instance = new NotificationService();

    public static void register(){
        EventBusService.register(instance);
    }

}
