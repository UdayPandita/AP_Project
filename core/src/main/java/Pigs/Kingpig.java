package Pigs;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
public class Kingpig extends Pig {
    public Texture   texture;
    public TextureRegion textureregion;
    public int health;
    public Kingpig() {
        texture = new Texture("character.png");
        textureregion = new TextureRegion(  texture,21,1,67,81);
       health = 20;
    }
}
