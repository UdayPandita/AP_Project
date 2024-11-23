package io.github.some_example_name;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import Birds.*;
import Pigs.*;
import Elements.*;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.math.Vector2;

public class GameScreen1 implements Screen {
    private final Main main;
    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 480;
    Texture buttons;
    private final Stage stage;
    private final SpriteBatch batch;
    private Texture bg, tnt, textures, textures2, slingshot, bird;
    private TextureRegion region, character, brd;
    private OrthographicCamera camera;
    private final Viewport viewport;
    private Texture dottexture;
    Redbird rdbrd;
    Blackbird blbrd;
    PurpleBird prbrd;
    Armourpig armpig;
    Pig pig;
    Steelblock steelblock;
    SteelTriangle steeltriangle;
    Steelslab steelslab;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Vector2 slingOrigin = new Vector2(125, 130); // Slingshot origin position
    private Vector2 slingStretch = new Vector2(slingOrigin); // Stretching position
    private boolean dragging = false; // To check if dragging is happening
    private Array<Vector2> trajectoryPoints; // Array to store trajectory points for dotted line

    public GameScreen1(final Main main) {
        this.main = main;
        this.batch = main.batch;
        buttons = new Texture("buttons.png");
        rdbrd = new Redbird();
        blbrd = new Blackbird();
        prbrd = new PurpleBird();
        armpig = new Armourpig();
        pig = new Pig();
        steelblock = new Steelblock();
        steelslab = new Steelslab();
        steeltriangle = new SteelTriangle();
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttons, 408, 174, 118, 108)));
        TextButton dummyButton = new TextButton("Dummy", main.skin);
        trajectoryPoints = new Array<>();
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        this.stage = new Stage(viewport, batch);
        Pixmap pixmap = new Pixmap(3, 3, Pixmap.Format.RGBA8888);  // Smaller dot size
        pixmap.setColor(1, 1, 1, 1);  // Set the color to white (or any color you want)
        pixmap.fillRectangle(0, 0, 3, 3);  // Fill the pixmap with a small white square
        dottexture = new Texture(pixmap);  // Create the texture from the pixmap
        pixmap.dispose();
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
                main.pause.previousScreen = main.game1;
                main.setScreen(main.pause);
            }
        });
        dummyButton.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("GameScreen", "Dummy button clicked");
                main.wscreen.previousScreen = main.game1;
                main.setScreen(main.wscreen);
            }
        });
    }

    @Override
    public void show() {
        // Create an InputMultiplexer to handle multiple input processors
        InputMultiplexer multiplexer = new InputMultiplexer();

        // Add the custom InputAdapter for dragging as the first processor (it will handle touch events first)
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector2 touchPos = viewport.unproject(new Vector2(screenX, screenY));

                // Check if the touch is within the slingshot's base area
                if (touchPos.dst(slingOrigin) <= 50) {  // 50 is the radius for the slingshot area
                    dragging = true;
                    return true;  // Return true to consume the event and prevent it from reaching the stage
                }

                return false;  // Return false to allow the event to propagate to the next input processor (the stage)
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (dragging) {
                    Vector2 touchPos = viewport.unproject(new Vector2(screenX, screenY));

                    // Update the sling (not slingshot) position for dragging
                    slingStretch.set(touchPos);

                    // Limit the drag distance (for example, don't let the bird go too far)
                    if (slingStretch.dst(slingOrigin) > 75) {
                        slingStretch.set(slingOrigin).add(touchPos.sub(slingOrigin).nor().scl(75));
                    }

                    // Update the trajectory while dragging
                    updateTrajectory();
                    return true;  // Consume the event to prevent it from reaching the stage
                }
                return false;  // Allow the event to propagate to the next input processor (the stage)
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (dragging) {
                    dragging = false; // Stop dragging the slingshot
                    slingStretch.set(slingOrigin); // Reset the sling stretch position
                    trajectoryPoints.clear(); // Clear the trajectory
                    return true;  // Consume the event to prevent it from reaching the stage
                }
                return false;  // Allow the event to propagate to the next input processor (the stage)
            }
        });

        // Add the stage as the second processor (UI interaction should be handled after the dragging logic)
        multiplexer.addProcessor(stage);

        // Set the input multiplexer as the input processor
        Gdx.input.setInputProcessor(multiplexer);

        // Load assets and set up your buttons and stage as usual
        bg = new Texture(Gdx.files.internal("background.png"));
        tnt = new Texture(Gdx.files.internal("TNT.png"));
        textures = new Texture(Gdx.files.internal("buildingelements.png"));
        textures2 = new Texture(Gdx.files.internal("pig.png"));
        slingshot = new Texture(Gdx.files.internal("slingshot.png"));
        bird = new Texture(Gdx.files.internal("birds.png"));

        region = new TextureRegion(textures, 1, 2, 37, 37);
        character = new TextureRegion(textures2, 60, 195, 40, 39);
        brd = new TextureRegion(bird, 929, 888, 72, 69);

        camera = (OrthographicCamera) viewport.getCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        // Create and configure the pause and dummy buttons
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttons, 408, 174, 118, 108)));
        TextButton dummyButton = new TextButton("Dummy", main.skin);

        // Set listeners for the buttons
        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("GameScreen", "Pause button clicked");
                main.pause.previousScreen = main.game1;
                main.setScreen(main.pause);
            }
        });

        dummyButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("GameScreen", "Dummy button clicked");
                main.wscreen.previousScreen = main.game1;
                main.setScreen(main.wscreen);
            }
        });

        // Add buttons to the layout
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.top().right();
        table.add(dummyButton).pad(10).width(80);
        table.add(pauseButton).pad(10).width(80);
    }



    private void updateTrajectory() {
        trajectoryPoints.clear();
        Vector2 direction = slingStretch.cpy().sub(slingOrigin);  // Direction from slingshot origin

        // Generate a series of points to form the trajectory (like a dotted line)
        int steps = 15; // Number of trajectory points

        for (int i = 1; i <= steps; i++) {
            float t = (i * (float) 1.5) / (float) steps;
            float x = slingOrigin.x + direction.x * t;
            float y = slingOrigin.y + direction.y * t;
            trajectoryPoints.add(new Vector2(x, y));
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(bg, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        batch.draw(steelblock.steelblock, 500, 102);
        batch.draw(steelblock.steelblock, 500, 137);
        batch.draw(steelblock.steelblock, 500, 169);
        batch.draw(steeltriangle.region, 500, 200);
        batch.draw(steelslab.region, 400, 100);
        batch.draw(steelslab.region, 400, 118);
        batch.draw(armpig.armourpig, 400, 135, 39 * 0.9f, 35 * 0.9f);

        batch.draw(tnt, 500 + (float) (39 - 37) / 2 + 3, 135 + (float) (39 - 37) / 2 + 2, 39 * 0.8f, 37 * 0.8f);
        batch.draw(pig.pig, 500 + (float) (39 - 37) / 2 + 3, 167 + (float) (39 - 37) / 2 + 5, 39 * 0.8f, 35 * 0.8f);

        // Draw slingshot (static position)
        batch.draw(slingshot, slingOrigin.x - 25, slingOrigin.y - 35, 50, 67);
        batch.draw(rdbrd.redbird, 80, 102, 39, 37);
        batch.draw(prbrd.purplebird, 40, 102, 39, 37);
        batch.draw(blbrd.blackbird, 2, 102, 39, 37);
        // Draw trajectory as a dotted line
        for (Vector2 point : trajectoryPoints) {

            batch.draw(dottexture, point.x + 5, point.y + 10, 3, 3);
            // Small dot for trajectory
        }

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
