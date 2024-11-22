package io.github.some_example_name;
// Updated
import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class Main extends Game {

    public SpriteBatch batch;
    public Skin skin;
    MainMenu menu;
    PauseMenu pause;
    LevelScreen level;
    GameScreen1 game1;
    GameScreen2 game2;
    GameScreen3 game3;
    WinScreen wscreen;
    OptionsScreen options;
    SaveScreen saves;
    Texture bg;

    @Override
    public void create() {
        batch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("ui/uiskin.json"));

        menu = new MainMenu(this);
        pause = new PauseMenu(this);
        wscreen = new WinScreen(this);
        level = new LevelScreen(this);
        game1 = new GameScreen1(this);
        game2 = new GameScreen2(this);
        game3 = new GameScreen3(this);
        options = new OptionsScreen(this);
        saves = new SaveScreen(this);
        bg = new Texture("mainmenu.png");

        setScreen(menu);

    }

    @Override
    public void render() {
        super.render();


    }

    @Override
    public void dispose() {
        batch.dispose();
        skin.dispose();
        getScreen().dispose();
    }
}
