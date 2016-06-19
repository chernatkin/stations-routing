package org.chernatkin.routing.model;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Route {
    
    private final int weight;
    
    private final List<Station> stations;

    public Route(int weight, List<Station> route) {
        this.weight = weight;
        this.stations = Collections.unmodifiableList(route);
    }

    public Route(int time) {
        this.weight = time;
        this.stations = Collections.emptyList();
    }

    public int getWeight() {
        return weight;
    }

    public List<Station> getStations() {
        return stations;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((stations == null) ? 0 : stations.hashCode());
        result = prime * result + weight;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Route other = (Route) obj;
        if (stations == null) {
            if (other.stations != null)
                return false;
        } else if (!stations.equals(other.stations))
            return false;
        if (weight != other.weight)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return stations.stream().map(s -> s.toString()).collect(Collectors.joining(" -> ")) + ": " + weight;
    }

    
}
