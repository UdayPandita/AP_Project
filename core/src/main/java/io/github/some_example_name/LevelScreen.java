package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class LevelScreen implements Screen {
    private final Stage stage;
    Texture bg;
    Texture select;
    Texture lvl1;
    Texture lvl2;
    Texture lvl3;
    Texture back;
    SpriteBatch batch;

    Image selectImage;
    ImageButton level1Button, level2Button, level3Button, mainMenuButton;

    private final int WORLD_WIDTH = 800;
    private final int WORLD_HEIGHT = 480;


    private final float SELECT_Y = 390;
    private final float LEVEL1_Y = 280;
    private final float LEVEL2_Y = 180;
    private final float LEVEL3_Y = 80;
    private final float BACK_Y = 20;
    private int flag = 0;

    public LevelScreen(final Main main) {

        stage = new Stage(new FitViewport(WORLD_WIDTH, WORLD_HEIGHT));
        batch = new SpriteBatch();


        bg = new Texture(Gdx.files.internal("background4.png"));
        select = new Texture(Gdx.files.internal("select.png"));
        lvl1 = new Texture(Gdx.files.internal("Level1.png"));
        lvl2 = new Texture(Gdx.files.internal("Level2.png"));
        lvl3 = new Texture(Gdx.files.internal("Level3.png"));
        back = new Texture(Gdx.files.internal("back.png"));


        selectImage = new Image(select);
        selectImage.setSize(200, 50);
        stage.addActor(selectImage);

        level1Button = new ImageButton(new TextureRegionDrawable(new TextureRegion(lvl1)));
        level1Button.setSize(100, 100);
        stage.addActor(level1Button);

        level2Button = new ImageButton(new TextureRegionDrawable(new TextureRegion(lvl2)));
        level2Button.setSize(100, 100);
        stage.addActor(level2Button);

        level3Button = new ImageButton(new TextureRegionDrawable(new TextureRegion(lvl3)));
        level3Button.setSize(100, 100);
        stage.addActor(level3Button);

        mainMenuButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(back)));
        mainMenuButton.setSize(80, 80);
        stage.addActor(mainMenuButton);


        level1Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("LevelScreen", "Level 1 button clicked");

                main.setScreen(new GameScreen1(main));
            }
        });

        level2Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("LevelScreen", "Level 2 button clicked");
                main.setScreen(new GameScreen2(main));
            }
        });

        level3Button.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("LevelScreen", "Level 3 button clicked");
                if (flag == 0) {
                    main.setScreen(main.game3);
                }
                else {
                    main.setScreen(new GameScreen3(main));
                }
            }
        });

        mainMenuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                Gdx.app.log("LevelScreen", "Main Menu button clicked");
                main.setScreen(main.menu);
            }
        });


        alignActors();
    }

    private void alignActors() {

        float centerX = WORLD_WIDTH / 2;


        selectImage.setPosition(centerX - selectImage.getWidth() / 2, SELECT_Y);
        level1Button.setPosition(centerX - level1Button.getWidth() / 2, LEVEL1_Y);
        level2Button.setPosition(centerX - level2Button.getWidth() / 2, LEVEL2_Y);
        level3Button.setPosition(centerX - level3Button.getWidth() / 2, LEVEL3_Y);
        mainMenuButton.setPosition(centerX - mainMenuButton.getWidth() / 2, BACK_Y);
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
        select.dispose();
        lvl1.dispose();
        lvl2.dispose();
        lvl3.dispose();
        back.dispose();
    }
}
