package Birds;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;

public class Blackbird extends Bird {
    public Blackbird() {
        texture = new Texture("birds.png");
        textureregion = new TextureRegion(texture, 581, 491, 82, 87);
        health=10;
        attack=8;
    }
}
