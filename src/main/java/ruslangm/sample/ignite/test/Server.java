package ruslangm.sample.ignite.test;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.cluster.ClusterNode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.internal.IgniteInterruptedCheckedException;
import org.apache.ignite.internal.util.lang.GridAbsPredicate;
import org.apache.ignite.testframework.GridTestUtils;
import org.apache.log4j.BasicConfigurator;
import org.junit.Test;
import ruslangm.sample.ignite.listener.EventListener;
import ruslangm.sample.ignite.listener.RemoteFactory;

import javax.cache.Cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class Server {
    private static final long DATA_AMOUNT = 10_000L;
    private static final long TIMEOUT = 30_000L;

    @Test
    public void test() throws InterruptedException, IgniteInterruptedCheckedException {
        BasicConfigurator.configure();
        Ignite grid0 = Ignition.start("server1.xml");
        Ignite grid1 = Ignition.start("server2.xml");

        CacheConfiguration<String, Long> cfg = new CacheConfiguration<>();
        cfg.setCacheMode(CacheMode.PARTITIONED);
        cfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        cfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_ASYNC);
        cfg.setName("myCache");
        cfg.setBackups(2);

        IgniteCache<String, Long> cache = grid0.getOrCreateCache(cfg);

        ClusterNode node0 = grid0.cluster().localNode();
        ClusterNode node1 = grid1.cluster().localNode();

        ContinuousQuery<String, Long> qry0 = new ContinuousQuery<>();
        EventListener lsnr0 = new EventListener();
        qry0.setLocalListener(lsnr0).setRemoteFilterFactory(new RemoteFactory(node0));

        ContinuousQuery<String, Long> qry1 = new ContinuousQuery<>();
        EventListener lsnr1 = new EventListener();
        qry1.setLocalListener(lsnr1).setRemoteFilterFactory(new RemoteFactory(node1));

        try (QueryCursor<Cache.Entry<String, Long>> cursor0 = cache.query(qry0);
             QueryCursor<Cache.Entry<String, Long>> cursor1 = cache.query(qry1)) {
            for (long i = 0; i < DATA_AMOUNT; i++) {
                cache.put("" + i, i);
                cache.remove("" + i, i);
            }

            boolean allEvtsRcvd = GridTestUtils.waitForCondition(new GridAbsPredicate() {
                @Override
                public boolean apply() {
                    return lsnr0.evtsCnt() + lsnr1.evtsCnt() >= DATA_AMOUNT;
                }
            }, TIMEOUT);

            assertTrue("All events are received by listener", allEvtsRcvd);
//            this assert is not working too
//            assertEquals("All events are received by listener", DATA_AMOUNT, lsnr0.evtsCnt() + lsnr1.evtsCnt());

            long initSum = lsnr0.evtsCnt() + lsnr1.evtsCnt();

            grid1.close();
            Thread.sleep(10_000L);

            long sum = lsnr0.evtsCnt() + lsnr1.evtsCnt();
            assertEquals(initSum, sum);
        }
    }
}
