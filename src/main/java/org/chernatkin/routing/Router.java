package org.chernatkin.routing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.chernatkin.routing.graph.Route;
import org.chernatkin.routing.model.Station;

public class Router {
    
    private final Map<Station, RoutingInfo> ROUTNG_MAP;
    
    private final Map<String, Station> nameToStation;
    
    public Router(int stationsNumber) {
        ROUTNG_MAP = new HashMap<Station, RoutingInfo>(stationsNumber, 1);
        nameToStation = new HashMap<>(stationsNumber, 1);
    }

    public Route<Station> loadRoute(Station from, Station to){
        Route<Station> route = ROUTNG_MAP.get(from).getRoute(to);
        if(route == null){
            throw new IllegalArgumentException(String.format("Error: No route from %s to %s", from, to));
        }
        return route;
    }
    
    public List<Route<Station>> getNearBy(Station from, int maxTime){
        return ROUTNG_MAP.get(from).getNearBy(maxTime);
    }
    
    public Station getStation(String name){
        return nameToStation.get(name);
    }
    
    public synchronized void addStationRoutingInfo(Station station, RoutingInfo info){
        ROUTNG_MAP.put(station, info);
        nameToStation.put(station.getName(), station);
    }
    
    public Station checkStation(String name){
        Station station = getStation(name);
        if(station == null){
            throw new IllegalArgumentException(String.format("Invalid station:%s", name));
        }
        return station;
    }
}
