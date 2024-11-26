package Pigs;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Pig extends Pigs{
    public Texture texture;
    public TextureRegion textureregion;
    public int health;
    public Body body;

    public Pig() {
       texture = new Texture("pig.png");
        textureregion = new TextureRegion(texture,60, 195, 40, 39);
        health = 5;
    }
    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }
}
