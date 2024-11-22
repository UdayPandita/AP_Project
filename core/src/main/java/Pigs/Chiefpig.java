package Pigs;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
public class Chiefpig {
   public  Texture pg;
    public TextureRegion chiefpig;
    public int health;
    public Chiefpig() {
        pg = new Texture("character.png");
        chiefpig = new TextureRegion(pg,20,307,61,54);
        health = 15;
    }
}
