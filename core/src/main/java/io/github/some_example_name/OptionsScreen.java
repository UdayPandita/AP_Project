package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class OptionsScreen implements Screen {
    private final Stage stage;
    private boolean soundOn = true;
    public Screen previousScreen;

    public OptionsScreen(final Main main) {
        stage = new Stage(new ScreenViewport());

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        Label optionsLabel = new Label("Options", main.skin);
        optionsLabel.setFontScale(2);

        TextButton soundButton = new TextButton("Sound: ON", main.skin);
        TextButton loadButton = new TextButton("Load Save", main.skin);
        TextButton backButton = new TextButton("Back", main.skin);

        table.add(optionsLabel).padBottom(40);
        table.row();
        table.add(soundButton).padBottom(10).width(150);
        table.row();
        table.add(loadButton).padBottom(10).width(150);
        table.row();
        table.add(backButton).width(150);

        soundButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                soundOn = !soundOn;
                soundButton.setText(soundOn ? "Sound: ON" : "Sound: OFF");
                Gdx.app.log("OptionsScreen", "Sound toggled: " + (soundOn ? "ON" : "OFF"));
                // TODO: Implement actual sound toggle logic
            }
        });

        loadButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("PauseMenu", "Load button clicked");
                main.setScreen(main.saves);
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("OptionsScreen", "Back button clicked");
                main.setScreen(previousScreen);
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

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
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
    }
}
