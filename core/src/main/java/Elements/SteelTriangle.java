package Elements;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
public class SteelTriangle {
    public Texture elements= new Texture(Gdx.files.internal("buildingelements.png"));
    public TextureRegion region;
    public int health;
    public SteelTriangle() {
        elements=new Texture(Gdx.files.internal("buildingelements.png"));
        region=new TextureRegion(elements,40, 2, 41, 40);
         health=12;
    }
}
