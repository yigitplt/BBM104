import java.util.Locale;

/**
 * The main class for analyzing the map.
 */
public class MapAnalyzer {
    public static void main(String[] args){
        Locale.setDefault(Locale.US);
        String input = args[0];
        String output = args[1];
        FileIO.writeToFile(output,"",false,false); //Initialize an empty file.

        CommandHandler commandHandler = new CommandHandler(input,output);
        commandHandler.applyCommands();



    }
}
