package Birds;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class PurpleBird {
    public Texture purple;
    public TextureRegion purplebird;
    public int health;
    public int attack;
    public Body body;
    public PurpleBird() {
        purple= new Texture("birds.png");
        purplebird = new TextureRegion(purple, 829, 742, 76, 74);
        health = 100;
        attack=8;
    }
}
