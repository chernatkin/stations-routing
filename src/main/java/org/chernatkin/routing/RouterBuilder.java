package org.chernatkin.routing;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.chernatkin.routing.graph.Node;
import org.chernatkin.routing.model.Route;
import org.chernatkin.routing.model.Station;

public class RouterBuilder {
    
    private final Map<String, Node<Station>> nodes;
    
    private final ExecutorService executor;
    
    public static RouterBuilder routerBuilder(int edgesNumber){
        return new RouterBuilder(edgesNumber);
    }
    
    private RouterBuilder(int edgesNumber) {
        this.nodes = new HashMap<>(edgesNumber * 2, 1);
        int coresNumber = Runtime.getRuntime().availableProcessors();
        if(coresNumber > 1){
            executor = new ThreadPoolExecutor(coresNumber, coresNumber, 0, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        }
        else{
            executor = null;
        }
    }

    public RouterBuilder addEdge(String stationFrom, String stationTo, int weight){
        Node<Station> nodeFrom = nodes.computeIfAbsent(stationFrom, k -> new Node<Station>(new Station(k)));
        Node<Station> nodeTo = nodes.computeIfAbsent(stationTo, k -> new Node<Station>(new Station(k)));
        
        nodeFrom.addOutEdge(nodeTo, weight);
        nodeTo.addInEdge(nodeFrom, weight);
        
        return this;
    }
    
    public Router build() {
        final Router router = new Router(nodes.size());
        
        for (Node<Station> node : nodes.values()){
            if(executor != null){
                executor.submit(() -> addRouterInfo(router, node));
            }
            else{
                addRouterInfo(router, node);
            }
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
        final Map<Node<Station>, Route> routes = GraphUtils.findRoutes(node);
        RoutingInfo info = new RoutingInfo(routes.size());
        
        for(Map.Entry<Node<Station>, Route> entry : routes.entrySet()){
            info.addRoute(entry.getKey().getStation(), entry.getValue());
        }
        
        info.build();
        
        router.addStationRoutingInfo(node.getStation(), info);
    }

}
