package Pigs;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
public class Kingpig {
    public Texture pg;
    public TextureRegion kingpig;
    public int health;
    public Kingpig() {
        pg = new Texture("character.png");
       kingpig = new TextureRegion(pg,21,1,67,81);
       health = 20;
    }
}
