package Pigs;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
public class Pig {
    public Texture pg;
    public TextureRegion pig;
    public int health;
    public Pig() {
        pg = new Texture("pig.png");
        pig = new TextureRegion(pg,60, 195, 40, 39);
        health = 5;
    }
}
