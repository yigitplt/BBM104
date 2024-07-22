import java.util.*;

/**
 * Contains the methods of Map Analyzer.
 */
public class RoadOperations {

    /**
     *Creates a hash map to indicate the roads that every point have.
     *
     * @param roads All roads taken from the input file.
     * @return A hash map that contains points as key and an array list of all of its roads as value.
     */
    public Map<String, ArrayList<Road>> linkRoads(ArrayList<Road> roads){
        Map<String, ArrayList<Road>> linkedRoads = new HashMap<>();

        for (Road road: roads){

            //Adds the points of the road to the map if they aren't already there.
            if (!linkedRoads.containsKey(road.getPoint1())){
                linkedRoads.put(road.getPoint1(),new ArrayList<>());
            }if (!linkedRoads.containsKey(road.getPoint2())){
                linkedRoads.put(road.getPoint2(),new ArrayList<>());
            }

            //Adds the road as a value of the point.
            if (linkedRoads.containsKey(road.getPoint1())){
                linkedRoads.get(road.getPoint1()).add(road);
            }if (linkedRoads.containsKey(road.getPoint2())){
                linkedRoads.get(road.getPoint2()).add(road);
            }
        }

        //Sorts the roads of every point.
        for (String point: linkedRoads.keySet()){
            linkedRoads.get(point).sort(new RoadComparator());
        }

        
        return linkedRoads;
    }

    /**
     * Finds the fastest route from the starting point to all other points.
     *
     * @param startingPoint The starting point.
     * @param linkedRoads   The map of linked roads.
     * @return A map of points to nodes representing the fastest route to each point.
     */
    public Map<String, Node> findFastestRoute(String startingPoint, Map<String, ArrayList<Road>> linkedRoads){
        Map<String, Node> fastestRoutes = new HashMap<>();
        ArrayList<Node> nodeList = new ArrayList<>();

        // Initialize the node list with roads from the starting point
        for (Road road: linkedRoads.get(startingPoint)){
            nodeList.add(new Node(road.getLength(),road));
        }

        // Add the starting point to the fastest routes. This is done to be able to start from the starting point.
        fastestRoutes.put(startingPoint,new Node(0,new Road("Ankara", "Ankara", 0,0)));

        while (!nodeList.isEmpty()){
            Node shortest = nodeList.remove(0);
            String nextPoint = null;

            // Determine the next point to process
            if (fastestRoutes.containsKey(shortest.getRoad().getPoint1()) && !fastestRoutes.containsKey(shortest.getRoad().getPoint2())){
                nextPoint = shortest.getRoad().getPoint2(); //Next point is p2 if p1 is already in the map.
            }else if (fastestRoutes.containsKey(shortest.getRoad().getPoint2()) && !fastestRoutes.containsKey(shortest.getRoad().getPoint1())){
                nextPoint = shortest.getRoad().getPoint1(); //Next point is p1 if p2 is already in the map.
            }else{
                continue; //Skip this node if both ends of its road are already in the map.
            }

            if(!fastestRoutes.containsKey(nextPoint)){
                // Add the next point to the fastest routes.
                fastestRoutes.put(nextPoint,shortest);

                //Add the roads of next point to the node list.
                if (linkedRoads.containsKey(nextPoint)){
                    for (Road road: linkedRoads.get(nextPoint)){
                        boolean found = false;
                        for (String s: fastestRoutes.keySet()){
                            if (fastestRoutes.get(s).getRoad().equals(road)){
                                found = true;
                                break; //Don't add it again if the road is added before.
                            }
                        }
                        if (!found){
                            //Add the road as a new node, adding the length of it to the distance of the point to the start.
                            nodeList.add(new Node(shortest.getDistance() + road.getLength(), road));
                        }
                    }
                    nodeList.sort(new NodeComparator());
                }
            }
        }

        return fastestRoutes;
    }

    /**
     * Rebuilds the shortest roads without creating a cycle.
     *
     * @param linkedRoads The map of linked roads.
     * @return An array list of roads that form the BCM.
     */
    public ArrayList<Road> calculateBarelyConnectedMap(Map<String, ArrayList<Road>> linkedRoads){
        TreeSet<String> sortedPoints = new TreeSet<>(linkedRoads.keySet()); //Alphabetically sorted points.
        Set<String> addedPoints = new HashSet<>(); //A set to see which points are processed.
        ArrayList<Road> roadList = new ArrayList<>();
        ArrayList<Road> barelyConnectedMap = new ArrayList<>();

        //Add all roads of the point that comes first alphabetically to the road list.
        roadList.addAll(linkedRoads.get(sortedPoints.first()));

        addedPoints.add(sortedPoints.first());
        roadList.sort(new RoadComparator());

        // Continue until all points are added to the BCM
        while (addedPoints.size() != sortedPoints.size()){
            Road smallest = roadList.remove(0);
            String nextPoint = null;

            // Determine the next point to add
            if(addedPoints.contains(smallest.getPoint1()) && !addedPoints.contains(smallest.getPoint2())){
                nextPoint = smallest.getPoint2(); //Next point is p2 if p1 is already added.
            }else if(addedPoints.contains(smallest.getPoint2()) && !addedPoints.contains(smallest.getPoint1())){
                nextPoint = smallest.getPoint1(); //Next point is p1 if p2 is already added.
            }else{
                continue; //Skip this road if both of its ends are added before.
            }

            barelyConnectedMap.add(smallest);

            for (Road road: linkedRoads.get(nextPoint)){
                //Add the road to the road list. Don't add it if it was added before or both of its ends are already processed.
                if(!roadList.contains(road) && !(addedPoints.contains(road.getPoint1()) && addedPoints.contains(road.getPoint2()))){
                    roadList.add(road);
                    addedPoints.add(nextPoint);
                }
            }
            roadList.sort(new RoadComparator()); //Sort the roads after adding the roads of the next point.
        }
        barelyConnectedMap.sort(new RoadComparator());
        return barelyConnectedMap;
    }

    /**
     * Analyzes the original map and BCM, and writes the analysis results to the output file.
     *
     * @param roads              All the roads of the original map.
     * @param barelyConnectedMap Roads of the barely connected map.
     * @param originalDistance Fastest route of the original map.
     * @param bcmDistance Fastest route of BCM
     * @param output Output file to write on.
     */
    public void analysis(ArrayList<Road> roads, ArrayList<Road> barelyConnectedMap, int originalDistance, int bcmDistance, String output){
        float totalLength = 0; //Total length of all the roads on the original map.
        float totalLengthOfBCM = 0; //Total length of all the roads on BCM.

        for (Road road: roads){
            totalLength += road.getLength();
        }
        for (Road road: barelyConnectedMap){
            totalLengthOfBCM += road.getLength();
        }

        FileIO.writeToFile(output,"Analysis:",true,true);

        String s1 = String.format("Ratio of Construction Material Usage Between Barely Connected and Original Map: %.2f", totalLengthOfBCM/totalLength);
        FileIO.writeToFile(output,s1,true,true);

        String s2 = String.format("Ratio of Fastest Route Between Barely Connected and Original Map: %.2f", (double) bcmDistance / originalDistance);
        FileIO.writeToFile(output,s2,true,false);
    }

    /**
     * Prints which roads are used to reach ending point from starting point.
     *
     * @param startingPoint The point that the route starts.
     * @param endingPoint   The point that we want to reach.
     * @param fastestRoutes The map of fastest routes to points.
     * @param output        Output file to write on.
     */
    public void printFastestRoute(String startingPoint, String endingPoint, Map<String,Node> fastestRoutes, String output){
        ArrayList<Road> printedRoads = new ArrayList<>();
        String currentPoint = endingPoint;

        //Start from ending point and follow the roads until the starting point is reached.
        while (!currentPoint.equals(startingPoint)){
            printedRoads.add(fastestRoutes.get(currentPoint).getRoad());

            //Determine the other end of the road.
            if (fastestRoutes.get(currentPoint).getRoad().getPoint1().equals(currentPoint)){
                currentPoint = fastestRoutes.get(currentPoint).getRoad().getPoint2();
            }else{
                currentPoint = fastestRoutes.get(currentPoint).getRoad().getPoint1();
            }
        }

        Collections.reverse(printedRoads); //Reverse is needed since the iteration was from end to start.

        for (Road road: printedRoads){
            FileIO.writeToFile(output,road.getPoint1() + "\t" + road.getPoint2() + "\t" + road.getLength() + "\t" + road.getId() + "\n", true,false);
        }
    }

    /**
     * Prints the roads of BCM.
     *
     * @param barelyConnectedMap Roads of BCM.
     * @param output             Output file to write on.
     */
    public void printBarelyConnectedMap(ArrayList<Road> barelyConnectedMap, String output){
        for (Road road: barelyConnectedMap){
            FileIO.writeToFile(output, road.getPoint1() + "\t" + road.getPoint2() + "\t" + road.getLength() + "\t" + road.getId() + "\n",true,false);
        }
    }
}
