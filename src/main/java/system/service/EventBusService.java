package system.service;


import com.google.common.eventbus.EventBus;

import java.util.Objects;

public class EventBusService {

    public final String DEBUG_TAG = EventBusService.class.getSimpleName();

    private static EventBus eventBus = new EventBus();

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
