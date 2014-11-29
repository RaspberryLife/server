package event;


import com.google.common.eventbus.EventBus;

import java.util.Objects;

public class EventBusService {

    private static EventBus eventBus;

    public static void init(){
        eventBus = new EventBus();
    }

    public static void post(Object o){
        eventBus.post(o);
    }

    public static void register(Object o){
        eventBus.register(o);
    }

    public static void unregister(Object o){
        eventBus.unregister(o);
    }

}
