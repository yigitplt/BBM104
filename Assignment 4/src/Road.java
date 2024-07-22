import java.util.Comparator;

/**
 * Represents a road that connects two points.
 */
public class Road {
    private final String point1;
    private final String point2;
    private final int length;
    private final int id;


    public Road(String point1, String point2, int length, int id){
        this.length= length;
        this.id = id;
        this.point1 = point1;
        this.point2 = point2;
    }

    public String getPoint1() {
        return point1;
    }

    public String getPoint2() {
        return point2;
    }

    public int getLength() {
        return length;
    }

    public int getId() {
        return id;
    }


}

/**
 * Compares two roads according to their lengths.
 * Compares their id if their lengths are equal.
 */
class RoadComparator implements Comparator<Road> {
    @Override
    public int compare(Road road1, Road road2) {
        // Compare distances
        int lengthResult = Integer.compare(road1.getLength(), road2.getLength());
        if (lengthResult != 0) {
            return lengthResult;
        }

        // If distances are equal, compare IDs
        return Integer.compare(road1.getId(), road2.getId());
    }
}
