/**
 * Class representing a purchase made from the machine.
 */
public class Purchase {
    private String type; // We don't use it for now. It might be useful if the machine accepts different kind of payment in the future.
    private short[] money;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public short[] getMoney() {
        return money;
    }

    public void setMoney(short[] money) {
        this.money = money;
    }

    /**
     * Adds the given money at a specific index of the money array
     *
     * @param index The index that is wanted to be changed
     * @param value The value of the money
     */
    public void setMoneyAtIndex(int index, short value) {
        money[index] = value;
    }

    /**
     * Buys a product with a specified number identifier.
     * Gives related info messages if there is a problem with
     * the purchase.
     *
     * @param wantedNum       The number identifier of the product to be bought.
     * @param givenValidMoney The total valid money provided by the user.
     * @param output          The output file to write on
     * @return 0 if successful, -1 if the purchase cannot be completed.
     */
    public short buyWithNumber(int wantedNum, short givenValidMoney, String output) {
        if (wantedNum > 24) {
            FileOutput.writeToFile(output, "INFO: Number cannot be accepted. Please try again with another number.", true, true);
            return -1;
        } else {

            // when we divide the wanted slot num with the number of rows, the quotient gives the row and the remainder gives the column.
            Product wantedProduct = Machine.getProductSlots(wantedNum / 4, wantedNum % 4);

            if (wantedProduct == null) {
                FileOutput.writeToFile(output, "INFO: This slot is empty, your money will be returned.", true, true);
                return -1;
            } else {
                if (givenValidMoney >= wantedProduct.getPrice()) { // if the given money is enough, proceed to buy
                    FileOutput.writeToFile(output, "PURCHASE: You have bought one " + wantedProduct.getName(), true, true);
                    FileOutput.writeToFile(output, "RETURN: Returning your change: " + (givenValidMoney - wantedProduct.getPrice()) + " TL", true, true);
                    wantedProduct.setStock(wantedProduct.getStock() - 1);

                    // removes the product from the array if the stock is finished after buying
                    if (wantedProduct.getStock() == 0) {
                        Machine.setProductSlots(wantedNum / 4, wantedNum % 4, null);
                    }

                } else {
                    FileOutput.writeToFile(output, "INFO: Insufficient money, try again with more money.", true, true);
                    return -1;
                }
            }
        }
        return 0;

    }

    /**
     * Buys a product with a specified value attribute.
     * Gives related info messages if there is a problem
     * with the purchase.
     *
     * @param wantedValue     The desired value of the attribute (e.g., protein, carb, fat, calorie).
     * @param parts           The parts of the purchase input.
     * @param givenValidMoney The total valid money provided by the user.
     * @param output          The output file to write on
     * @return 0 if successful, -1 if the purchase cannot be completed.
     */
    public short buyWithValue(int wantedValue, String[] parts, short givenValidMoney, String output) {
        boolean found = false; // to keep track if a suitable product is found or not.
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 4; col++) {
                Product product = Machine.getProductSlots(row, col);

                if (product != null) {
                    float productValue = 0; // This represents the productValue of the product. (carb etc.) Given 0 just to initate.

                    // gets the values according to the choice
                    if (parts[2].equals("CARB")) {
                        productValue = product.getCarb();
                    } else if (parts[2].equals("PROTEIN")) {
                        productValue = product.getProtein();
                    } else if (parts[2].equals("FAT")) {
                        productValue = product.getFat();
                    } else if (parts[2].equals("CALORIE")) {
                        productValue = product.getCalorie();
                    }


                    if (productValue >= wantedValue - 5 && productValue <= wantedValue + 5) {
                        found = true;
                        if (givenValidMoney >= product.getPrice()) { // if the given money is enough, proceed to buy
                            FileOutput.writeToFile(output, "PURCHASE: You have bought one " + product.getName(), true, true);
                            FileOutput.writeToFile(output, "RETURN: Returning your change: " + (givenValidMoney - product.getPrice()) + " TL", true, true);
                            product.setStock(product.getStock() - 1);
                            // removes the product from the array if the stock is finished after buying
                            if (product.getStock() == 0) {
                                Machine.setProductSlots(row, col, null);
                            }

                            break;
                        } else {
                            FileOutput.writeToFile(output, "INFO: Insufficient money, try again with more money.", true, true);
                            return -1;
                        }
                    }
                }

            }
            if (found) {
                break; // break from the outer loop if the product is found
            }
        }
        if (!found) {
            FileOutput.writeToFile(output, "INFO: Product not found, your money will be returned.", true, true);
            return -1;

        }
        return 0;
    }

    /**
     * Checks if a given money value is valid.
     *
     * @param money The money value to check for validity.
     * @return 0 if valid, -1 if invalid.
     */
    public static short isValidMoney(short money) {
        short[] validMoney = {1, 5, 10, 20, 50, 100, 200};
        boolean valid = false;
        for (short i : validMoney) {
            if (i == money) {
                valid = true;
                break;
            }
        }
        if (!valid) {
            return -1;
        }
        return 0;
    }


    /**
     * Checks if there is invalid money in the given money array.
     * Writes an info message if there is such a case.
     *
     * @param output The output file to write the info message
     * @return 0 if there is no problem, -1 if the money array is wrong
     */
    public int isValidArray(String output){
        for (short i: money){
            if (isValidMoney(i) == -1){
                FileOutput.writeToFile(output, "INFO: Wrong money given. GMM only accepts 1, 5, 10, 20, 50, 100 and 200 TL",true,true);
                return -1;
            }

        }
        return 0;
    }


    public short totalValidMoney(String output) {
        short total = 0;
        for (short givenMoney : money) {
            if (isValidMoney(givenMoney) == 0) {
                total += givenMoney;
            }
        }
        return total;
    }


}
