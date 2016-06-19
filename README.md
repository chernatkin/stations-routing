# Public Transport Routing



## runnig service

```Shell
mvn clean install
java -jar target/executable-stations-routing-0.0.1-SNAPSHOT.jar (or ./run.sh on Linux and run.bat on Windows))

```
service will start and ready for STDIN input data

## requests example
4

A -> B: 240

A -> C: 70

A -> D: 120

C -> B: 60

route A -> B

A -> C -> B: 130

nearby A, 130

C: 70, D: 120, B: 130


## notes

This task (and any that require heavy computing) require compromise between totally runtime computing and totally caching, which depends on task initial conditions

This solution based on totally caching because:
- public transport system usually is static and changes very rarely
- nodes (and edges) number is limited and system contains several hundreds stations
- route requests may be frequent and response should be fast 


 Assumptions above cause follow consequences:
- routes computation after loading all edges have O(N^3) complexity in worst case (executes one time), computation on this step uses Dijkstra`s algorithm for finding the shortest paths
- routes cache require O(N^2) memory
- route  request processing have O(1) complexity
- nearby request processing have O(Log(N) + M*Log(M)) worst case complexity (N - Number of stations, M - Number of available nearby stations)

- in public transport system time of moving between stations may not have accuracy less than second (all time intervals in seconds)
- some hard memory optimization is possible by replace some standard collection by arrays, by non Java SE collections, but they make code understanding harder and now not used in this solution  
- improvements of initial computations include parallel computation shortest routes if computer have more than one core


Main classes:

Router - contains routing and nearby processing code;

RouterBuilder - contains initial computations code


For more easy understanding, unit tests have examples of different transport systems routing