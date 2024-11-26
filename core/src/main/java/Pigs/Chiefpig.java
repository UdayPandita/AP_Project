package Pigs;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
public class Chiefpig extends Pig {
   public  Texture  texture;
    public TextureRegion  textureregion;
    public int health;
    public Chiefpig() {
        texture= new Texture("character.png");
        textureregion = new TextureRegion(   texture,20,307,61,54);
        health = 15;
    }
    public int getHealth() {
        return health;
    }
    public void setHealth(int health) {
        this.health = health;
    }
}
