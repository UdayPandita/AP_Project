package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;

class MainMenu implements Screen {
    private final Stage stage;
    private final SpriteBatch batch;
    private final Viewport viewport;
    private final OrthographicCamera camera;

    Texture bg;
    Texture text;
    Texture buttons;

    private final AssetManager assetManager;

    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 480;

    public MainMenu(final Main main) {
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);  // Set camera position once here

        stage = new Stage(viewport);
        batch = new SpriteBatch();

        assetManager = new AssetManager();

        assetManager.load("mainmenu.png", Texture.class);
        assetManager.load("text.png", Texture.class);
        assetManager.load("buttons.png", Texture.class);
        assetManager.finishLoading();

        if (assetManager.isLoaded("mainmenu.png")) {
            bg = assetManager.get("mainmenu.png", Texture.class);
        }
        if (assetManager.isLoaded("text.png")) {
            text = assetManager.get("text.png", Texture.class);
        }
        if (assetManager.isLoaded("buttons.png")) {
            buttons = assetManager.get("buttons.png", Texture.class);
        }

        ImageButton playButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttons, 436, 778, 107, 97)));
        ImageButton optionsButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttons, 542, 548, 107, 98)));

        float buttonWidth = 150;
        float buttonHeight = 75;

        playButton.setSize(buttonWidth, buttonHeight);
        optionsButton.setSize(buttonWidth, buttonHeight);

        playButton.setPosition(WORLD_WIDTH / 2 - buttonWidth - 10, WORLD_HEIGHT / 10);
        optionsButton.setPosition(WORLD_WIDTH / 2 + 20, WORLD_HEIGHT / 10);

        stage.addActor(playButton);
        stage.addActor(optionsButton);

        playButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent e, Actor actor) {
                Gdx.app.log("MainMenu", "Play button clicked");
                main.setScreen(main.level);
            }
        });

        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("MainMenu", "Options button clicked");
                main.options.previousScreen = main.menu;
                main.setScreen(main.options);
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        if (bg != null && text != null) {  // Ensure textures are loaded before drawing
            batch.draw(bg, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
            batch.draw(text, 250, WORLD_HEIGHT - 150, 300, 200);
        }
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        assetManager.dispose();
    }
}
