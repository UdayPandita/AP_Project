package Elements;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
public class Steelblock {
   public Texture elements= new Texture(Gdx.files.internal("buildingelements.png"));
   public TextureRegion steelblock;
   public int health ;
    public Steelblock() {
        elements=new Texture(Gdx.files.internal("buildingelements.png"));
        steelblock=new TextureRegion(elements,1, 2, 37, 37);
        health=12;
    }
}
