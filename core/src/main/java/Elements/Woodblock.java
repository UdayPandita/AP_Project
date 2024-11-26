package Elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
public class Woodblock{
    public Texture elements= new Texture(Gdx.files.internal("buildingelements.png"));
    public TextureRegion region;
    public int health;
    public Woodblock() {
        elements=new Texture(Gdx.files.internal("buildingelements.png"));
        region=new TextureRegion(elements,0, 40, 41, 41);
        health=8;
    }
    public int gethealth() {
        return health;
    }
    public void sethealth(int health) {
        this.health = health;
    }
}
