package Elements;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Structure {
    public Body body;
    public int health ;
    public Texture elements;
    public TextureRegion region;
    public boolean destroyed = false;

}

