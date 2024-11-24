package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class GameScreen2 implements Screen {
    private final Main main;
    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 480;
    private final Stage stage;
    private final SpriteBatch batch;
    private Texture bg, textures, bird;
    private TextureRegion brd, steelBlockRegion;
    private OrthographicCamera camera;
    private final Viewport viewport;
    private Texture dottexture;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Vector2 slingOrigin = new Vector2(100, 150); // Slingshot origin
    private Vector2 slingStretch = new Vector2(slingOrigin);
    private boolean dragging = false;
    private Array<Vector2> trajectoryPoints;

    public GameScreen2(final Main main) {
        this.main = main;
        this.batch = main.batch;

        // Initialize Box2D world
        world = new World(new Vector2(0, -9.8f), true);
        debugRenderer = new Box2DDebugRenderer();

        // Initialize viewport and camera
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        camera = (OrthographicCamera) viewport.getCamera();
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
        camera.update();

        // Initialize stage
        this.stage = new Stage(viewport, batch);

        // Load textures
        bg = new Texture("background2.png");
        textures = new Texture("buildingelements.png");
        bird = new Texture("birds.png");

        // Define regions
        brd = new TextureRegion(bird, 929, 888, 72, 69);
        steelBlockRegion = new TextureRegion(textures, 1, 2, 37, 37);

        // Dot texture for trajectory
        Pixmap pixmap = new Pixmap(3, 3, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1); // White dots
        pixmap.fillRectangle(0, 0, 3, 3);
        dottexture = new Texture(pixmap);
        pixmap.dispose();

        // Initialize trajectory points
        trajectoryPoints = new Array<>();
    }

    @Override
    public void show() {
        InputMultiplexer multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(stage);

        // Custom InputAdapter for dragging slingshot
        multiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                Vector2 touchPos = viewport.unproject(new Vector2(screenX, screenY));
                if (touchPos.dst(slingOrigin) <= 50) {
                    dragging = true;
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                if (dragging) {
                    Vector2 touchPos = viewport.unproject(new Vector2(screenX, screenY));
                    slingStretch.set(touchPos);
                    if (slingStretch.dst(slingOrigin) > 75) {
                        slingStretch.set(slingOrigin).add(touchPos.sub(slingOrigin).nor().scl(75));
                    }
                    updateTrajectory();
                    return true;
                }
                return false;
            }

            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                if (dragging) {
                    dragging = false;
                    slingStretch.set(slingOrigin);
                    trajectoryPoints.clear();
                    return true;
                }
                return false;
            }
        });

        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        // Draw background
        batch.draw(bg, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        // Draw slingshot and bird
        batch.draw(brd, slingStretch.x - 36, slingStretch.y - 34, 72, 69);

        // Draw trajectory points
        for (Vector2 point : trajectoryPoints) {
            batch.draw(dottexture, point.x - 1.5f, point.y - 1.5f, 3, 3);
        }

        // Example block (static object)
        batch.draw(steelBlockRegion, 400, 150, 37, 37);

        batch.end();

        // Render Box2D debug shapes
        debugRenderer.render(world, camera.combined);

        // Step Box2D world
        world.step(1 / 60f, 6, 2);
        stage.act(delta);
        stage.draw();
    }

    private void updateTrajectory() {
        trajectoryPoints.clear();
        Vector2 direction = slingStretch.cpy().sub(slingOrigin);
        int steps = 15;

        for (int i = 1; i <= steps; i++) {
            float t = i * 0.2f; // Adjust step size for better visualization
            float x = slingOrigin.x + direction.x * t;
            float y = slingOrigin.y + direction.y * t - (0.5f * 9.8f * t * t);
            trajectoryPoints.add(new Vector2(x, y));
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height);
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
        bird.dispose();
        textures.dispose();
        world.dispose();
        debugRenderer.dispose();
        dottexture.dispose();
    }
}
