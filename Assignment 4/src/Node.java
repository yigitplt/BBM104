import java.util.Comparator;

/**
 * Represents an object that holds the distance from the starting point and the
 * road that connects two points that the object holds.
 */
public class Node {
    private final int distance; //Distance from the starting point.
    private final Road road; //The road that is used to arrive this node.


    public Node(int distance, Road road){
        this.distance = distance;
        this.road = road;

    }

    public int getDistance() {
        return distance;
    }

    public Road getRoad() {
        return road;
    }


}

/**
 * Compares two nodes according to their distances from the starting point.
 */
class NodeComparator implements Comparator<Node> {
    @Override
    public int compare(Node node1, Node node2) {
        return Integer.compare(node1.getDistance(), node2.getDistance());
    }
}
