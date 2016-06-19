package org.chernatkin.routing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.chernatkin.routing.graph.Edge;
import org.chernatkin.routing.graph.Node;
import org.chernatkin.routing.model.Route;
import org.chernatkin.routing.model.Station;

public abstract class GraphUtils {

    public static Map<Node<Station>, Route> findRoutes(Node<Station> start){
        
        final Map<Node<Station>, Route> weigths = new HashMap<>();
        weigths.put(start, new Route(0, Collections.<Station>singletonList(start.getStation())));
        
        final Set<Node<Station>> visited = new HashSet<>();
        
        final Queue<Node<Station>> notVisited = new LinkedList<>();
        notVisited.add(start);
        
        while(!notVisited.isEmpty()){
            Node<Station> current = notVisited.remove();
            visited.add(current);
            
            Route currentNodeRoute = weigths.get(current);
            
            for(Edge<Station> edge : current.getOut()){
                Node<Station> neighborNode = edge.getNode();
                if(!visited.contains(neighborNode)){
                    notVisited.add(neighborNode);
                }
                
                Route neighborNodeOldRoute = weigths.get(neighborNode);
                
                int routeNewWeight = currentNodeRoute.getWeight() + edge.getWeigth();
                if(neighborNodeOldRoute == null || routeNewWeight < neighborNodeOldRoute.getWeight()){
                    final List<Station> stations = new ArrayList<>(currentNodeRoute.getStations().size() + 1);
                    stations.addAll(currentNodeRoute.getStations());
                    stations.add(neighborNode.getStation());
                    
                    weigths.put(neighborNode, new Route(routeNewWeight, stations));
                }  
            }
        }
        
        return weigths;
    }
    
}
