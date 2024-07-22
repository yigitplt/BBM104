import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

/**
 * The class which is responsible for creating the game map.
 */
public class MapCreator {
    private final GridPane grid;
    private final AnchorPane root;

    public MapCreator(GridPane grid, AnchorPane root){
        this.grid = grid;
        this.root = root;
    }

    /**
     * Creates the sky and the ground by adding rectangles to the grid.
     */
    public void createBackground(){

        //Adds blue rectangles to the first three rows of the grid to represent sky.
        for (int i = 0; i < 3; i++){
            for (int k = 0; k < 18; k++){
                Rectangle sky = new Rectangle(50,50);
                sky.setFill(Color.LIGHTSKYBLUE);
                grid.add(sky,k,i);
            }
        }

        //Adds brown rectangles to the remaining rows to represent ground.
        for (int i = 3; i < 18; i++){
            for (int k = 0; k < 18; k++){
                Rectangle ground = new Rectangle(50,50);
                ground.setFill(Color.SADDLEBROWN);
                grid.add(ground,k,i);
            }
        }
    }

    /**
     * Creates the first ground row, which is soil with grass on top.
     */
    public void createTopRow(){
        for (int i = 0; i < 18; i++){
            Top top = new Top();
            grid.add(top.getImage(),i,3);
            grid.getProperties().put(top.getImage(),top); //Adds the object to the image as a property, so we can understand what class the image belongs.
        }
    }

    /**
     * Creates boulders at the left, right, and bottom parts of the map.
     */
    public void createObstacles(){

        //Left boulders
        for (int i = 4; i < 18; i++){
            Obstacle obstacle = new Obstacle();
            grid.add(obstacle.getImage(),0,i);
            grid.getProperties().put(obstacle.getImage(),obstacle); //Adds the object to the image as a property, so we can understand what class the image belongs.
        }

        //Right boulders
        for (int i = 4; i < 18; i++){
            Obstacle obstacle = new Obstacle();
            grid.add(obstacle.getImage(),17,i);
            grid.getProperties().put(obstacle.getImage(),obstacle); //Adds the object to the image as a property, so we can understand what class the image belongs.
        }

        //Bottom boulders
        for (int i = 1; i < 17; i++) {
            Obstacle obstacle = new Obstacle();
            grid.add(obstacle.getImage(), i, 17);
            grid.getProperties().put(obstacle.getImage(),obstacle); //Adds the object to the image as a property, so we can understand what class the image belongs.
        }
    }

    /**
     * Puts the objects taken from the array list of the random map to the grid.
     */
    public void createUnderground(){

        ArrayList<Underground> undergrounds = Underground.pickRandomMap(); //An array list that holds every underground object that is generated randomly.

        int rowIndex = 4;
        int colIndex = 1;

        for (Underground underground : undergrounds) {
            ImageView image = underground.getImage();
            grid.add(image, colIndex, rowIndex); //Adds the image to the grid.
            grid.getProperties().put(image, underground);  //Adds the object to the image as a property, so we can use its attributes like money.

            // Increment column index
            colIndex++;
            if (colIndex == 17) {
                // If column index reaches 17, move to the next row
                colIndex = 1;
                rowIndex++;
            }

            // Check if the rowIndex exceeds 16 (grid height)
            if (rowIndex > 16) {
                // Break the loop if grid height is reached
                break;
            }
        }
    }

    /**
     * Creates the texts that displays the attributes of the machine.
     */
    public void createTexts() {
        VBox texts = new VBox(5);
        Text fuel = new Text("Fuel: 100000.0");
        fuel.setFont(new Font(25));
        Text money = new Text("Money: 0");
        money.setFont(new Font(25));
        Text haul = new Text("Haul: 0");
        haul.setFont(new Font(25));

        texts.getChildren().addAll(fuel, money, haul);
        root.getChildren().add(texts); //Adds the vbox to the anchor pane root.


    }


}
