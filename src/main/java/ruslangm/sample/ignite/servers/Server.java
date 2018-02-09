package ruslangm.sample.ignite.servers;

import java.util.Random;
import java.util.Scanner;
import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.cache.query.ContinuousQuery;
import org.apache.ignite.cache.query.QueryCursor;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ruslangm.sample.ignite.listener.EventListener;

import javax.cache.Cache;

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
            cfg.setBackups(1);
            IgniteCache<String, Long> cache = ignite.getOrCreateCache(cfg);

            ContinuousQuery<String, Long> query = new ContinuousQuery<>();
            query.setLocalListener(new EventListener());
            query.setLocal(true);
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
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void putToCache(IgniteCache<String, Long> cache) throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            Random random = new Random();
            int randomInt = random.nextInt();
            long millis = System.currentTimeMillis();
            cache.put(String.valueOf(randomInt), millis);
        }
        System.out.println("done");
    }
}
