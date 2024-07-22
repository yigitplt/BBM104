import java.util.ArrayList;

/**
 * The abstract superclass representing a bus.
 */
public abstract class Bus {

    protected boolean[] seats;
    private int id;
    private String departure;
    private String destination;
    private int rowCount;
    private double seatPrice;
    private double revenue;

    public Bus(String id, String departure, String destination, String rowCount, String seatPrice) throws IllegalArgumentException {
        setId(id);
        this.departure = departure;
        this.destination = destination;
        setRowCount(rowCount);
        setSeatPrice(seatPrice);
    }

    public int getId() {
        return id;
    }

    /**
     * Sets the id if it is valid.
     *
     * @param id The id value given by the user. Taken as String in order to catch NumberFormatException while parsing.
     * @throws IllegalArgumentException Throws this exception if the id is not a positive integer.
     */
    public void setId(String id) throws IllegalArgumentException {
        try {
            if (Integer.parseInt(id) > 0) {
                this.id = Integer.parseInt(id);
            } else {
                throw new IllegalArgumentException(String.format("ERROR: %d is not a positive integer, ID of a voyage must be a positive integer!", Integer.parseInt(id)));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("ERROR: %s is not a positive integer, ID of a voyage must be a positive integer!", id));
        }

    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getRowCount() {
        return rowCount;
    }

    /**
     * Sets the row count if it is valid.
     *
     * @param rowCount The row count value given by the user. Taken as String in order to catch NumberFormatException while parsing.
     * @throws IllegalArgumentException Throws this exception if the row count is not a positive integer.
     */
    public void setRowCount(String rowCount) throws IllegalArgumentException {
        try {
            if (Integer.parseInt(rowCount) > 0) {
                this.rowCount = Integer.parseInt(rowCount);
            } else {
                throw new IllegalArgumentException(String.format("ERROR: %d is not a positive integer, number of seat rows of a voyage must be a positive integer!", Integer.parseInt(rowCount)));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("ERROR: %s is not a positive integer, number of seat rows of a voyage must be a positive integer!", rowCount));
        }


    }

    public double getSeatPrice() {
        return seatPrice;
    }

    /**
     * Sets the seat price if it is valid.
     *
     * @param seatPrice The price value given by the user. Taken as String in order to catch NumberFormatException while parsing.
     * @throws IllegalArgumentException Throws this exception if the price is not a positive number.
     */
    public void setSeatPrice(String seatPrice) throws IllegalArgumentException {
        try {
            if (Double.parseDouble(seatPrice) > 0) {
                this.seatPrice = Double.parseDouble(seatPrice);
            } else {
                throw new IllegalArgumentException("ERROR: " + seatPrice + " is not a positive number, price must be a positive number!");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("ERROR: " + seatPrice + " is not a positive number, price must be a positive number!");
        }
    }

    public double getRevenue() {
        return revenue;
    }

    public void setRevenue(double revenue) {
        this.revenue = revenue;
    }

    /**
     * Writes the confirmation of the voyage initiation to the output.
     *
     * @param parts  Parts of the line of the input that we are working on.
     * @param output The output file to write on.
     */
    public abstract void initVoyage(String[] parts, String output);

    /**
     * Prints the condition of the seats and the revenue of the voyage to the output.
     * (*) stands for empty seats and (X) stands for occupied seats.
     *
     * @param output The output file to write on.
     */
    public abstract void printVoyage(String output);

    /**
     * Sells the wanted empty seats of the voyage with the given id.
     * Adds the taken money to the revenue.
     * Marks the bought seats as occupied.
     *
     * @param parts  Parts of the line of the input that we are working on.
     * @param output The output file to write on.
     */
    public abstract void sellTicket(String[] parts, String output) throws IllegalArgumentException;

    /**
     * Refunds the wanted occupied seats of the voyage with given id with some cut.
     * Subtracts the refunded money from the revenue.
     * Marks the refunded seats as empty.
     *
     * @param parts  Parts of the line of the input that we are working on.
     * @param output The output file to write on.
     */
    public abstract void refundTicket(String[] parts, String output);

    /**
     * Cancels the voyage and returns the full seat price to the customers.
     * Prints the condition of the bus just before the cancellation.
     *
     * @param output The output file to write on.
     */
    public void cancelVoyage(String output) {

        // if a seat is true (bought), refund the full price of the seat.
        for (boolean seat : seats) {
            if (seat) {
                revenue -= seatPrice;
            }
        }

        FileIO.writeToFile(output, "Voyage " + id + " was successfully cancelled!", true, true);
        FileIO.writeToFile(output, "Voyage details can be found below:", true, true);
        printVoyage(output);


    }

    /**
     * Sorts every existing bus according to their ids and prints the condition of all of them.
     *
     * @param busList The array list which is holding the bus objects.
     * @param output  The output file to write on.
     */
    public static void zReport(ArrayList<Bus> busList, String output) {
        busList.sort((bus1, bus2) -> Integer.compare(bus1.getId(), bus2.getId())); //sorts according to id

        FileIO.writeToFile(output, "Z Report:\n----------------", true, true);

        if (busList.isEmpty()) {
            FileIO.writeToFile(output, "No Voyages Available!", true, true);
            FileIO.writeToFile(output, "----------------", true, false);
        } else {
            for (int i = 0; i < busList.size(); i++) {
                busList.get(i).printVoyage(output); //calls printvoyage() method for every bus

                //adds a new line after the lines unless it is the last bus in the list.
                if (i == busList.size() - 1) {
                    FileIO.writeToFile(output, "----------------", true, false);
                } else {
                    FileIO.writeToFile(output, "----------------", true, true);
                }

            }
        }
    }
}
