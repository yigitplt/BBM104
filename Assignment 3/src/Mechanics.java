import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;

/**
 * Represents the class that is responsible for the game mechanics
 */
public class Mechanics {
    private static GridPane grid;
    private static AnchorPane root;
    private static DrillMachine drillMachine;
    private Timeline gravity;

    public Mechanics(GridPane grid, AnchorPane root) {
        Mechanics.grid = grid;
        Mechanics.root = root;
        drillMachine = new DrillMachine(10000, 0, 0, grid);

        //Keeps applying the gravity.
        gravity = new Timeline(new KeyFrame(Duration.seconds(0.75), event -> Mechanics.gravity()));
        gravity.setCycleCount(Timeline.INDEFINITE);
        gravity.play();

    }

    /**
     * Drills the underground. Removes the image of the drilled object from the grid
     * and collects its money and weight.
     */
    public void drill() {
        int machineRow = GridPane.getRowIndex(drillMachine.getImage());
        int machineColumn = GridPane.getColumnIndex(drillMachine.getImage());

        for (Node node : new ArrayList<>(grid.getChildren())) {
            if (node instanceof ImageView) {
                int nodeRow = GridPane.getRowIndex(node);
                int nodeColumn = GridPane.getColumnIndex(node);

                if (!(node == drillMachine.getImage())) { //Makes sure that we don't remove the machine accidentally.
                    if (machineRow == nodeRow && machineColumn == nodeColumn) {//Finds the row and column of the node that the machine has just moved on.
                        Object object = grid.getProperties().get(node);
                        if (object instanceof Underground) {
                            Underground undergroundObject = (Underground) object;
                            // Checks if the underground object is not an obstacle
                            if (!(undergroundObject instanceof Obstacle)) {
                                drillMachine.setMoney(drillMachine.getMoney() + undergroundObject.getWorth()); //Gets the money
                                drillMachine.setHaul(drillMachine.getHaul() + undergroundObject.getWeight()); //Gets the weight

                                grid.getChildren().remove(node); //Deletes the image of the underground object.
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * Checks if there is something solid under the machine.
     * If there is nothing, makes sure that the machine falls.
     */
    public static void gravity() {
            int currentY = GridPane.getRowIndex(drillMachine.getImage());
            int nextY = currentY + 1; //The grid which is under the machine.

            boolean emptyBottom = true; //To indicate if there is something under the machine.

            for (Node node : grid.getChildren()) {
                if (GridPane.getRowIndex(node) == nextY && GridPane.getColumnIndex(node) == GridPane.getColumnIndex(drillMachine.getImage())) {
                    if ((grid.getProperties().get(node) instanceof Underground)) {
                        //Do nothing if there is an underground object below.
                        emptyBottom = false;
                        break;
                    }
                }
            }

            if (emptyBottom) {
                drillMachine.applyGravity(); //Enables gravity if there is nothing under.
            }

    }


    /**
     * Creates and shows the game over screen if the machine drills into lava.
     */
    public static void gameOverLava() {
        StackPane gameOver = new StackPane(); //Used stack pane because centering is easier.
        Rectangle rectangle = new Rectangle(900, 900);
        rectangle.setFill(Color.RED);
        Text text = new Text("GAME OVER");
        text.setFont(new Font(100));
        text.setFill(Color.WHITE);

        StackPane.setAlignment(text, Pos.CENTER); //Put the text into the center of the pane.
        text.setTranslateY(-37); //Moves the texts slightly up for aesthetic purposes.


        gameOver.getChildren().addAll(rectangle, text);
        root.getChildren().add(gameOver); //Shows the game over screen.
    }

    /**
     * Creates and shows the game over screen if the machine runs out of fuel.
     */
    public static void gameOverFuel() {
        StackPane gameOver = new StackPane(); //Used stack pane because centering is easier.
        VBox texts = new VBox();
        Rectangle rectangle = new Rectangle(900, 900);
        rectangle.setFill(Color.GREEN);
        Text text1 = new Text("GAME OVER");
        Text text2 = new Text("Collected Money: " + drillMachine.getMoney());
        text1.setFont(new Font(100));
        text1.setFill(Color.WHITE);
        text2.setFont(new Font(75));
        text2.setFill(Color.WHITE);

        StackPane.setAlignment(texts, Pos.CENTER);
        texts.setAlignment(Pos.CENTER);
        texts.setTranslateY(-37); //Move the texts slightly up.

        texts.getChildren().addAll(text1, text2);
        gameOver.getChildren().addAll(rectangle, texts);
        root.getChildren().add(gameOver); //Shows the game over screen.

    }


    /**
     * Updates the text that are indicating the attributes of the machine.
     */
    public static void updateTexts() {
        for (Node node : root.getChildren()) {
            if (node instanceof VBox) {
                for (Node text : ((VBox) node).getChildren()) {
                    if (((Text) text).getText().startsWith("Fuel: ")) {
                        ((Text) text).setText("Fuel: " + drillMachine.getFuel());
                    } else if (((Text) text).getText().startsWith("Money: ")) {
                        ((Text) text).setText("Money: " + drillMachine.getMoney());
                    } else if (((Text) text).getText().startsWith("Haul: ")) {
                        ((Text) text).setText("Haul: " + drillMachine.getHaul());
                    }
                }
            }
        }
    }


    /**
     * Handles the movement based on which arrow key is pressed.
     *
     * @param scene The scene we are currently playing on.
     */
    public void keyEvents(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.LEFT) {
                drillMachine.moveLeft();
            } else if (event.getCode() == KeyCode.RIGHT) {
                drillMachine.moveRight();
            } else if (event.getCode() == KeyCode.UP) {
                drillMachine.moveUp();
            } else if (event.getCode() == KeyCode.DOWN) {
                drillMachine.moveDown();
            }

            drill(); //Drills after moving.
            updateTexts(); //Updates the attributes in case the machine collected a valuable.


        });
    }

}
