package org.chernatkin.routing;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.chernatkin.routing.graph.Edge;
import org.chernatkin.routing.graph.GraphUtils;
import org.chernatkin.routing.graph.Node;
import org.chernatkin.routing.graph.Route;
import org.chernatkin.routing.model.Station;

public class RouterBuilder {
    
    private final Map<String, Node<Station>> nodes;
    
    private final Set<Edge<Station>> edges;
    
    private ExecutorService executor;
    
    public static RouterBuilder routerBuilder(int edgesNumber){
        return new RouterBuilder(edgesNumber);
    }
    
    private RouterBuilder(int edgesNumber) {
        this.nodes = new HashMap<>(edgesNumber * 2, 1);
        this.edges = new HashSet<>(edgesNumber, 1);
        int coresNumber = Runtime.getRuntime().availableProcessors();
        this.executor = new ThreadPoolExecutor(coresNumber, coresNumber, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
    }

    public RouterBuilder addEdge(String stationFrom, String stationTo, int weight){
        Node<Station> nodeFrom = nodes.computeIfAbsent(stationFrom, k -> new Node<Station>(new Station(k)));
        Node<Station> nodeTo = nodes.computeIfAbsent(stationTo, k -> new Node<Station>(new Station(k)));
        
        Edge<Station> edge = new Edge<>(weight, nodeFrom, nodeTo);
        edges.add(edge);
        
        nodeFrom.getIn().add(edge);
        nodeTo.getOut().add(edge);
        
        return this;
    }
    
    public Router build() {
        final Router router = new Router(nodes.size());
        
        for (Node<Station> node : nodes.values()){
            executor.submit(() -> addRouterInfo(router, node));
        }
        
        try {
            executor.shutdown();
            executor.awaitTermination(30, TimeUnit.DAYS);
        } catch (InterruptedException e) {
            throw new RuntimeException("Building routes is interrupted", e);
        }
        
        return router;
    }
    
    private void addRouterInfo(Router router, Node<Station> node){
        final Map<Node<Station>, Route<Station>> routes = GraphUtils.findBellmanFordRoutes(edges, node);
        RoutingInfo info = new RoutingInfo(routes.size());
        
        for(Map.Entry<Node<Station>, Route<Station>> entry : routes.entrySet()){
            info.addRoute(entry.getKey().getStation(), entry.getValue());
        }
        
        info.build();
        
        router.addStationRoutingInfo(node.getStation(), info);
    }

}
