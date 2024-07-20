/**
 * Class representing the machine containing products.
 */
public class Machine {
    private static final short rowNumber = 6;
    private static final short columnNumber = 4;
    private static Product[][] productSlots = new Product[rowNumber][columnNumber];

    public static Product getProductSlots(int row, int col) {
        return productSlots[row][col];
    }

    public static void setProductSlots(int row, int col, Product product) {
        productSlots[row][col] = product;
    }

    /**
     * Fills the machine with products based on input data.
     *
     * @param input  An array containing product information.
     * @param output The output file to write on
     * @return 0 if successful, -1 if the machine is full.
     */
    public short fill(String[] input, String output) {


        for (String line : input) {

            // creates a product object and sets values
            Product product = new Product();
            String[] parts = line.split("\t");
            product.setName(parts[0]);
            product.setPrice(Short.parseShort(parts[1]));
            String[] values = parts[2].split("\\s");
            product.setProtein(Float.parseFloat(values[0]));
            product.setCarb(Float.parseFloat(values[1]));
            product.setFat(Float.parseFloat(values[2]));
            product.setCalorie(product.findCalorie());


            boolean duplicate = false; // used to determine if the product is new or already in a slot


            /*
            If the product already has a slot;
            adds it to the current slot if the stock isn't full.
            If it is full, marks it as non-duplicate and proceeds like as is a new item.
             */
            for (int row = 0; row < rowNumber; row++) {
                for (int col = 0; col < columnNumber; col++) {
                    if (productSlots[row][col] != null && product.getName().equals(productSlots[row][col].getName())) {
                        if (productSlots[row][col].getStock() == 10) {
                            duplicate = false;
                        } else {
                            duplicate = true;
                            productSlots[row][col].setStock(productSlots[row][col].getStock() + 1);
                            break; // Break out of the inner loop once a duplicate is found
                        }
                    }
                }
                if (duplicate) // Break out of the outer loop once a duplicate is found
                    break;
            }


            //If it is a new item, creates a new slot
            if (!duplicate) {
                boolean filled = false;
                for (int row = 0; row < rowNumber; row++) {
                    for (int col = 0; col < columnNumber; col++) {
                        if (productSlots[row][col] == null) {
                            filled = true;
                            productSlots[row][col] = product;
                            productSlots[row][col].setStock(productSlots[row][col].getStock() + 1);

                            break; // Break out of the inner loop once the product is added
                        }
                    }
                    if (filled) { // break out of the outer loop
                        break;
                    }
                }


                if (!filled) {
                    FileOutput.writeToFile(output, "INFO: There is no available place to put " + product.getName(), true, true);
                    if (isFull()) {
                        FileOutput.writeToFile(output, "INFO: The machine is full!", true, true);
                        return -1;
                    }
                }
            }


        }
        return 0;

    }

    /**
     * Simulates buying products from the machine based on purchase input.
     *
     * @param purchaseInput An array containing purchase information.
     * @param output        The output file to write on
     */
    public void buyFromMachine(String[] purchaseInput, String output) {


        for (String line : purchaseInput) {

            // creates a purchase object and sets values
            Purchase purchase = new Purchase();
            FileOutput.writeToFile(output, "INPUT: " + line, true, true);
            String[] parts = line.split("\t");
            purchase.setType(parts[0]);
            String[] givenMoney = parts[1].split("\\s");
            purchase.setMoney(new short[givenMoney.length]); // creates a new money array
            for (int i = 0; i < givenMoney.length; i++) { // adds money values to array
                purchase.setMoneyAtIndex(i, Short.parseShort(givenMoney[i]));
            }

            purchase.isValidArray(output); // checks the money array before buying

            short totalValidMoney = purchase.totalValidMoney(output);


            //If the user wants to buy with a slot number, calls the related method.
            if (parts[2].equals("NUMBER")) {
                short wantedNum = Short.parseShort(parts[3]);

                short boughtWithNumber = purchase.buyWithNumber(wantedNum, totalValidMoney, output);
                if (boughtWithNumber == 0) {
                    continue; // continue with the next purchase if there is no problem with this one
                }

            }

            //If the user wants to buy with one of these values, calls the related method.
            String[] input = {"CARB", "PROTEIN", "FAT", "CALORIE"};
            short boughtWithValue = 1; // To use the return value of the method. Assigned to 1 to initiate.
            for (String i : input) {
                if (parts[2].equals(i)) {
                    int wantedValue = Integer.parseInt(parts[3]);
                    boughtWithValue = purchase.buyWithValue(wantedValue, parts, totalValidMoney, output);


                }

            }
            if (boughtWithValue == 0) {
                continue; // continue with the next purchase if there is no problem with this one
            }

            FileOutput.writeToFile(output, "RETURN: Returning your change: " + totalValidMoney + " TL", true, true);


        }


    }

    /**
     * Checks if the machine is full.
     *
     * @return True if the machine is full, otherwise false.
     */
    public static boolean isFull() {
        for (Product[] products : productSlots) {
            for (Product product : products) {
                if (product == null) {
                    return false;
                } else if (product.getStock() != 10) {
                    return false;
                }

            }
        }
        return true;
    }

    /**
     * Prints the contents of the machine.
     */
    public void print(String output) {
        FileOutput.writeToFile(output, "-----Gym Meal Machine-----", true, true);
        for (int a = 0; a < 6; a++) {
            for (int b = 0; b < 4; b++) {
                if (productSlots[a][b] == null) {
                    FileOutput.writeToFile(output, "___(0, 0)___", true, false);
                } else {
                    FileOutput.writeToFile(output, productSlots[a][b].getName() + "(" + (int) (productSlots[a][b].getCalorie() + 0.5) + ", " + productSlots[a][b].getStock() + ")___", true, false);
                }


            }
            FileOutput.writeToFile(output, "\n", true, false);

        }
        FileOutput.writeToFile(output, "----------", true, true);
    }


}
