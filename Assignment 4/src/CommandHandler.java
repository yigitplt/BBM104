import java.util.ArrayList;
import java.util.Map;

/**
 * This class is responsible for handling the input file and applying the commands to analyze the map.
 */
public class CommandHandler {
    private final String inputFile;
    private final String outputFile;

    public CommandHandler(String input, String output){
        this.inputFile = input;
        this.outputFile = output;
    }

    /**
     * Reads the input file, processes the data, and writes the analysis results to the output file.
     */
    public void applyCommands(){
        String[] input = FileIO.readFile(inputFile,true,true);

        String startingPoint = input[0].split("\t")[0];
        String endingPoint = input[0].split("\t")[1];

        //Adds all the roads in the input file to an array list.
        ArrayList<Road> roads = new ArrayList<>();
        for (int i = 1; i < input.length; i++){
            String[] parts = input[i].split("\t");
            roads.add(new Road(parts[0],parts[1], Integer.parseInt(parts[2]),Integer.parseInt( parts[3])));
        }

        RoadOperations roadOperations = new RoadOperations();

        //Link points with their roads.
        Map<String, ArrayList<Road>> linkedRoads = roadOperations.linkRoads(roads);

        // Find and print the fastest route on the original map
        Map<String, Node> fastestRoutes = roadOperations.findFastestRoute(startingPoint, linkedRoads);
        FileIO.writeToFile(outputFile,"Fastest Route from " + startingPoint + " to " + endingPoint + " (" + fastestRoutes.get(endingPoint).getDistance() + " KM):",true,true);
        roadOperations.printFastestRoute(startingPoint,endingPoint,fastestRoutes,outputFile);

        // Calculate and print the Barely Connected Map (BCM)
        ArrayList<Road> barelyConnectedMap = roadOperations.calculateBarelyConnectedMap(linkedRoads);
        FileIO.writeToFile(outputFile,"Roads of Barely Connected Map is:",true,true);
        roadOperations.printBarelyConnectedMap(barelyConnectedMap,outputFile);

        // Link roads for the BCM and find the fastest route on it
        Map<String, ArrayList<Road>> linkedRoadsOfBCM = roadOperations.linkRoads(barelyConnectedMap);
        Map<String, Node> fastestRoutesOfBCM = roadOperations.findFastestRoute(startingPoint, linkedRoadsOfBCM);
        FileIO.writeToFile(outputFile,"Fastest Route from " + startingPoint + " to " + endingPoint + " on Barely Connected Map (" + fastestRoutesOfBCM.get(endingPoint).getDistance() + " KM):",true,true);
        roadOperations.printFastestRoute(startingPoint,endingPoint, fastestRoutesOfBCM,outputFile);

        // Perform and print the analysis of the original map and BCM
        roadOperations.analysis(roads,barelyConnectedMap,fastestRoutes.get(endingPoint).getDistance() ,fastestRoutesOfBCM.get(endingPoint).getDistance(), outputFile);
    }
}
