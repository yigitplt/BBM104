import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        AnchorPane root = new AnchorPane();
        GridPane grid = new GridPane();
        Scene scene = new Scene(root,900,900);
        MapCreator mapCreator = new MapCreator(grid,root);

        mapCreator.createBackground();
        mapCreator.createTopRow();
        mapCreator.createObstacles();
        mapCreator.createUnderground();

        root.getChildren().add(grid);

        mapCreator.createTexts();

        Mechanics mechanics = new Mechanics(grid, root);
        mechanics.keyEvents(scene);

        primaryStage.setScene(scene);
        primaryStage.setTitle("HU-Load");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
}

