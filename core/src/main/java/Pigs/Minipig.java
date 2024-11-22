package Pigs;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
public class Minipig {
    public Texture pg;
    public TextureRegion minipig;
    public int health;
    public Minipig() {
        pg = new Texture("pig.png");
        minipig = new TextureRegion(pg,57, 509, 24, 23);
        health = 2;
    }
}
