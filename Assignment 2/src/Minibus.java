/**
 * A minibus has 2 regular seats in one row.
 * The tickets are not refundable.
 */
public class Minibus extends Bus {

    //Constructor
    public Minibus(String id, String departure, String destination, String rowCount, String seatPrice) {
        super(id, departure, destination, rowCount, seatPrice);
        seats = new boolean[getRowCount() * 2];

    }

    @Override
    public void initVoyage(String[] parts, String output) {
        String info = String.format("Voyage %d was initialized as a minibus (2) voyage from %s to %s with %.2f TL priced %d regular seats. Note that minibus tickets are not refundable.", getId(), getDeparture(), getDestination(), getSeatPrice(), 2 * getRowCount());
        FileIO.writeToFile(output, info, true, true);
    }

    @Override
    public void sellTicket(String[] parts, String output) throws IllegalArgumentException {

        double actionCost = 0; //The money taken from the user.
        String[] sWantedSeats = parts[2].split("_");
        int[] wantedSeats = new int[sWantedSeats.length];

        // Switches the wanted seats from String to int
        for (int i = 0; i < sWantedSeats.length; i++) {
            try {
                if (Integer.parseInt(sWantedSeats[i]) <= 0) {
                    throw new IllegalArgumentException(String.format("ERROR: %d is not a positive integer, seat number must be a positive integer!", Integer.parseInt(sWantedSeats[i])));
                }
                wantedSeats[i] = Integer.parseInt(sWantedSeats[i]) - 1; // -1 because seat 1 is at index 0 etc.
            } catch (NumberFormatException e) { //Parsing throws NumberFormatException if wanted seat is not an integer.
                throw new IllegalArgumentException(String.format("ERROR: %s is not a positive integer, seat number must be a positive integer!", sWantedSeats[i]));
            }
        }

        try {
            for (int k = 0; k < wantedSeats.length;k++) {
                if (seats[wantedSeats[k]]) { //If the seat is already occupied, throws exception.
                    throw new IllegalArgumentException("ERROR: One or more seats already sold!");
                }
                for (int i = k + 1; i < wantedSeats.length; i++){
                    if (wantedSeats[k] == wantedSeats[i]){ // if a seat is given two times, throws exception
                        throw new IllegalArgumentException("ERROR: Same seat can't be bought more than once!");
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("ERROR: There is no such a seat!");
        }

        for (int seat : wantedSeats) {
            if (!seats[seat]) { // If the seat is empty, switch it to occupied and increase the revenue.
                seats[seat] = true;
                actionCost += getSeatPrice();
                setRevenue(getRevenue() + getSeatPrice());
            }
        }

        String str = String.format("Seat %s of the Voyage %d from %s to %s was successfully sold for %.2f TL.", parts[2].replace("_", "-"), getId(), getDeparture(), getDestination(), actionCost);
        FileIO.writeToFile(output, str, true, true);
    }

    @Override
    public void refundTicket(String[] parts, String output) {
        FileIO.writeToFile(output, "ERROR: Minibus tickets are not refundable!", true, true);
    }


    @Override
    public void printVoyage(String output) {
        FileIO.writeToFile(output, "Voyage " + getId(), true, true);
        FileIO.writeToFile(output, getDeparture() + "-" + getDestination(), true, true);
        for (int i = 0; i < seats.length; i++) {
            if (!seats[i]) {
                if (i % 2 == 0) { //Every second seat in a row has a space character in front.
                    FileIO.writeToFile(output, "*", true, false);
                } else {
                    FileIO.writeToFile(output, " *", true, false);
                }
            } else {
                if (i % 2 == 0) { //Every second seat in a row has a space character in front.
                    FileIO.writeToFile(output, "X", true, false);
                } else {
                    FileIO.writeToFile(output, " X", true, false);
                }
            }

            if (i % 2 == 1) { //Adds a new line after the end of each row
                FileIO.writeToFile(output, "", true, true);
            }
        }
        FileIO.writeToFile(output, String.format("Revenue: %.2f", getRevenue()), true, true);
    }
}
