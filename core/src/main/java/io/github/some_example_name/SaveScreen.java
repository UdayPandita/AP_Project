package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class SaveScreen implements Screen {
    private final Stage stage;
    Texture bg;
    Texture load;
    Texture sv1;
    Texture sv2;
    Texture sv3;
    Texture sv4;
    Texture back;
    SpriteBatch batch;

    Image loadImage;
    ImageButton save1Button, save2Button, save3Button, save4Button, optionsButton;

    private final int WORLD_WIDTH = 800;
    private final int WORLD_HEIGHT = 480;

    public SaveScreen(final Main main) {

        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        batch = new SpriteBatch();

        bg = new Texture(Gdx.files.internal("background4.png"));
        load = new Texture(Gdx.files.internal("load.png"));
        sv1 = new Texture(Gdx.files.internal("Save1.png"));
        sv2 = new Texture(Gdx.files.internal("Save2.png"));
        sv3 = new Texture(Gdx.files.internal("Save3.png"));
        sv4 = new Texture(Gdx.files.internal("Save4.png"));
        back = new Texture(Gdx.files.internal("back.png"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        loadImage = new Image(load);
        loadImage.setSize(200, 50);

        save1Button = new ImageButton(new TextureRegionDrawable(new TextureRegion(sv1)));
        save1Button.setSize(100, 100);

        save2Button = new ImageButton(new TextureRegionDrawable(new TextureRegion(sv2)));
        save2Button.setSize(100, 100);

        save3Button = new ImageButton(new TextureRegionDrawable(new TextureRegion(sv3)));
        save3Button.setSize(100, 100);

        save4Button = new ImageButton(new TextureRegionDrawable(new TextureRegion(sv4)));
        save4Button.setSize(100, 100);

        optionsButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(back)));
        optionsButton.setSize(80, 80);

        table.top();
        table.add(loadImage).padTop(20).padBottom(40).colspan(4).center().width(200);
        table.row();

        table.add(save1Button).pad(10).padLeft(20).expandX().width(100).height(100);
        table.add(save2Button).pad(10).expandX().width(100).height(100);
        table.add(save3Button).pad(10).expandX().width(100).height(100);
        table.add(save4Button).pad(10).padRight(20).expandX().width(100).height(100);

        table.row();
        table.add(optionsButton).padTop(100).colspan(4).center().bottom().width(80).height(80);


        save1Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("SaveScreen", "Save 1 button clicked");
                // Load save 1
            }
        });

        save2Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("SaveScreen", "Save 2 button clicked");
                // Load save 2
            }
        });

        save3Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("SaveScreen", "Save 3 button clicked");
                // Load save 3
            }
        });

        save4Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("SaveScreen", "Save 4 button clicked");
                // Load save 4
            }
        });

        optionsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("SaveScreen", "Options button clicked");
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
        Gdx.gl.glClearColor(0, 0, 0, 0.8f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getViewport().apply();
        batch.setProjectionMatrix(stage.getCamera().combined);

        batch.begin();
        batch.draw(bg, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.end();

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
        batch.dispose();
        bg.dispose();
        load.dispose();
        sv1.dispose();
        sv2.dispose();
        sv3.dispose();
        sv4.dispose();
        back.dispose();
    }
}
