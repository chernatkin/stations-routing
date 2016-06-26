package org.chernatkin.routing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.chernatkin.routing.graph.Route;
import org.chernatkin.routing.model.Station;

public class RoutingInfo {
    
    private final Map<Station, Route<Station>> stationToTime;

    private final TreeMap<Integer, List<Route<Station>>> timeToRoutes = new TreeMap<>();
    
    public RoutingInfo(int stationsNumber) {
        stationToTime = new HashMap<>(stationsNumber, 1);
    }

    public void addRoute(Station station, Route<Station> route){
        stationToTime.put(station, route);
        timeToRoutes.computeIfAbsent(route.getWeight(), k -> new ArrayList<>()).add(route);
        
    }
    
    public RoutingInfo build(){
        return this;
    }
    
    public Route<Station> getRoute(final Station destination){
        return stationToTime.get(destination);
    }
    
    public List<Route<Station>> getNearBy(final int time) {
        return timeToRoutes.subMap(0, false, time, true)
                                         .values()
                                         .stream()
                                         .flatMap(l -> l.stream())
                                         .sorted((o1, o2) -> Integer.compare(o1.getWeight(), o2.getWeight()))
                                         .collect(Collectors.toList());
    }
    
}
