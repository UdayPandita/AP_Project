package Birds;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Blackbird {
    public Texture black;
    public TextureRegion blackbird;
    public int health;
    public int attack;
    public Blackbird() {
        black = new Texture("birds.png");
        blackbird = new TextureRegion(black, 581, 491, 82, 87);
        health=10;
        attack=15;
    }

}
