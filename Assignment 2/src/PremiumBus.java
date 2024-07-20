/**
 * A premium bus has 1 premium seat and 2 regular seats in one row.
 * Premium seats has a premium fee.
 * Tickets can be refunded.
 */
public class PremiumBus extends Bus {
    private int refundCut;
    private int premiumFee;

    //Constructor
    public PremiumBus(String id, String departure, String destination, String rowCount, String seatPrice, String refundCut, String premiumFee) throws IllegalArgumentException {
        super(id, departure, destination, rowCount, seatPrice);
        setRefundCut(refundCut);
        setPremiumFee(premiumFee);
        seats = new boolean[getRowCount() * 3];

    }

    public int getRefundCut() {
        return refundCut;
    }

    /**
     * Sets the refund cut if it is valid.
     * Throws exception if it is not.
     *
     * @param refundCut The refund cut of the seat. Taken as String to catch NumberFormatException.
     * @throws IllegalArgumentException Throws IllegalArgumentException if refund cut ins not in range of [0, 100].
     */
    public void setRefundCut(String refundCut) throws IllegalArgumentException {
        try {
            if (0 <= Integer.parseInt(refundCut) && Integer.parseInt(refundCut) <= 100) {
                this.refundCut = Integer.parseInt(refundCut);
            } else {
                throw new IllegalArgumentException(String.format("ERROR: %d is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!", Integer.parseInt(refundCut)));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("ERROR: %s is not an integer that is in range of [0, 100], refund cut must be an integer that is in range of [0, 100]!", refundCut));
        }
    }

    public int getPremiumFee() {
        return premiumFee;
    }

    /**
     * Sets the fee of a premium seat if it is valid.
     * Throws exception if it is not.
     *
     * @param premiumFee The fee of a premium seat. Taken as String to catch NumberFormatException.
     * @throws IllegalArgumentException Throws IllegalArgumentException if premium fee is not in range of [0, 100].
     */
    public void setPremiumFee(String premiumFee) throws IllegalArgumentException {
        try {
            if (Integer.parseInt(premiumFee) >= 0) {
                this.premiumFee = Integer.parseInt(premiumFee);
            }else{
                throw new IllegalArgumentException(String.format("ERROR: %d is not a non-negative integer, premium fee must be a non-negative integer!",Integer.parseInt(premiumFee)));
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(String.format("ERROR: %s is not a non-negative integer, premium fee must be a non-negative integer!",premiumFee));
        }
    }

    /**
     * Calculates the price of a premium seat using premium fee.
     *
     * @return The price of a premium seat.
     */
    public double premiumSeatPrice() {
        return (getSeatPrice() + getSeatPrice() * ((double) premiumFee / 100));
    }

    /**
     * Calculates the refund price of a seat.
     *
     * @return Refund price.
     */
    public double refundPrice(int seat) {
        if (seat % 3 == 0) { // If the seat  is premium, use premiumSeatPrice.
            return premiumSeatPrice() - premiumSeatPrice() * ((double) refundCut / 100);
        }

        return getSeatPrice() - getSeatPrice() * ((double) refundCut / 100);
    }

    @Override
    public void initVoyage(String[] parts, String output) {
        String info = String.format("Voyage %d was initialized as a premium (1+2) voyage from %s to %s with %.2f TL priced %d regular seats and %.2f TL priced %d premium seats. Note that refunds will be %d%% less than the paid amount.", getId(), getDeparture(), getDestination(), getSeatPrice(), 2 * getRowCount(), premiumSeatPrice(), getRowCount(), getRefundCut());
        FileIO.writeToFile(output, info, true, true);
    }

    @Override
    public void sellTicket(String[] parts, String output) throws IllegalArgumentException {
        double actionCost = 0; //The money taken from the user.
        String[] sWantedSeats = parts[2].split("_");
        int[] wantedSeats = new int[sWantedSeats.length];


        // Switches the wanted seats from String to int.
        for (int i = 0; i < sWantedSeats.length; i++) {
            try {
                if (Integer.parseInt(sWantedSeats[i]) <= 0) {
                    throw new IllegalArgumentException(String.format("ERROR: %d is not a positive integer, seat number must be a positive integer!", Integer.parseInt(sWantedSeats[i])));
                }
                wantedSeats[i] = Integer.parseInt(sWantedSeats[i]) - 1; //-1 because seat 1 is at index 0 etc.
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

                if (seat % 3 == 0) { // If the seat is premium, use premium seat price.
                    actionCost += premiumSeatPrice();
                    setRevenue(getRevenue() + premiumSeatPrice());
                } else {
                    actionCost += getSeatPrice();
                    setRevenue(getRevenue() + getSeatPrice());
                }
            }
        }

        String str = String.format("Seat %s of the Voyage %d from %s to %s was successfully sold for %.2f TL.", parts[2].replace("_", "-"), getId(), getDeparture(), getDestination(), actionCost);
        FileIO.writeToFile(output, str, true, true);
    }

    @Override
    public void refundTicket(String[] parts, String output) throws IllegalArgumentException {
        double actionCost = 0; // The money that will be refunded to the user.
        String[] sRefundedSeats = parts[2].split("_");
        int[] refundedSeats = new int[sRefundedSeats.length];

        // Switches the refunded seats from String to int.
        for (int i = 0; i < sRefundedSeats.length; i++) {
            try {
                if (Integer.parseInt(sRefundedSeats[i]) <= 0){
                    throw new IllegalArgumentException(String.format("ERROR: %d is not a positive integer, seat number must be a positive integer!", Integer.parseInt(sRefundedSeats[i])));
                }
                refundedSeats[i] = Integer.parseInt(sRefundedSeats[i]) - 1; //-1 because seat 1 is at index 0 etc.
            } catch (NumberFormatException e) { //Parsing throws NumberFormatException if a refunded seat is not an integer.
                throw new IllegalArgumentException(String.format("ERROR: %s is not a positive integer, seat number must be a positive integer!", sRefundedSeats[i]));
            }
        }


        try {
            for (int k = 0; k < refundedSeats.length;k++){
                if (!seats[refundedSeats[k]]){ // If the user tries to refund an empty seat, throws exception.
                    throw new IllegalArgumentException("ERROR: One or more seats are already empty!");
                }
                for (int i = k + 1; i <refundedSeats.length; i++){
                    if (refundedSeats[k] == refundedSeats[i]){ // if a seat is given two times, throws exception
                        throw new IllegalArgumentException("ERROR: Same seat can't be refunded more than once!");
                    }
                }
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("ERROR: There is no such a seat!");
        }

        for (int seat : refundedSeats) {
            if (seats[seat]) { // If the seat is occupied, switches it to empty and decreases the revenue.
                seats[seat] = false;
                actionCost += refundPrice(seat);
                setRevenue(getRevenue() - refundPrice(seat));
            }
        }

        String str = String.format("Seat %s of the Voyage %d from %s to %s was successfully refunded for %.2f TL.",
                parts[2].replace("_", "-"), getId(), getDeparture(), getDestination(), actionCost);
        FileIO.writeToFile(output, str, true, true);


    }

    @Override
    public void cancelVoyage(String output) {
        for (int i = 0; i < seats.length; i++) {
            if (seats[i]) {
                if (i % 3 == 0) { // If the seat is premium, use premium seat price.
                    setRevenue(getRevenue() - premiumSeatPrice());
                } else {
                    setRevenue(getRevenue() - getSeatPrice());
                }
            }
        }
        FileIO.writeToFile(output, "Voyage " + getId() + " was successfully cancelled!", true, true);
        FileIO.writeToFile(output, "Voyage details can be found below:", true, true);
        printVoyage(output);
    }

    public void printVoyage(String output) {
        FileIO.writeToFile(output, "Voyage " + getId(), true, true);
        FileIO.writeToFile(output, getDeparture() + "-" + getDestination(), true, true);
        for (int i = 0; i < seats.length; i++) {
            if (!seats[i]) {
                if (i % 3 == 0) { // Every seat except the first one in the row has space character in front.
                    FileIO.writeToFile(output, "*", true, false);
                } else {
                    FileIO.writeToFile(output, " *", true, false);
                }
            } else {
                if (i % 3 == 0) { // Every seat except the first one in the row has space character in front.
                    FileIO.writeToFile(output, "X", true, false);
                } else {
                    FileIO.writeToFile(output, " X", true, false);
                }
            }

            if (i % 3 == 2) { // Adds new line after the end of a row.
                FileIO.writeToFile(output, "", true, true);
            } else if (i % 3 == 0) { // Adds a line representing the corridor after the second seat of a row.
                FileIO.writeToFile(output, " |", true, false);
            }
        }
        FileIO.writeToFile(output, String.format("Revenue: %.2f", getRevenue()), true, true);
    }
}
