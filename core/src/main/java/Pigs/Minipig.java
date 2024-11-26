package Pigs;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
public class Minipig extends Pig {
    public Texture texture;
    public TextureRegion textureregion;
    public int health;
    public Minipig() {
        texture = new Texture("pig.png");
        textureregion = new TextureRegion(texture,57, 509, 24, 23);
        health = 2;
    }
    public int gethealth() {
        return health;
    }
    public void sethealth(int health) {
        this.health = health;
    }
}
