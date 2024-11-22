package io.github.some_example_name;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import Birds.*;
import Pigs.*;
import Elements.*;

public class GameScreen1 implements Screen, InputProcessor {
    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 480;
    private static final float PPM = 100; // Pixels per meter for Box2D scaling
    private static final float SLING_FORCE_MULTIPLIER = 100f;

    private final SpriteBatch batch;
    private final Stage stage;

    private OrthographicCamera camera;
    private final Viewport viewport;

    private World world;
    private Box2DDebugRenderer debugRenderer;

    // Sprite objects
    private Redbird rdbrd;
    private Blackbird blbrd;
    private PurpleBird prbrd;
    private Armourpig armpig;
    private Pig pig;
    private Steelblock steelblock;
    private SteelTriangle steeltriangle;
    private Steelslab steelslab;

    // Physics bodies
    private Body redBirdBody, blackBirdBody, purpleBirdBody;
    private Body currentBirdBody;
    private Body slingshotBody;

    private Body pigBody, armourPigBody;
    private Body steelBlockBody, steelSlabBody, steelTriangleBody;

    // State tracking
    private boolean isDragging = false;
    private Vector2 slingStartPos;
    private Vector2 dragPos;

    public GameScreen1(final Main main) {
        this.batch = main.batch;

        // Initialize sprites
        rdbrd = new Redbird();
        blbrd = new Blackbird();
        prbrd = new PurpleBird();
        armpig = new Armourpig();
        pig = new Pig();
        steelblock = new Steelblock();
        steelslab = new Steelslab();
        steeltriangle = new SteelTriangle();

        this.viewport = new FitViewport(WORLD_WIDTH / PPM, WORLD_HEIGHT / PPM);
        this.stage = new Stage(viewport, batch);

        // Initialize Box2D world
        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();

        // Set up camera
        camera = (OrthographicCamera) viewport.getCamera();
        camera.position.set(WORLD_WIDTH / 2 / PPM, WORLD_HEIGHT / 2 / PPM, 0);

        createPhysicsObjects();

        // Input processor for dragging birds
        Gdx.input.setInputProcessor(this);
    }

    private void createPhysicsObjects() {
        // Slingshot
        BodyDef slingshotDef = new BodyDef();
        slingshotDef.type = BodyDef.BodyType.StaticBody;
        slingshotDef.position.set(100 / PPM, 120 / PPM);
        slingshotBody = world.createBody(slingshotDef);

        PolygonShape slingshotShape = new PolygonShape();
        slingshotShape.setAsBox(15 / PPM, 25 / PPM);
        slingshotBody.createFixture(slingshotShape, 0.0f);
        slingshotShape.dispose();

        // Birds
        redBirdBody = createDynamicBody(100, 120, 10);
        blackBirdBody = createDynamicBody(40, 120, 10);
        purpleBirdBody = createDynamicBody(20, 120, 10);

        // Pigs
        pigBody = createStaticBody(500, 120, 15);
        armourPigBody = createStaticBody(400, 120, 15);

        // Elements
        steelBlockBody = createStaticBody(500, 100, 20, 40);
        steelSlabBody = createStaticBody(450, 100, 40, 10);
        steelTriangleBody = createStaticBody(480, 140, 20, 20);

        // Set the first bird on the slingshot
        currentBirdBody = redBirdBody;
        slingStartPos = new Vector2(slingshotBody.getPosition());
    }

    private Body createDynamicBody(float x, float y, float radius) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x / PPM, y / PPM);
        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius / PPM);
        body.createFixture(shape, 1.0f);
        shape.dispose();

        return body;
    }

    private Body createStaticBody(float x, float y, float width, float height) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x / PPM, y / PPM);
        Body body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2 / PPM, height / 2 / PPM);
        body.createFixture(shape, 1.0f);
        shape.dispose();

        return body;
    }

    private Body createStaticBody(float x, float y, float radius) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x / PPM, y / PPM);
        Body body = world.createBody(bodyDef);

        CircleShape shape = new CircleShape();
        shape.setRadius(radius / PPM);
        body.createFixture(shape, 1.0f);
        shape.dispose();

        return body;
    }

    @Override
    public void render(float delta) {
        // Step the physics world
        world.step(1 / 60f, 6, 2);

        // Clear screen
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        // Render sprites
        batch.begin();

        // Birds
        batch.draw(rdbrd.redbird, redBirdBody.getPosition().x * PPM - 10, redBirdBody.getPosition().y * PPM - 10, 20, 20);
        batch.draw(blbrd.blackbird, blackBirdBody.getPosition().x * PPM - 10, blackBirdBody.getPosition().y * PPM - 10, 20, 20);
        batch.draw(prbrd.purplebird, purpleBirdBody.getPosition().x * PPM - 10, purpleBirdBody.getPosition().y * PPM - 10, 20, 20);

        // Pigs
        batch.draw(pig.pig, pigBody.getPosition().x * PPM - 15, pigBody.getPosition().y * PPM - 15, 30, 30);
        batch.draw(armpig.armourpig, armourPigBody.getPosition().x * PPM - 15, armourPigBody.getPosition().y * PPM - 15, 30, 30);

        // Elements
        batch.draw(steelblock.steelblock, steelBlockBody.getPosition().x * PPM - 20, steelBlockBody.getPosition().y * PPM - 20, 40, 40);
        batch.draw(steelslab.region, steelSlabBody.getPosition().x * PPM - 20, steelSlabBody.getPosition().y * PPM - 5, 40, 10);
        batch.draw(steeltriangle.region, steelTriangleBody.getPosition().x * PPM - 10, steelTriangleBody.getPosition().y * PPM - 10, 20, 20);

        batch.end();

        // Render physics debug lines
        debugRenderer.render(world, camera.combined);

        // Handle dragging logic
        if (isDragging && currentBirdBody != null) {
            currentBirdBody.setTransform(dragPos.x, dragPos.y, 0);
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (currentBirdBody != null) {
            Vector2 worldTouch = viewport.unproject(new Vector2(screenX, screenY)).scl(PPM);
            if (currentBirdBody.getFixtureList().get(0).testPoint(worldTouch)) {
                isDragging = true;
                dragPos = currentBirdBody.getPosition();
            }
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (isDragging) {
            dragPos = viewport.unproject(new Vector2(screenX, screenY)).scl(PPM);
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (isDragging) {
            isDragging = false;

            // Apply force to launch bird
            Vector2 releasePos = currentBirdBody.getPosition();
            Vector2 force = slingStartPos.cpy().sub(releasePos).scl(SLING_FORCE_MULTIPLIER);
            currentBirdBody.setLinearVelocity(force);

            // Switch to next bird
            if (currentBirdBody == redBirdBody) {
                currentBirdBody = blackBirdBody;
            } else if (currentBirdBody == blackBirdBody) {
                currentBirdBody = purpleBirdBody;
            } else {
                currentBirdBody = null; // No more birds
            }
        }
        return true;
    }
    @Override
    public void show() {
        // Set the input processor for handling touch events
        Gdx.input.setInputProcessor(this);
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
        world.dispose();
        debugRenderer.dispose();
    }
    @Override public boolean keyDown(int keycode) { return false; }
    @Override public boolean keyUp(int keycode) { return false; }
    @Override public boolean keyTyped(char character) { return false; }
    @Override public boolean mouseMoved(int screenX, int screenY) { return false; }
    @Override public boolean scrolled(float amountX, float amountY) { return false; }
    // Other overridden methods are unchanged
    @Override public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        // Handle touch cancellation if necessary
        isDragging = false;  // Ensure dragging is reset when the touch is canceled
        return true;
    }
}
