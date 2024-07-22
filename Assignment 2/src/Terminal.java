import java.util.ArrayList;

/**
 * This class holds the bus objects and does file operations.
 */
public class Terminal {
    private static ArrayList<Bus> busList; // The array list of every bus object that is created.

    /**
     * Calls the related methods and handles exceptions according to the input.
     * This method exists in order to catch exceptions before calling the methods,
     * instead of sending possibly wrong parameters to them.
     *
     * @param inputFile The content of the input file.
     * @param outputFile The output file to write on.
     */
    public static void operations(String[] inputFile, String outputFile) {
        busList = new ArrayList<Bus>();

        if (inputFile.length == 0){ //If the file is empty, call zReport.
            Bus.zReport(busList,outputFile);
        }

        for (int k = 0; k < inputFile.length; k++) {
            FileIO.writeToFile(outputFile, "COMMAND: " + inputFile[k], true, true);
            String[] parts = inputFile[k].split("\t");

            if (parts[0].equals("INIT_VOYAGE")) {
                try {
                    Bus bus;

                    // Create a new bus of wanted type.
                    if (parts[1].equals("Minibus")) {
                        if (parts.length != 7){
                            throw new IllegalArgumentException("ERROR: Erroneous usage of \"INIT_VOYAGE\" command!");
                        }
                        bus = new Minibus(parts[2], parts[3], parts[4], parts[5], parts[6]);
                    } else if (parts[1].equals("Standard")) {
                        if (parts.length != 8){
                            throw new IllegalArgumentException("ERROR: Erroneous usage of \"INIT_VOYAGE\" command!");
                        }
                        bus = new StandardBus(parts[2], parts[3], parts[4], parts[5], parts[6], parts[7]);
                    } else if (parts[1].equals("Premium")) {
                        if (parts.length != 9){
                            throw new IllegalArgumentException("ERROR: Erroneous usage of \"INIT_VOYAGE\" command!");
                        }
                        bus = new PremiumBus(parts[2], parts[3], parts[4], parts[5], parts[6], parts[7], parts[8]);
                    } else {
                        throw new IllegalArgumentException("ERROR: Erroneous usage of \"INIT_VOYAGE\" command!");
                    }

                    // Checks if there is already a voyage with the same id.
                    for (Bus myBus : busList) {
                        if (myBus.getId() == Integer.parseInt(parts[2])) {
                            throw new IllegalArgumentException("ERROR: There is already a voyage with ID of " + parts[2] + "!");
                        }
                    }

                    busList.add(bus); // Adds the created bus object to the array list.
                    bus.initVoyage(parts, outputFile);

                } catch (IllegalArgumentException i) {
                    FileIO.writeToFile(outputFile, i.getMessage(), true, true);
                } catch (Exception e) {
                    FileIO.writeToFile(outputFile, "ERROR: Erroneous usage of \"INIT_VOYAGE\" command!", true, true);
                }


            }

            else if (parts[0].equals("PRINT_VOYAGE")) {

                try {
                    if (parts.length != 2) {
                        throw new IllegalArgumentException("ERROR: Erroneous usage of \"PRINT_VOYAGE\" command!");
                    }

                    if (Integer.parseInt(parts[1]) <= 0) {
                        throw new IllegalArgumentException(String.format("ERROR: %d is not a positive integer, ID of a voyage must be a positive integer!", Integer.parseInt(parts[1])));
                    }

                    boolean found = false;
                    for (Bus bus : busList) {
                        if (bus.getId() == Integer.parseInt(parts[1])) { // If the voyage with the wanted id is found, call the method.
                            found = true;
                            bus.printVoyage(outputFile);
                            break;
                        }
                    }
                    if (!found) {
                        throw new IllegalArgumentException(String.format("ERROR: There is no voyage with ID of %d!", Integer.parseInt(parts[1])));
                    }
                } catch (NumberFormatException i) { // Parsing at line 78 throws NumberFormatException if ID is not an integer.
                    FileIO.writeToFile(outputFile, String.format("ERROR: %s is not a positive integer, ID of a voyage must be a positive integer!", parts[1]), true, true);
                } catch (IllegalArgumentException e) {
                    FileIO.writeToFile(outputFile, e.getMessage(), true, true);
                }
            }

            else if (parts[0].equals("Z_REPORT")) {
                try {
                    if (parts.length != 1) {
                        throw new IllegalArgumentException("ERROR: Erroneous usage of \"Z_REPORT\" command!");
                    }

                    Bus.zReport(busList, outputFile);
                    if (!(k == inputFile.length - 1)) { // Adds a new line unless we are at the end of the file.
                        FileIO.writeToFile(outputFile, "", true, true);
                    }
                } catch (IllegalArgumentException e) {
                    FileIO.writeToFile(outputFile, e.getMessage(), true, true);
                }
            }

            else if (parts[0].equals("SELL_TICKET")) {
                try {

                    if (parts.length != 3) {
                        throw new IllegalArgumentException("ERROR: Erroneous usage of \"SELL_TICKET\" command!");
                    }

                    if (Integer.parseInt(parts[1]) <= 0) {
                        throw new IllegalArgumentException(String.format("ERROR: %d is not a positive integer, ID of a voyage must be a positive integer!", Integer.parseInt(parts[1])));
                    }

                    boolean found = false;
                    for (Bus bus : busList) {
                        if (Integer.parseInt(parts[1]) == bus.getId()) { // If the voyage with the wanted id is found, calls the method.
                            found = true;
                            bus.sellTicket(parts, outputFile);
                            break;
                        }
                    }

                    if (!found) {
                        throw new IllegalArgumentException(String.format("ERROR: There is no voyage with ID of %d!", Integer.parseInt(parts[1])));
                    }
                } catch (NumberFormatException n) { //Parsing at line 122 throws NumberFormatException if the id is not an int.
                    FileIO.writeToFile(outputFile, String.format("ERROR: %s is not a positive integer, ID of a voyage must be a positive integer!", parts[1]), true, true);
                } catch (IllegalArgumentException i) {
                    FileIO.writeToFile(outputFile, i.getMessage(), true, true);
                }


            }

            else if (parts[0].equals("REFUND_TICKET")){
                try {
                    if (parts.length != 3) {
                        throw new IllegalArgumentException("ERROR: Erroneous usage of \"REFUND_TICKET\" command!");
                    }

                    if (Integer.parseInt(parts[1]) <= 0) {
                        throw new IllegalArgumentException(String.format("ERROR: %d is not a positive integer, ID of a voyage must be a positive integer!",Integer.parseInt(parts[1])));
                    }

                    boolean found = false;
                    for (Bus bus : busList) {
                        if (Integer.parseInt(parts[1]) == bus.getId()) { // If the voyage with the wanted id is found, calls the method.
                            found = true;
                            bus.refundTicket(parts, outputFile);
                            break;
                        }
                    }

                    if (!found) {
                        throw new IllegalArgumentException(String.format("ERROR: There is no voyage with ID of %d!", Integer.parseInt(parts[1])));
                    }
                } catch (NumberFormatException n) { //Parsing at line 159 throws NumberFormatException if the id is not an int.
                    FileIO.writeToFile(outputFile, String.format("ERROR: %s is not a positive integer, ID of a voyage must be a positive integer!", parts[1]), true, true);
                } catch (IllegalArgumentException i) {
                    FileIO.writeToFile(outputFile, i.getMessage(), true, true);
                }


            }

            else if (parts[0].equals("CANCEL_VOYAGE")){
                try {
                    if (parts.length != 2) {
                        throw new IllegalArgumentException("ERROR: Erroneous usage of \"CANCEL_VOYAGE\" command!");
                    }

                    if (Integer.parseInt(parts[1]) <= 0) {
                        throw new IllegalArgumentException(String.format("ERROR: %d is not a positive integer, ID of a voyage must be a positive integer!", Integer.parseInt(parts[1])));
                    }

                    boolean found = false;
                    for (Bus bus : busList) {
                        if (Integer.parseInt(parts[1]) == bus.getId()) { //If the voyage with the wanted id is found, calls the method.
                            found = true;
                            bus.cancelVoyage(outputFile);
                            busList.remove(bus); // Remove the bus from the list if it is canceled.
                            break;
                        }
                    }
                    if (!found) {
                        throw new IllegalArgumentException(String.format("ERROR: There is no voyage with ID of %d!", Integer.parseInt(parts[1])));
                    }
                } catch (NumberFormatException n) { //Parsing at line 184 throws NumberFormatException if the id is not an int.
                    FileIO.writeToFile(outputFile, String.format("ERROR: %s is not a positive integer, ID of a voyage must be a positive integer!", parts[1]), true, true);
                }
                catch (IllegalArgumentException i){
                    FileIO.writeToFile(outputFile,i.getMessage(),true,true);
                }


            }

            else { // If an unknown command is used, print an error.
                FileIO.writeToFile(outputFile, "ERROR: There is no command namely " + parts[0] + "!", true, true);
            }

            // If the last command of the input file is not Z_REPORT, call it.
            if (k == inputFile.length - 1) {
                if (!parts[0].equals("Z_REPORT")) {
                    Bus.zReport(busList, outputFile);
                }
            }
        }
    }
}