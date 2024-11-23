package Pigs;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Pig {
    public Texture pg;
    public TextureRegion pig;
    public int health;
    public Body body;
    public Pig() {
        pg = new Texture("pig.png");
        pig = new TextureRegion(pg,60, 195, 40, 39);
        health = 5;
    }
}
