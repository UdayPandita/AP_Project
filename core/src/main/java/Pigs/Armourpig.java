package Pigs;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Armourpig {
    public Texture pg;
    public TextureRegion armourpig;
    public int health;
    public Body body;
    public Armourpig() {
        pg = new Texture("character.png");
        armourpig = new TextureRegion(pg,192,460,50,43);
        health=10;
    }
}
