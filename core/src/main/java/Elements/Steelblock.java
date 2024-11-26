package Elements;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Steelblock extends Structure {
   public Texture elements= new Texture(Gdx.files.internal("buildingelements.png"));
   public TextureRegion steelblock;
   public int health ;
    public Body body;

    public Steelblock() {
        elements=new Texture(Gdx.files.internal("buildingelements.png"));
        steelblock=new TextureRegion(elements,1, 2, 37, 37);
        health=12;
    }
    @Override
    public int gethealth() {
        return this.health; // Use the specific WoodenStructure health
    }
    public void sethealth(int health) {
        this.health = health;
    }
}
