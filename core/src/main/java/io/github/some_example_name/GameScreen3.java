package io.github.some_example_name;

import Birds.Blackbird;
import Birds.PurpleBird;
import Birds.Redbird;
import Elements.*;
import Pigs.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
public class GameScreen3 implements Screen {

    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 480;
    Texture buttons;
    private final Stage stage;
    private final SpriteBatch batch;
    private Texture bg, tnt, textures, textures2, slingshot, bird, pig;

    private TextureRegion region, brd;
    private OrthographicCamera camera;
    private final Viewport viewport;
    Redbird rdbrd;
    Blackbird blbrd;
    PurpleBird prbrd;
    Armourpig armpig;
    Kingpig kngpig;
    Chiefpig chfpig;
    Pig pg;
    Minipig minipig;
    Iceblock iceblock;
    Steelslab steelslab;
    Icerod icerod;
    Icecube icecube;
    Steelbrick steelbrick;
    Woodblock woodblock;
    public GameScreen3(final Main main) {
        this.batch = main.batch;
        buttons = new Texture("buttons.png");
        rdbrd = new Redbird();
        blbrd = new Blackbird();
        prbrd = new PurpleBird();
        armpig = new Armourpig();
        kngpig = new Kingpig();
        chfpig = new Chiefpig();
        pg = new Pig();
        minipig = new Minipig();
        iceblock = new Iceblock();
        steelslab = new Steelslab();
        icerod = new Icerod();
        steelbrick = new Steelbrick();
        woodblock = new Woodblock();
        icecube = new Icecube();
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttons, 408, 174, 118, 108)));
        TextButton dummyButton=new TextButton("Dummy", main.skin);

        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        this.stage = new Stage(viewport, batch);

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.top().right();
        table.add(dummyButton).pad(10).width(80);
        table.add(pauseButton).pad(10).width(80);

        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("GameScreen", "Pause button clicked");
                main.pause.previousScreen = main.game3;
                main.setScreen(main.pause);
            }
        });
        dummyButton.addListener(new ChangeListener(){
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("GameScreen", "Dummy button clicked");
                main.wscreen.previousScreen = main.game3;
                main.setScreen(main.wscreen);

            }

        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        bg = new Texture(Gdx.files.internal("background3.png"));
        tnt = new Texture(Gdx.files.internal("TNT.png"));
        textures = new Texture(Gdx.files.internal("buildingelements.png"));
        textures2 = new Texture(Gdx.files.internal("character.png"));
        slingshot = new Texture(Gdx.files.internal("slingshot.png"));
        bird = new Texture(Gdx.files.internal("birds.png"));
        pig = new Texture(Gdx.files.internal("pig.png"));


        region = new TextureRegion(textures, 324, 0, 39, 41);
        brd = new TextureRegion(bird, 929, 888, 72, 69);


        camera = (OrthographicCamera) viewport.getCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();


        batch.draw(bg, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);


        batch.draw(iceblock.region, 500, 63);
        batch.draw(iceblock.region, 500, 100);
        batch.draw(iceblock.region, 500, 137);
        batch.draw(iceblock.region, 500, 174);
        batch.draw(iceblock.region, 538, 63);
        batch.draw(iceblock.region, 538, 100);
        batch.draw(iceblock.region, 538, 137);
        batch.draw(iceblock.region, 538, 174);

        batch.draw(steelslab.region, 520, 213);
        batch.draw(steelslab.region, 582, 63);
        batch.draw(steelslab.region, 670, 63);
        batch.draw(steelslab.region, 755, 63);
        batch.draw(woodblock.region, 605, 80);
        batch.draw(woodblock.region, 6707*0.1f, 80);
        batch.draw(woodblock.region, 735, 80);
        batch.draw(steelbrick.region, 630, 117);
        batch.draw(steelbrick.region, 682, 115);
        batch.draw(steelbrick.region, 728, 118);
        batch.draw(icecube.region, 637, 128);
        batch.draw(icecube.region, 682, 126);
        batch.draw(icecube.region, 720, 128);
        batch.draw(icerod.region, 642, 147);
        batch.draw(kngpig. textureregion, 665, 152, 50, 52);
        batch.draw(pg.textureregion, 455, 63,30,32);
        batch.draw(minipig. textureregion, 640, 63);
        batch.draw(minipig. textureregion, 713, 63);
        batch.draw(chfpig. textureregion, 605 + (float) (39 - 37) / 2 + 3, 80 + (float) (39 - 37) / 2 + 4, 39 * 0.8f, 37 * 0.8f);
        batch.draw(chfpig. textureregion, 672 + (float) (39 - 37) / 2 + 3, 80 + (float) (39 - 37) / 2 + 4, 39 * 0.8f, 37 * 0.8f);
        batch.draw(chfpig. textureregion, 735 + (float) (39 - 37) / 2 + 3, 80 + (float) (39 - 37) / 2 + 4, 39 * 0.8f, 37 * 0.8f);

        batch.draw(tnt, 500 + (float) (39 - 37) / 2 + 3, 137 + (float) (39 - 37) / 2 + 4, 39 * 0.8f, 37 * 0.8f);
        batch.draw(tnt, 538 + (float) (39 - 37) / 2 + 3, 137 + (float) (39 - 37) / 2 + 4, 39 * 0.8f, 37 * 0.8f);

        batch.draw(slingshot, 138, 63, 50, 67);
        batch.draw(brd, 88, 63, 34, 36);

        batch.draw(prbrd.textureregion, 50, 63, 32, 34);
        batch.draw(blbrd.textureregion, 125, 63, 42, 44);
        batch.draw(rdbrd.textureregion, 16, 63, 34, 36);
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        stage.dispose();
        bg.dispose();
        tnt.dispose();
        textures.dispose();
        textures2.dispose();
        slingshot.dispose();
        bird.dispose();
        pig.dispose();
    }
}
