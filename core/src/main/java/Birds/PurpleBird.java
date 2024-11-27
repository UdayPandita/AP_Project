package Birds;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class PurpleBird extends Bird{
    public PurpleBird() {
        texture = new Texture("birds.png");
        textureregion = new TextureRegion(texture, 829, 742, 76, 74);
        health = 100;
        attack=8;
    }
    @Override
    public int getattack() {
        return attack;
    }
}
