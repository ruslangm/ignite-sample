package ruslangm.sample.ignite.listener;

import org.apache.ignite.lang.IgniteAsyncCallback;

import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryListenerException;
import javax.cache.event.CacheEntryUpdatedListener;
import javax.cache.event.EventType;
import java.util.concurrent.atomic.AtomicLong;

@IgniteAsyncCallback
public class EventListener implements CacheEntryUpdatedListener<String, Long> {
    private final AtomicLong evtsCnt = new AtomicLong();

    @Override
    public void onUpdated(
            Iterable<CacheEntryEvent<? extends String, ? extends Long>> events) throws CacheEntryListenerException {
        for (CacheEntryEvent<? extends String, ? extends Long> event : events) {
            if (event.getEventType() == EventType.CREATED) {
                evtsCnt.incrementAndGet();
            }
        }
    }

    public long evtsCnt() {
        return evtsCnt.get();
    }
}
