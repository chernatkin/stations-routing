package org.chernatkin.routing.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public abstract class GraphUtils {

    public static <T> Map<Node<T>, Route<T>> findRoutes(Node<T> start){
        
        final Map<Node<T>, Route<T>> weigths = new HashMap<>();
        weigths.put(start, new Route<T>(0, Collections.<T>singletonList(start.getStation())));
        
        final Set<Node<T>> visited = new HashSet<>();
        
        final Queue<Node<T>> notVisited = new LinkedList<>();
        notVisited.add(start);
        
        while(!notVisited.isEmpty()){
            Node<T> current = notVisited.remove();
            visited.add(current);
            
            Route<T> currentNodeRoute = weigths.get(current);
            
            for(Edge<T> edge : current.getOut()){
                Node<T> neighborNode = edge.getNode();
                if(!visited.contains(neighborNode)){
                    notVisited.add(neighborNode);
                }
                
                Route<T> neighborNodeOldRoute = weigths.get(neighborNode);
                
                int routeNewWeight = currentNodeRoute.getWeight() + edge.getWeigth();
                if(neighborNodeOldRoute == null || routeNewWeight < neighborNodeOldRoute.getWeight()){
                    final List<T> stations = new ArrayList<>(currentNodeRoute.getStations().size() + 1);
                    stations.addAll(currentNodeRoute.getStations());
                    stations.add(neighborNode.getStation());
                    
                    weigths.put(neighborNode, new Route<T>(routeNewWeight, stations));
                }  
            }
        }
        
        return weigths;
    }
    
}
