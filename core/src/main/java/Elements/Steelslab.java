package Elements;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Steelslab extends Structure{
    public Texture elements= new Texture(Gdx.files.internal("buildingelements.png"));
    public TextureRegion region;
    public int health;
    public Body body;

    public Steelslab() {
        elements=new Texture(Gdx.files.internal("buildingelements.png"));
        region=new TextureRegion(elements,220, 117, 39, 20);
        health=12;
    }
    @Override
    public int  gethealth() {
        return this.health; // Use the specific WoodenStructure health
    }
    public void sethealth(int health) {
        this.health = health;
    }
}
