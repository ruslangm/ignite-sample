## Sample project for testing EventListener's work.

**When I created one server node and put 10 elements in IgniteCache I've got good results. For 10 elements it was (time diff in millis):**

ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 51
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 2
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 1
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 1
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 1
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 2
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 2
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 2
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 2
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 1

**When I added one server node (Topology snapshot became: [ver=2, servers=2, clients=0, CPUs=4, heap=3.6GB]) and setBackups(0)
I also have got good results:**

ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 52
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 1
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 2
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 2
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 3
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 4
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 6
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 6
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 6

**But when I setBackups(1) (Topology snapshot still had 2 servers) I've got strange results:**

ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 573
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 573
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 570
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 571
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 571
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 571
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 571
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 561
ruslangm.sample.ignite.listener.EventListener  - Time diff between put and listener - 560
