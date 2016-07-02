package org.chernatkin.routing.graph;

public class Edge<T> {
    
    private final int weight;
    
    private final Node<T> headNode;

    private final Node<T> tailNode;
    
    public Edge(int weight, Node<T> tailNode, Node<T> headNode) {
        this.weight = weight;
        this.headNode = headNode;
        this.tailNode = tailNode;
    }

    public int getWeight() {
        return weight;
    }

    public Node<T> getHeadNode() {
        return headNode;
    }

    public Node<T> getTailNode() {
        return tailNode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result
                + ((headNode == null) ? 0 : headNode.hashCode());
        result = prime * result
                + ((tailNode == null) ? 0 : tailNode.hashCode());
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
        Edge other = (Edge) obj;
        if (headNode == null) {
            if (other.headNode != null)
                return false;
        } else if (!headNode.equals(other.headNode))
            return false;
        if (tailNode == null) {
            if (other.tailNode != null)
                return false;
        } else if (!tailNode.equals(other.tailNode))
            return false;
        if (weight != other.weight)
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "Edge [weight=" + weight + ", headNode=" + headNode + ", tailNode=" + tailNode + "]";
    }
}
