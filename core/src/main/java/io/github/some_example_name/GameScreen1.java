package io.github.some_example_name;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.*;
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
    Steelblock steelblock2;
    Steelblock steelblock3;
    SteelTriangle steeltriangle;
    Steelslab steelslab;
    Steelslab steelslab2;
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
        steelblock2 = new Steelblock();
        steelblock3 = new Steelblock();
        steelslab = new Steelslab();
        steelslab2 = new Steelslab();
        steeltriangle = new SteelTriangle();
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttons, 408, 174, 118, 108)));
        TextButton dummyButton = new TextButton("Dummy", main.skin);
        trajectoryPoints = new Array<>();
        world = new World(new Vector2(0, -4.0f), true); // Gravity pointing down
        debugRenderer = new Box2DDebugRenderer(true, true, true, true, true, true);
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

                    // Calculate the launch force
                    Vector2 launchDirection = slingOrigin.cpy().sub(slingStretch); // Direction of launch
                    float forceMultiplier = 12; // Adjust this value for force strength
                    Vector2 launchForce = launchDirection.scl(forceMultiplier);

                    // Apply the force to the bird
                    if (rdbrd.body != null && rdbrd.body.getLinearVelocity().isZero()) {
                        rdbrd.body.setAwake(true); // Ensure the body is active
                        rdbrd.body.applyForceToCenter(launchForce, true); // Apply the calculated force
                    }

                    slingStretch.set(slingOrigin); // Reset the sling stretch position
                    trajectoryPoints.clear(); // Clear the trajectory
                    return true; // Consume the event to prevent it from reaching the stage
                }
                return false; // Allow the event to propagate to the next input processor (the stage)
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

        // Define and create Box2D bodies for the birds and pigs.
        CircleShape birdShape = new CircleShape();
        birdShape.setRadius(18 / 100f); // 18 pixels converted to Box2D units

// Create Red Bird body at a reasonable height
        rdbrd.body = createBody(world, BodyDef.BodyType.DynamicBody,
            80 / 100f, 120/ 100f, birdShape, 1f, 0.3f, 0.5f); // Place bird at (80px, 200px) in Box2D world

// Create Black Bird body at a reasonable height
        blbrd.body = createBody(world, BodyDef.BodyType.DynamicBody,
            60/ 100f, 120 / 100f, birdShape, 1f, 0.3f, 0.5f); // Place bird at (160px, 200px) in Box2D world

// Create Purple Bird body at a reasonable height
        prbrd.body = createBody(world, BodyDef.BodyType.DynamicBody,
            40 / 100f, 120 / 100f, birdShape, 1f, 0.3f, 0.5f); // Place bird at (240px, 200px) in Box2D world

        birdShape.dispose(); // Dispose the shape after useer use

        CircleShape pigShape = new CircleShape();
        pigShape.setRadius(19.5f / 100f); // 19.5 pixels converted to Box2D units



        armpig.body = createBody(world, BodyDef.BodyType.DynamicBody,
            400 / 100f, 135 / 100f, pigShape, 1f, 5.0f, 0.5f);
        armpig.body.setLinearDamping(2.0f); // Add damping to reduce sliding


        pig.body = createBody(world, BodyDef.BodyType.DynamicBody,
            520 / 100f, 120 / 100f, pigShape, 1f, 1.5f, 0.5f);
        pig.body.setLinearDamping(2.0f);
        pigShape.dispose();

        PolygonShape steelblockShape = new PolygonShape();
        steelblockShape.setAsBox(18 / 100f, 18 / 100f); // S
        steelblock.body = createBody(world, BodyDef.BodyType.DynamicBody,
            500 / 100f, 102 / 100f, steelblockShape, 5f, 0.5f, 0.2f);

        steelblock2.body = createBody(world, BodyDef.BodyType.DynamicBody,
            500 / 100f, 137 / 100f, steelblockShape, 5f, 0.5f, 0.2f);

        steelblock3.body = createBody(world, BodyDef.BodyType.DynamicBody,
            500 / 100f, 169 / 100f, steelblockShape, 5f, 0.5f, 0.2f);
        PolygonShape steelslabShape = new PolygonShape();
        steelslabShape.setAsBox(37 / 100f, 9 / 100f);
        steelslab.body = createBody(world, BodyDef.BodyType.DynamicBody,
            400 / 100f, 100 / 100f, steelslabShape, 5f, 0.8f, 0.0f);

        steelslab2.body = createBody(world, BodyDef.BodyType.DynamicBody,
            400 / 100f, 118 / 100f, steelslabShape, 5f, 0.8f, 0.0f);
        PolygonShape steeltriangleShape = new PolygonShape();
        steeltriangleShape.set(new Vector2[]{
            new Vector2(-18 / 100f, -18 / 100f),
            new Vector2(18 / 100f, -18 / 100f),
            new Vector2(0, 18 / 100f)
        }); // Triangle size matches sprite (36x36 pixels)

// Create a dynamic body for the steeltriangle
        steeltriangle.body = createBody(world, BodyDef.BodyType.DynamicBody,
            500 / 100f, 200 / 100f, steeltriangleShape, 5f, 0.9f, 0.2f);

        steelblockShape.dispose();
        steeltriangleShape.dispose();
        steelslabShape.dispose();


        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(WORLD_WIDTH / 2 / 100f, 100/ 100f); // Positioned at the bottom center of the screen in Box2D units

// Create the body
        Body groundBody = world.createBody(groundBodyDef);

// Define the shape of the ground
        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(WORLD_WIDTH / 2 / 100f, 1 / 100f); // Ground width spans the screen, height of 1 unit
        FixtureDef groundFixtureDef = new FixtureDef();
        groundFixtureDef.shape = groundShape;
        groundFixtureDef.friction = 1.5f; // Increase friction for ground
        groundFixtureDef.restitution = 0.0f; // Ensure no bounce

// Attach the shape to the body
        groundBody.createFixture(groundFixtureDef);
        groundShape.dispose(); // Dispose the shape after creating the fixture


    }

    private Body createBody(World world, BodyDef.BodyType bodyType, float x, float y,
                            Shape shape, float density, float friction, float restitution) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = bodyType;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restitution; // Adjust this for bounciness

        body.createFixture(fixtureDef);
        return body;
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
        // Clear screen and set up the camera
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Rendering the background and objects
        batch.begin();
        batch.draw(bg, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        // Draw building elements and objects as usual
        batch.draw(steelblock.steelblock,
            steelblock.body.getPosition().x * 100 - 18,
            steelblock.body.getPosition().y * 100 - 18,
            36, 36);

        batch.draw(steelblock2.steelblock,
            steelblock2.body.getPosition().x * 100 - 18,
            steelblock2.body.getPosition().y * 100 - 18,
            36, 36);

        batch.draw(steelblock3.steelblock,
            steelblock3.body.getPosition().x * 100 - 18,
            steelblock3.body.getPosition().y * 100 - 18,
            36, 36);

        if (steeltriangle.body != null) {
            batch.draw(steeltriangle.region,
                steeltriangle.body.getPosition().x * 100 - 18,
                steeltriangle.body.getPosition().y * 100 - 18,
                36, 36); // Sprite size
        }
        batch.draw(steelslab.region,
            steelslab.body.getPosition().x * 100 - 37,
            steelslab.body.getPosition().y * 100 - 9,
            74, 18);

        batch.draw(steelslab2.region,
            steelslab2.body.getPosition().x * 100 - 37,
            steelslab2.body.getPosition().y * 100 - 9,
            74, 18);

        // Draw pigs and birds in their correct positions
        if (armpig.body != null) {
            batch.draw(armpig.armourpig,
                armpig.body.getPosition().x * 100 - 19.5f,
                armpig.body.getPosition().y * 100 - 19.5f,
                39, 39); // Sprite size
        }

        if (pig.body != null) {
            batch.draw(pig.pig,
                pig.body.getPosition().x * 100 - 19.5f,
                pig.body.getPosition().y * 100 - 19.5f,
                39, 39); // Sprite size
        }

        // Draw slingshot (static position)
        batch.draw(slingshot, slingOrigin.x - 25, slingOrigin.y - 35, 50, 67);

        // Draw birds in their correct positions
        if (rdbrd.body != null) {
            batch.draw(rdbrd.textureregion,
                rdbrd.body.getPosition().x * 100 -18,
                rdbrd.body.getPosition().y * 100 - 18,
                36, 36); // Sprite size
        }

        if (blbrd.body != null) {
            batch.draw(blbrd.textureregion,
                blbrd.body.getPosition().x * 100 - 18,
                blbrd.body.getPosition().y * 100 - 18,
                36, 36); // Sprite size
        }

        if (prbrd.body != null) {
            batch.draw(prbrd.textureregion,
                prbrd.body.getPosition().x * 100 - 18,
                prbrd.body.getPosition().y * 100 - 18,
                36, 36); // Sprite size
        }

        // Draw trajectory as a dotted line
        for (Vector2 point : trajectoryPoints) {
            batch.draw(dottexture, point.x + 5, point.y + 10, 3, 3);
        }

        batch.end();
        debugRenderer.render(world, camera.combined.cpy().scl(100)); // Adjust scaling factor for world-to-pixel conversion

        // Render Box2D debug shapes (optional)

        // Update the Box2D world step
        world.step(1 / 60f, 6, 2); // Fixed timestep

        // Draw stage UI elements
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
    public void hide() {
        // Clean up Box2D world by destroying all bodies
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies); // Retrieve all bodies from the world
        for (Body body : bodies) {
            world.destroyBody(body);
        }
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
        world.dispose(); // Dispose Box2D world
        debugRenderer.dispose(); // D
    }
}
