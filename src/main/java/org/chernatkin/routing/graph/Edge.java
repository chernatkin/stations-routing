package org.chernatkin.routing.graph;

public class Edge<T> {
    
    private final int weigth;
    
    private final Node<T> node;

    public Edge(int weigth, Node<T> node) {
        this.weigth = weigth;
        this.node = node;
    }

    public int getWeigth() {
        return weigth;
    }

    public Node<T> getNode() {
        return node;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((node == null) ? 0 : node.hashCode());
        result = prime * result + weigth;
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
        Edge<T> other = (Edge<T>) obj;
        if (node == null) {
            if (other.node != null)
                return false;
        } else if (!node.equals(other.node))
            return false;
        if (weigth != other.weigth)
            return false;
        return true;
    }
    
    

}
