package Elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
public class Steelbrick {
    public Texture elements= new Texture(Gdx.files.internal("buildingelements.png"));
    public TextureRegion region;
    public int health;
    public Steelbrick() {
        elements=new Texture(Gdx.files.internal("buildingelements.png"));
        region=new TextureRegion(elements,496, 62, 22, 12);
        health=12;
    }
    public int gethealth() {
        return health;
    }
    public void sethealth(int health) {
        this.health = health;
    }
}
