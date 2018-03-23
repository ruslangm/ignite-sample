package ruslangm.sample.ignite.listener;

import org.apache.ignite.Ignite;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.lang.IgniteAsyncCallback;

import javax.cache.configuration.Factory;
import javax.cache.event.CacheEntryEventFilter;

@IgniteAsyncCallback
public class RemoteFactory implements Factory<CacheEntryEventFilter<String, Long>>{
    private final Ignite ignite;
    private final ClusterNode node;

    public RemoteFactory(Ignite ignite, ClusterNode node) {
        this.ignite = ignite;
        this.node = node;
    }

    @Override
    public CacheEntryEventFilter<String, Long> create() {
        return cacheEntryEvent -> node.equals(ignite.cluster().localNode());
    }
}
