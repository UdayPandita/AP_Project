package Pigs;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Armourpig extends Pig {
    public Texture   texture;
    public TextureRegion  textureregion;
    public int health;
    public Body body;

    public Armourpig() {
        texture = new Texture("character.png");
        textureregion = new TextureRegion(  texture,192,460,50,43);
        health=10;
    }
    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }
}
