/**
 * Class representing a product in the machine.
 */
public class Product {

    /*
    Attributes for Product.
    Used float and short in general for the sake of memory usage.
    Used int for stock to make sure that there is no problem with the increments and decrements.
     */
    private String name;
    private short price;
    private float protein;
    private float carb;
    private float fat;
    private float calorie;
    private int stock;

    //getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public short getPrice() {
        return price;
    }

    public void setPrice(short price) {
        this.price = price;
    }

    public float getProtein() {
        return protein;
    }

    public void setProtein(float protein) {
        this.protein = protein;
    }

    public float getCarb() {
        return carb;
    }

    public void setCarb(float carb) {
        this.carb = carb;
    }

    public float getFat() {
        return fat;
    }

    public void setFat(float fat) {
        this.fat = fat;
    }

    public float getCalorie() {
        return calorie;
    }

    public void setCalorie(float calorie) {
        this.calorie = calorie;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    /**
     * Calculates the calorie amount of the product.
     * Cast to float to decrease memory usage.
     *
     * @return The calorie amount of the product.
     */
    public float findCalorie() {

        return (float) 4 * protein + 4 * carb + 9 * fat;
    }


}