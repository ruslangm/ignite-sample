package ruslangm.sample.ignite.servers;

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
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ruslangm.sample.ignite.listener.EventListener;
import ruslangm.sample.ignite.listener.RemoteFactory;

import javax.cache.Cache;
import java.util.Random;
import java.util.Scanner;

public class Server {
    private final static Logger LOGGER = LoggerFactory.getLogger(Server.class);

    public static void main(String... args) {
        BasicConfigurator.configure();
        try (Ignite ignite = Ignition.start("example-ignite.xml")) {
            CacheConfiguration<String, Long> cfg = new CacheConfiguration<>();
            cfg.setCacheMode(CacheMode.PARTITIONED);
            cfg.setAtomicityMode(CacheAtomicityMode.ATOMIC);
            cfg.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_ASYNC);
            cfg.setName("myCache");
            cfg.setBackups(2);
            IgniteCache<String, Long> cache = ignite.getOrCreateCache(cfg);
            ClusterNode node = ignite.cluster().localNode();

            ContinuousQuery<String, Long> query = new ContinuousQuery<>();
            query.setLocalListener(new EventListener());
            query.setRemoteFilterFactory(new RemoteFactory(ignite, node));
            QueryCursor<Cache.Entry<String, Long>> cursor = cache.query(query);

            while (true) {
                System.out.print("Put to cache? ");
                Scanner scanner = new Scanner(System.in);
                String input = scanner.nextLine();

                if (input.equals("y")) {
                    putToCache(cache);
                }

                if (input.equals("q")) {
                    System.out.println("Exit");
                    cursor.close();
                    cache.close();
                    System.exit(0);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void putToCache(IgniteCache<String, Long> cache) throws InterruptedException {
        int n = 50;
        for (int i = 0; i < n; i++) {
            Random random = new Random();
            String key = String.valueOf(random.nextInt());
            long millis = System.currentTimeMillis();
            cache.put(key, millis);
            Thread.sleep(100); // sleep to ensure that event has been delivered to listener
            cache.removeAsync(key); // remove object from cache
        }
        System.out.println("done - " + n);
    }
}
