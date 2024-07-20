import java.io.IOException;

public class BookingSystem {
    public static void main(String[] args) {


        try {
            if (args.length != 2){
                throw new Exception("ERROR: This program works exactly with two command line arguments, the first one is the path to the input file whereas the second one is the path to the output file. Sample usage can be as follows: \"java8 BookingSystem input.txt output.txt\". Program is going to terminate!");
            }

            String[] inputFile = FileIO.readFile(args[0], true, true);
            String outputFile = args[1];

            int x = FileIO.writeToFile(outputFile, "", false, false);
            if (x == -1){ // If writeToFile() returns -1, it means that it caught IOException.
                throw new IllegalArgumentException("ERROR: This program cannot write to the \""+ outputFile + "\", please check the permissions to write that directory. Program is going to terminate!");
            }

            Terminal.operations(inputFile,outputFile);

        } catch (IOException i){ // The exception coming from the readFile() method.
            System.out.println(i.getMessage());
        }

        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }


}
