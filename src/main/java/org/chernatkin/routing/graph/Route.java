package org.chernatkin.routing.graph;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Route<T> {
    
    private final int weight;
    
    private final List<T> stations;

    public Route(int weight, List<T> route) {
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

    public List<T> getStations() {
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
        Route<T> other = (Route<T>) obj;
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
