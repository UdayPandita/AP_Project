package Elements;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class SteelTriangle extends Structure {
    public Texture elements= new Texture(Gdx.files.internal("buildingelements.png"));
    public TextureRegion region;
    public int health;
    public Body body;

    public SteelTriangle() {
        elements=new Texture(Gdx.files.internal("buildingelements.png"));
        region=new TextureRegion(elements,40, 2, 41, 40);
         health=12;
    }

    @Override
    public int getHealth() {
        return this.health; // Use the specific WoodenStructure health
    }
    public void setHealth(int health) {
        this.health = health;
    }
}
