package com.mytaxi.app.utils;

import org.greenrobot.eventbus.EventBus;

public class BusProvider {

    private BusProvider() {
        /*Empty constructor*/
    }

    public interface Bus {
        void post(Object event);
    }

    private interface BusRegister extends Bus {
        void register(Object... subscribers);

        void unregister(Object... subscribers);
    }

    /**
     * Specific class created as wrapper for {@link EventBus}
     */
    private static class EventBusWrapper implements BusRegister {

        private EventBus bus;

        public EventBusWrapper(EventBus bus) {
            this.bus = bus;
        }

        @Override
        public void post(Object event) {
            bus.post(event);
        }

        @Override
        public void register(Object... subscribers) {
            for (Object subs : subscribers) {
                if (subs != null) {
                    bus.register(subs);
                }
            }
        }

        @Override
        public void unregister(Object... subscribers) {
            for (Object subs : subscribers) {
                if (subs != null) {
                    bus.unregister(subs);
                }
            }
        }
    }

    private static final BusRegister bus = new EventBusWrapper(EventBus.getDefault());

    public static Bus getInstance() {
        return bus;
    }

    public static void register(Object... subscribers) {
        bus.register(subscribers);
    }

    public static void unregister(Object... subscribers) {
        bus.unregister(subscribers);
    }
}
