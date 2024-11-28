package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.OrthographicCamera;

class PauseMenu implements Screen {
    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 480;
    private final Stage stage;
    private final OrthographicCamera camera;
    private final Viewport viewport;
    public Screen previousScreen;
    public SaveData saveData;
    private final Texture bg;
    private final SpriteBatch batch;

    public PauseMenu(final Main main) {
        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);
        stage = new Stage(viewport);
        batch = new SpriteBatch();
        bg = new Texture(Gdx.files.internal("background4.png"));

        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label pauseLabel = new Label("Game is Paused", main.skin);
        pauseLabel.setColor(0, 0, 0, 1);
        pauseLabel.setFontScale(2);

        TextButton resumeButton = new TextButton("Resume", main.skin);
        TextButton restartButton = new TextButton("Restart Level", main.skin);
        TextButton saveButton = new TextButton("Save Game", main.skin);
        TextButton optionsButton = new TextButton("Options", main.skin);
        TextButton mainMenuButton = new TextButton("Main Menu", main.skin);

        table.add(pauseLabel).padBottom(40);
        table.row();
        table.add(resumeButton).padBottom(10).width(150);
        table.row();
        table.add(restartButton).padBottom(10).width(150);
        table.row();
        table.add(saveButton).padBottom(10).width(150);
        table.row();
        table.add(optionsButton).padBottom(10).width(150);
        table.row();
        table.add(mainMenuButton).width(150);

        resumeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("PauseMenu", "Resume button clicked");
                if (previousScreen instanceof GameScreen1) {
                    previousScreen = new GameScreen1(main);
                    ((GameScreen1) previousScreen).load(saveData);
                }
                main.setScreen(previousScreen);
            }
        });

        restartButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("PauseMenu", "Restart button clicked");
                if (previousScreen instanceof GameScreen1) {
                    ((GameScreen1) previousScreen).False();
                }
                main.setScreen(previousScreen);
            }
        });

        saveButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("PauseMenu", "Save button clicked");
                // TODO: Implement saving game state functionality
            }
        });

        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("PauseMenu", "Options button clicked");
                main.options.previousScreen = PauseMenu.this;
                main.setScreen(main.options);
            }
        });

        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("PauseMenu", "Main Menu button clicked");
                main.setScreen(main.menu);
            }
        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 0.8f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        batch.draw(bg, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
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
        bg.dispose();
    }
}
