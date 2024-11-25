package io.github.some_example_name;

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
import Birds.*;
import Pigs.*;
import Elements.*;
public class GameScreen2 implements Screen {
    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 480;
    Texture buttons;
    private final Stage stage;
    private final SpriteBatch batch;
    private Texture bg, tnt, textures, textures2, slingshot, bird;
    private TextureRegion region, brd;
    private OrthographicCamera camera;
    private final Viewport viewport;
    Redbird rdbrd;
    Blackbird blbrd;
    PurpleBird prbrd;
    Armourpig armpig;
    Kingpig kngpig;
    Chiefpig chfpig;
    Steelblock steelblock;
    public GameScreen2(final Main main) {
        this.batch = main.batch;
        buttons = new Texture("buttons.png");
        rdbrd = new Redbird();
        blbrd = new Blackbird();
        prbrd = new PurpleBird();
        armpig = new Armourpig();
        kngpig = new Kingpig();
        chfpig = new Chiefpig();
        steelblock = new Steelblock();
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
                main.pause.previousScreen = main.game2;
                main.setScreen(main.pause);
            }
        });
        dummyButton.addListener(new ChangeListener(){
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("GameScreen", "Dummy button clicked");
                main.wscreen.previousScreen = main.game2;
                main.setScreen(main.wscreen);

            }

        });
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        bg = new Texture(Gdx.files.internal("background2.png"));
        tnt = new Texture(Gdx.files.internal("TNT.png"));
        textures = new Texture(Gdx.files.internal("buildingelements.png"));
        textures2 = new Texture(Gdx.files.internal("character.png"));
        slingshot = new Texture(Gdx.files.internal("slingshot.png"));
        bird = new Texture(Gdx.files.internal("birds.png"));

        region = new TextureRegion(textures, 1, 2, 37, 37);
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

        batch.draw(steelblock.steelblock, 500, 152);
        batch.draw(steelblock.steelblock,520,189);
        batch.draw(steelblock.steelblock,540,226);
        batch.draw(steelblock.steelblock,578,226);
        batch.draw(steelblock.steelblock,602,189);
        batch.draw(steelblock.steelblock, 620, 152);
        batch.draw(chfpig. textureregion,538,152,39,37);
        batch.draw(armpig. textureregion,578,152,39,37);
        batch.draw(kngpig. textureregion,555,260,50,52);

        batch.draw(tnt,540 + (float) (39 - 37) / 2 + 3,224+ (float) (39 - 37) / 2 + 4, 39 * 0.8f, 37 * 0.8f);
        batch.draw(tnt,578 + (float) (39 - 37) / 2 + 3,224+ (float) (39 - 37) / 2 + 4, 39 * 0.8f, 37 * 0.8f);

        batch.draw(slingshot, 130, 152, 50, 67);
        batch.draw(rdbrd.textureregion, 100, 152, 42, 44);

        batch.draw(prbrd.textureregion, 44, 152, 42, 44);
        batch.draw(blbrd.textureregion, 2, 152, 42, 44);

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
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        stage.dispose();
        bg.dispose();
        tnt.dispose();
        textures.dispose();
        textures2.dispose();
        slingshot.dispose();
        bird.dispose();
    }
}
