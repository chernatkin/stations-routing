package org.chernatkin.routing.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public abstract class GraphUtils {

    public static <T> Map<Node<T>, Route<T>> findDijkstraRoutes(Node<T> start){
        
        final Map<Node<T>, Route<T>> weights = initWeights(start);
        
        final Set<Node<T>> visited = new HashSet<>();
        
        final Queue<Node<T>> notVisited = new LinkedList<>();
        notVisited.add(start);
        
        while(!notVisited.isEmpty()){
            Node<T> current = notVisited.remove();
            visited.add(current);
            
            Route<T> currentNodeRoute = weights.get(current);
            
            for(Edge<T> edge : current.getOut()){
                Node<T> neighborNode = edge.getHeadNode();
                if(!visited.contains(neighborNode)){
                    notVisited.add(neighborNode);
                }
                
                Route<T> neighborNodeOldRoute = weights.get(neighborNode);
                
                updateRouteIfNeed(weights, currentNodeRoute, edge, neighborNodeOldRoute);
            }
        }
        
        return weights;
    }
    
    public static <T> Map<Node<T>, Route<T>> findBellmanFordRoutes(Collection<Edge<T>> edges, Node<T> start){
        
        final Map<Node<T>, Route<T>> weights = initWeights(start);
        
        boolean anyRoutesChanged = false;
        
        do {
            anyRoutesChanged = false;
            
            for(Edge<T> edge : edges){
                final Route<T> tailRoute = weights.get(edge.getTailNode());
                if(tailRoute == null){
                    continue;
                }
                
                final Route<T> headRoute = weights.get(edge.getHeadNode());
                
                anyRoutesChanged |= updateRouteIfNeed(weights, tailRoute, edge, headRoute);
            }

        } while(anyRoutesChanged);
        
        return weights;
    }
    
    private static <T> boolean updateRouteIfNeed(final Map<Node<T>, Route<T>> weights, 
                                                  final Route<T> routeToTail,
                                                  final Edge<T> edge, 
                                                  final Route<T> oldRouteToHead){
        
        final int newRouteWeight = routeToTail.getWeight() + edge.getWeight();
        
        if(oldRouteToHead != null && oldRouteToHead.getWeight() <= newRouteWeight){
            return false;
        }
        
        final List<T> stations = new ArrayList<>(routeToTail.getStations().size() + 1);
        stations.addAll(routeToTail.getStations());
        stations.add(edge.getHeadNode().getStation());
        
        weights.put(edge.getHeadNode(), new Route<T>(newRouteWeight, stations));
        return true;
    }
    
    private static <T> Map<Node<T>, Route<T>> initWeights(Node<T> start){
        final Map<Node<T>, Route<T>> weights = new HashMap<>();
        weights.put(start, new Route<T>(0, Collections.<T>singletonList(start.getStation())));
        
        return weights;
    }
}
