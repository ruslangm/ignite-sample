package ruslangm.sample.ignite.listener;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryUpdatedListener;
import javax.cache.event.EventType;

import org.apache.ignite.lang.IgniteAsyncCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@IgniteAsyncCallback
public class EventListener implements CacheEntryUpdatedListener<String, Long> {
    private final static Logger LOGGER = LoggerFactory.getLogger(EventListener.class);

    @Override
    public void onUpdated(
            Iterable<CacheEntryEvent<? extends String, ? extends Long>> events) throws CacheEntryListenerException {
        for (CacheEntryEvent<? extends String, ? extends Long> event : events) {
            if (event.getEventType() == EventType.CREATED) {
                long millis = System.currentTimeMillis();
                long delta = millis - event.getValue();
                LOGGER.debug("Time diff between put and listener - " + delta);
            }
        }
    }
}
