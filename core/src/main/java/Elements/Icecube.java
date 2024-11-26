package Elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
public class Icecube {
    public Texture elements= new Texture(Gdx.files.internal("buildingelements.png"));
    public TextureRegion region;
    public int health;
    public Icecube() {
        elements=new Texture(Gdx.files.internal("buildingelements.png"));
        region=new TextureRegion(elements,442, 159, 22, 20);
        health=5;
    }
    public int gethealth() {
        return health;
    }
    public void sethealth(int health) {
        this.health = health;
    }
}
