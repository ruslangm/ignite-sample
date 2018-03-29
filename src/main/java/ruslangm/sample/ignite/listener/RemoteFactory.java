package ruslangm.sample.ignite.listener;

import org.apache.ignite.Ignite;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.lang.IgniteAsyncCallback;
import org.apache.ignite.resources.IgniteInstanceResource;

import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEvent;
import javax.cache.event.CacheEntryEventFilter;

@IgniteAsyncCallback
public class RemoteFactory implements Factory<CacheEntryEventFilter<String, Long>> {
    private final ClusterNode node;

    public RemoteFactory(ClusterNode node) {
        this.node = node;
    }

    @Override
    public CacheEntryEventFilter<String, Long> create() {
        return new CacheEntryEventFilter<String, Long>() {
            @IgniteInstanceResource
            private Ignite ignite;

            @Override
            public boolean evaluate(CacheEntryEvent<? extends String, ? extends Long> cacheEntryEvent) {
                return node.id().equals(ignite.cluster().localNode().id());
            }
        };
    }
}
