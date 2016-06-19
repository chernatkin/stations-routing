package org.chernatkin.routing.graph;

import java.util.HashSet;
import java.util.Set;

public class Node<T> {
    
    private final T station;
    
    private final Set<Edge<T>> in = new HashSet<>();
    
    private final Set<Edge<T>> out = new HashSet<>();
    
    public Node(T station) {
        this.station = station;
    }

    public T getStation() {
        return station;
    }
    
    public void addInEdge(Node<T> node, int weight){
        addEdge(in, node, weight);
    }
    
    public void addOutEdge(Node<T> node, int weight){
        addEdge(out, node, weight);
    }
    
    private void addEdge(Set<Edge<T>> edges, Node<T> node, int weight){
        edges.add(new Edge<T>(weight, node));
    }

    public Set<Edge<T>> getIn() {
        return in;
    }

    public Set<Edge<T>> getOut() {
        return out;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((station == null) ? 0 : station.hashCode());
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
        Node<T> other = (Node<T>) obj;
        if (station == null) {
            if (other.station != null)
                return false;
        } else if (!station.equals(other.station))
            return false;
        return true;
    }
}
