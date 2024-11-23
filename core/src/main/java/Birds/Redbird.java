package Birds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Redbird {
    public Texture red;
    public TextureRegion redbird;
    public int health;
    public int attack;
    public Body body;
    public Redbird() {
        red = new Texture("birds.png");
        redbird = new TextureRegion(red,929, 888, 72, 69);
        health = 10;
        attack=10;
    }
}
