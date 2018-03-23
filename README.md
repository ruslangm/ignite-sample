## Sample project for testing EventListener's work.

Run 2 server nodes. You should see something like this:

Topology snapshot [ver=2, servers=2, clients=0, CPUs=4, heap=3.6GB]

Put elements to cache (I put about 300 elements). Elements will be distributed between two nodes approxiamtely in the same amount. 

As you can see from the code the sequence of call is: 
Put to cache -> sleep 100 ms (to ensure that lsitener has received event) -> delete from cache.

Stop one node. (You shoul see something like: Topology snapshot [ver=3, servers=1, clients=0, CPUs=4, heap=1.8GB])

Look at the log of another server node. 

Topology snapshot [ver=3, servers=1, clients=0, CPUs=4, heap=1.8GB]

ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 5374; N = 107
ruslangm.sample.ignite.listener.EventListener  - -------- 1189809414 --------
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 17651; N = 108
ruslangm.sample.ignite.listener.EventListener  - -------- 550085344 --------
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 16293; N = 109
ruslangm.sample.ignite.listener.EventListener  - -------- 1505243141 --------
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 36203; N = 110
ruslangm.sample.ignite.listener.EventListener  - -------- -869811566 --------
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 23918; N = 111
ruslangm.sample.ignite.listener.EventListener  - -------- -1159391917 --------

Yep, some events (absolutely randomly choosen and deleted from cache to this moment) still came to EventListener.
