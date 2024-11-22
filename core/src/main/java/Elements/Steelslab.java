package Elements;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
public class Steelslab {
    public Texture elements= new Texture(Gdx.files.internal("buildingelements.png"));
    public TextureRegion region;
    public int health;
    public Steelslab() {
        elements=new Texture(Gdx.files.internal("buildingelements.png"));
        region=new TextureRegion(elements,220, 117, 39, 20);
        health=12;
    }
}
