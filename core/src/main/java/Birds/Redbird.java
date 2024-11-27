package Birds;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Redbird extends Bird {
    public Redbird() {
        texture = new Texture("birds.png");
        textureregion = new TextureRegion(texture,929, 888, 72, 69);
        health = 10;
        attack=8;
    }
    @Override
    public int getattack() {
        return attack;
    }
}
