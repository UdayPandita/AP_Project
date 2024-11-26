package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class WinScreen implements Screen {
    private final Stage stage;
    Screen previousScreen;
    Texture template;
    Texture stars;
    Texture level;
    Texture bg;
    Texture back;


    SpriteBatch batch;

    Image LevelImage;
    ImageButton backButton;

    private final int WORLD_WIDTH = 800;
    private final int WORLD_HEIGHT = 480;

    public WinScreen(final Main main) {

        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        batch = new SpriteBatch();

        bg = new Texture(Gdx.files.internal("background3.png"));
        template = new Texture(Gdx.files.internal("Score.png"));
        stars = new Texture(Gdx.files.internal("Stars.png"));
        level = new Texture(Gdx.files.internal("Levelcompleted.png"));
        back = new Texture(Gdx.files.internal("back.png"));

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        backButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(back)));
        backButton.setSize(80, 80);
        LevelImage = new Image(level);
        LevelImage.setSize(200, 50);


        table.top();
        table.add(LevelImage).padTop(40).padBottom(40).colspan(4).center().width(200);
        table.row();

        table.add(backButton).padTop(170).colspan(4).center().bottom().width(80).height(80);




       backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("SaveScreen", "Options button clicked");
                if (previousScreen instanceof GameScreen1) {
                    ((GameScreen1) previousScreen).False();
                }
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
        Gdx.gl.glClearColor(0, 0, 0, 0.8f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.getViewport().apply();
        batch.setProjectionMatrix(stage.getCamera().combined);

        batch.begin();
        batch.draw(bg, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.draw(new TextureRegion(template,125,60,166,60), 357, 250,90,92);
        batch.draw(new TextureRegion(stars,180,653,191,182),300,200,40,42);
        batch.draw(new TextureRegion(stars,180,653,191,182),380,200,40,42);
        batch.draw(new TextureRegion(stars,180,653,191,182),460,200,40,42);

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
        level.dispose();
        template.dispose();
        stars.dispose();
        back.dispose();
        bg.dispose();

    }
}
