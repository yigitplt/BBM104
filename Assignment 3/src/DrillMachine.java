import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

/**
 * Represents the drill machine that the player is using in the game.
 */
public class DrillMachine {
    private float fuel;
    private int haul;
    private int money;
    private final ImageView image;
    private final GridPane grid;
    private final Timeline fuelTime;

    public DrillMachine(float fuel, int haul, int money, GridPane grid){
        this.fuel = fuel;
        this.haul = haul;
        this.money = money;
        this.grid = grid;

        // crops and sets the image
        ImageView imageView = new ImageView(new Image("assets/drill/drill_01.png"));
        Rectangle2D cropRegion = new Rectangle2D(0, 0, 70, 61);
        imageView.setViewport(cropRegion);
        imageView.setFitWidth(50.0);
        imageView.setFitHeight(50.0);
        this.image = imageView;

        //Sets the timer to decrease the fuel periodically.
        fuelTime = new Timeline(new KeyFrame(Duration.seconds(0.25), event -> decreaseFuel()));
        fuelTime.setCycleCount(Timeline.INDEFINITE);
        fuelTime.play();

        GridPane.setValignment(image, VPos.BOTTOM);
        grid.add(image,16,2); // starting position of the machine

    }

    public float getFuel() {
        return fuel;
    }

    public int getHaul() {
        return haul;
    }

    public void setHaul(int haul) {
        this.haul = haul;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public ImageView getImage() {
        return image;
    }

    /**
     * Decreases the fuel and updates the fuel text shown on the screen.
     * Shows the game over screen if the machine runs out of fuel.
     */
    private void decreaseFuel(){
        fuel -= 0.33F;
        Mechanics.updateTexts();

        if (fuel <= 0){ //Show the game over screen if the fuel is over
            Mechanics.gameOverFuel();
            fuelTime.stop();
        }
    }


    /**
     * Moves the machine to the left grid.
     * Sets the image of the machine as it is facing left.
     */
    public void moveLeft() {
        //Crops and sets the image
        Rectangle2D cropRegion = new Rectangle2D(0, 0, 70, 61);
        image.setViewport(cropRegion);
        image.setImage(new Image("assets/drill/drill_01.png"));
        image.setFitWidth(50.0);
        image.setFitHeight(50.0);image.setImage(new Image("assets/drill/drill_01.png"));

        fuel -= 100; //decreases the fuel every movement

        if (fuel <= 0){ // Shows the game over screen if the machine runs out of fuel.
            Mechanics.gameOverFuel();
            return;
        }

        int currentX = GridPane.getColumnIndex(image);
        int nextX = currentX - 1;
        if (nextX >= 0) { //Makes sure that the machine can't go outside the screen.
            for (Node node : grid.getChildren()) {
                if (GridPane.getColumnIndex(node) == nextX && GridPane.getRowIndex(node) == GridPane.getRowIndex(image)) {//Finds the node that the machine will be moving on.
                    if (grid.getProperties().get(node) instanceof Obstacle) {
                        return; // Don't move if there's an obstacle
                    }
                    else if (grid.getProperties().get(node) instanceof Lava){
                        Mechanics.gameOverLava(); //Shows the game over screen if the machine moves into the lava
                        return;
                    }
                }
            }
            GridPane.setColumnIndex(image, nextX); //Moves the machine to its next location.
        }
    }

    /**
     * Moves the machine to the right grid.
     * Sets the image of the machine as it is facing right.
     */
    public void moveRight() {

        //Crops and sets the image
        Rectangle2D cropRegion = new Rectangle2D(0, 0, 70, 56);
        image.setViewport(cropRegion);
        image.setImage(new Image("assets/drill/drill_55.png"));
        image.setFitWidth(50.0);
        image.setFitHeight(50.0);

        fuel -= 100; //Decreases the fuel every movement

        if (fuel <= 0){
            Mechanics.gameOverFuel(); // Shows the game over screen if the machine runs out of fuel.
            return;
        }

        int currentX = GridPane.getColumnIndex(image);
        int nextX = currentX + 1;
        if (nextX < 18) { //Makes sure that the machine can't go outside the screen.
            for (Node node : grid.getChildren()) {
                if (GridPane.getColumnIndex(node) == nextX && GridPane.getRowIndex(node) == GridPane.getRowIndex(image)) {//Finds the node that the machine will be moving on.
                    if (grid.getProperties().get(node) instanceof Obstacle) {
                        return; // Don't move if there's an obstacle
                    }
                    else if (grid.getProperties().get(node) instanceof Lava){
                        Mechanics.gameOverLava(); //Shows the game over screen if the machine moves into the lava
                        return;
                    }
                }
            }
            GridPane.setColumnIndex(image, nextX); //Moves the machine to its next location.
        }
    }

    /**
     * Moves the machine to the upper grid.
     * Sets the image of the machine as it is facing upwards.
     */
    public void moveUp() {

        //Crops and sets the image
        Rectangle2D cropRegion = new Rectangle2D(10, 0, 70, 56);
        image.setViewport(cropRegion);
        image.setImage(new Image("assets/drill/drill_24.png"));
        image.setFitWidth(50.0);
        image.setFitHeight(50.0);

        fuel -= 150; //Decreases the fuel every movement. The fuel usage is higher while moving up.

        if (fuel <= 0){
            Mechanics.gameOverFuel(); // Shows the game over screen if the machine runs out of fuel.
            return;
        }

        int currentY = GridPane.getRowIndex(image);
        int nextY = currentY - 1;
        if (nextY >= 0) { //Makes sure that the machine can't go outside the screen.
            for (Node node : grid.getChildren()) {
                if (GridPane.getRowIndex(node) == nextY && GridPane.getColumnIndex(node) == GridPane.getColumnIndex(image)) {//Finds the node that the machine will be moving on.
                    if (grid.getProperties().get(node) instanceof Underground) {
                        return; // Don't move if there's an object
                    }
                    else if (grid.getProperties().get(node) instanceof Lava){
                        Mechanics.gameOverLava(); //Shows the game over screen if the machine moves into the lava
                        return;
                    }
                }
            }
            GridPane.setRowIndex(image, nextY);  //Moves the machine to its next location.
        }
    }

    /**
     * Moves the machine to the down grid.
     * Sets the image of the machine as it is facing down.
     */
    public void moveDown() {
        //Crops and sets the image
        Rectangle2D cropRegion = new Rectangle2D(8, 0, 70, 63);
        image.setViewport(cropRegion);
        image.setImage(new Image("assets/drill/drill_41.png"));
        image.setFitWidth(50.0);
        image.setFitHeight(50.0);

        fuel -= 100; //Decreases the fuel every movement.

        if (fuel <= 0){
            Mechanics.gameOverFuel(); // Shows the game over screen if the machine runs out of fuel.
            return;
        }

        int currentY = GridPane.getRowIndex(image);
        int nextY = currentY + 1;
        if (nextY < 18) { //Makes sure that the machine can't go outside the screen.
            for (Node node : grid.getChildren()) {
                if (GridPane.getRowIndex(node) == nextY && GridPane.getColumnIndex(node) == GridPane.getColumnIndex(image)) {//Finds the node that the machine will be moving on.
                    if (grid.getProperties().get(node) instanceof Obstacle) {
                        return; // Don't move if there's an obstacle
                    }
                    else if (grid.getProperties().get(node) instanceof Lava){
                        Mechanics.gameOverLava(); //Shows the game over screen if the machine moves into the lava
                        return;
                    }
                }
            }
            GridPane.setRowIndex(image, nextY); //Moves the machine to its next location.
        }
    }

    /**
     * Moves the machine to the down grid if it is empty.
     * Sets the image of the machine as it is flying.
     */
    public void applyGravity() {
        //Crops and sets the image.
        Rectangle2D cropRegion = new Rectangle2D(10, 0, 70, 56);
        image.setViewport(cropRegion);
        image.setImage(new Image("assets/drill/drill_24.png"));
        image.setFitWidth(50.0);
        image.setFitHeight(50.0);

        int currentY = GridPane.getRowIndex(image);
        int nextY = currentY + 1;
        if (nextY < 18) { //Makes sure that the machine can't go outside the screen.
            GridPane.setRowIndex(image, nextY); //Moves the machine to its next location.
        }
    }



}



