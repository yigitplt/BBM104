import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.*;

/**
 * Represents the every ground object of the game.
 */
public class Underground {
    private int worth;
    private int weight;
    private ImageView image;

    public int getWorth() {
        return worth;
    }

    public void setWorth(int worth) {
        this.worth = worth;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public ImageView getImage() {
        return image;
    }

    public void setImage(ImageView image) {
        this.image = image;
    }

    /**
     * Creates a random map every start.
     *
     * @return A shuffled array list representing the map.
     */
    public static ArrayList<Underground> pickRandomMap(){
        ArrayList<Underground> undergrounds = new ArrayList<>();

        //Adds 140 soils to make sure that they are more than other elements. There are 208 elements in total
        for (int i = 0; i < 140; i++){
            undergrounds.add(new Soil());
        }

        //Adds one object of each object to make sure that they appear in the game.
        undergrounds.add(new Diamond());
        undergrounds.add(new Emerald());
        undergrounds.add(new Ruby());
        undergrounds.add(new Obstacle());
        undergrounds.add(new Lava());

        //Completes the rest of the list randomly.
        for (int i = 0; i < 67; i++){
            double rand = Math.random() * 2;
            if (rand < 0.2) {
                undergrounds.add(new Diamond());
            } else if (rand < 0.4) {
                undergrounds.add(new Emerald());
            } else if (rand < 0.6) {
                undergrounds.add(new Ruby());
            } else if (rand < 0.8) {
                undergrounds.add(new Obstacle());
            } else if (rand < 1) {
                undergrounds.add(new Lava());
            }else{
                undergrounds.add(new Soil());
            }
        }


        Collections.shuffle(undergrounds); //Shuffles the list to get different maps every time.
        return undergrounds;

    }
}

class Diamond extends Underground {
    public Diamond(){
        setWorth(100000);
        setWeight(100);
        setImage(new ImageView(new Image("assets/underground/valuable_diamond.png")));
    }
}

class Emerald extends Underground {
    public Emerald(){
        setWorth(5000);
        setWeight(60);
        setImage(new ImageView(new Image("assets/underground/valuable_emerald.png")));
    }
}

class Ruby extends Underground {
    public Ruby(){
        setWorth(20000);
        setWeight(80);
        setImage(new ImageView(new Image("assets/underground/valuable_ruby.png")));
    }
}

class Top extends Underground {
    public Top(){
        setImage(new ImageView(new Image("assets/underground/top_01.png")));
    }
}

class Obstacle extends Underground {
    public Obstacle(){
        setImage(new ImageView(new Image("assets/underground/obstacle_01.png")));
    }
}

class Soil extends Underground{
    public Soil(){
        setImage(new ImageView(new Image("assets/underground/soil_01.png")));
    }
}

class Lava extends Underground{
    public Lava(){
        setImage(new ImageView(new Image("assets/underground/lava_01.png")));
    }
}


