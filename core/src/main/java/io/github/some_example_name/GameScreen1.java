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
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import Birds.*;
import Pigs.*;
import Elements.*;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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
    private Vector2 slingOrigin = new Vector2(125, 130);
    private Vector2 slingStretch = new Vector2(slingOrigin);
    private boolean dragging = false;
    private Array<Vector2> trajectoryPoints;
    public Array<Bird> birds;
    private int currentBirdIndex;
    public Array<Pigs> Pig;
    public Array<Structure> Structure;
    public Array<Body> bodiesToDestroy;
    private Map<Bird, Long> birdLaunchTimes = new HashMap<>();
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
        world = new World(new Vector2(0, -4.0f), true);
        this.viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        this.stage = new Stage(viewport, batch);
        Pixmap pixmap = new Pixmap(3, 3, Pixmap.Format.RGBA8888);
        pixmap.setColor(1, 1, 1, 1);
        pixmap.fillRectangle(0, 0, 3, 3);
        birds = new Array<>();
        Pig = new Array<>();
        Structure = new Array<>();
        currentBirdIndex = 0; //
        dottexture = new Texture(pixmap);
        bodiesToDestroy = new Array<>();

        // Create the texture from the pixmap
        pixmap.dispose();
        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.top().right();
        table.add(pauseButton).pad(10).width(80);
    }

    @Override
    public void show() {

        InputMultiplexer multiplexer = new InputMultiplexer();

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
                    Vector2 dragVector = slingOrigin.cpy().sub(slingStretch);
                    float distance = dragVector.len(); // Get the length of the drag
                    float forceScale = Math.min(distance / 150f, 2f); // Scale force based on drag, limit max force to 2

                    float speedMultiplier = 0.25f;  // This scales the speed down to half (adjust as needed)
                    Vector2 launchDirection = dragVector.scl(forceScale * speedMultiplier); // Apply scaling to the launch vector

                    if (currentBirdIndex < birds.size) {
                        Body bird = birds.get(currentBirdIndex).body;
                        bird.setTransform(slingOrigin.x / 100f, slingOrigin.y / 100f, 0);
                        bird.setLinearVelocity(launchDirection);
                        bird.setActive(true);

                        birdLaunchTimes.put(birds.get(currentBirdIndex), TimeUtils.nanoTime());


                        currentBirdIndex++;
                    }

                    slingStretch.set(slingOrigin);
                    trajectoryPoints.clear();
                    return true;
                }
                return false;
            }

        });

        multiplexer.addProcessor(stage);

        Gdx.input.setInputProcessor(multiplexer);

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

        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttons, 408, 174, 118, 108)));

        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("GameScreen", "Pause button clicked");
                main.pause.previousScreen = main.game1;
                main.setScreen(main.pause);
            }
        });

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);
        table.top().right();
        table.add(pauseButton).pad(10).width(80);

        CircleShape birdShape = new CircleShape();
        birdShape.setRadius(18 / 100f);

        rdbrd.body = createBody(world, BodyDef.BodyType.DynamicBody, 80 / 100f, 120/ 100f, birdShape, 1f, 0.3f, 0.5f); // Place bird at (80px, 200px) in Box2D world
        rdbrd.body.setLinearDamping(1);
        blbrd.body = createBody(world, BodyDef.BodyType.DynamicBody, 60/ 100f, 120 / 100f, birdShape, 1f, 0.3f, 0.5f); // Place bird at (160px, 200px) in Box2D world
        blbrd.body.setLinearDamping(1);
        prbrd.body = createBody(world, BodyDef.BodyType.DynamicBody, 40 / 100f, 120 / 100f, birdShape, 1f, 0.3f, 0.5f); // Place bird at (240px, 200px) in Box2D world
        prbrd.body.setLinearDamping(1);
        birds.add(rdbrd);
        birds.add(blbrd);
        birds.add(prbrd);

        birdShape.dispose();

        CircleShape pigShape = new CircleShape();
        pigShape.setRadius(19.5f / 100f);



        armpig.body = createBody(world, BodyDef.BodyType.DynamicBody,
            400 / 100f, 135 / 100f, pigShape, 1f, 5.0f, 0.5f);
        armpig.body.setLinearDamping(2.0f);


        pig.body = createBody(world, BodyDef.BodyType.DynamicBody,
            520 / 100f, 120 / 100f, pigShape, 1f, 1.5f, 0.5f);
        pig.body.setLinearDamping(2.0f);
        pigShape.dispose();
        Pig.add(pig);
        Pig.add(armpig);


        PolygonShape steelblockShape = new PolygonShape();
        steelblockShape.setAsBox(18 / 100f, 18 / 100f);
        steelblock.body = createBody(world, BodyDef.BodyType.DynamicBody, 500 / 100f, 102 / 100f, steelblockShape, 2.5f, 0.5f, 0.2f);

        steelblock2.body = createBody(world, BodyDef.BodyType.DynamicBody, 500 / 100f, 137 / 100f, steelblockShape, 2.5f, 0.5f, 0.2f);

        steelblock3.body = createBody(world, BodyDef.BodyType.DynamicBody, 500 / 100f, 169 / 100f, steelblockShape, 2.5f, 0.5f, 0.2f);
        PolygonShape steelslabShape = new PolygonShape();
        steelslabShape.setAsBox(37 / 100f, 9 / 100f);
        steelslab.body = createBody(world, BodyDef.BodyType.DynamicBody, 400 / 100f, 100 / 100f, steelslabShape, 5f, 0.8f, 0.0f);

        steelslab2.body = createBody(world, BodyDef.BodyType.DynamicBody, 400 / 100f, 118 / 100f, steelslabShape, 5f, 0.8f, 0.0f);
        PolygonShape steeltriangleShape = new PolygonShape();
        steeltriangleShape.set(new Vector2[]{
            new Vector2(-18 / 100f, -18 / 100f),
            new Vector2(18 / 100f, -18 / 100f),
            new Vector2(0, 18 / 100f)
        });

        steeltriangle.body = createBody(world, BodyDef.BodyType.DynamicBody, 500 / 100f, 200 / 100f, steeltriangleShape, 5f, 0.9f, 0.2f);

        steelblockShape.dispose();
        steeltriangleShape.dispose();
        steelslabShape.dispose();
        rdbrd.body.setUserData(rdbrd);
        prbrd.body.setUserData(prbrd);
        blbrd.body.setUserData(blbrd);
        pig.body.setUserData(pig);

        armpig.body.setUserData(armpig);
        steelblock.body.setUserData(steelblock);
        steelblock2.body.setUserData(steelblock2);
        steelblock3.body.setUserData(steelblock3);
        steelslab.body.setUserData(steelslab);
        steelslab2.body.setUserData(steelslab2);
        steeltriangle.body.setUserData(steeltriangle);
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(WORLD_WIDTH / 2 / 100f, 100/ 100f);

        Body groundBody = world.createBody(groundBodyDef);

        PolygonShape groundShape = new PolygonShape();
        groundShape.setAsBox(WORLD_WIDTH / 2 / 100f, 1 / 100f);
        FixtureDef groundFixtureDef = new FixtureDef();
        groundFixtureDef.shape = groundShape;
        groundFixtureDef.friction = 1.5f;
        groundFixtureDef.restitution = 0.0f;

        groundBody.createFixture(groundFixtureDef);
        groundShape.dispose();
        world.setContactListener(new ContactListener() {

            @Override
            public void beginContact(Contact contact) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                boolean isBirdA = isBird(fixtureA);
                boolean isBirdB = isBird(fixtureB);

                boolean isPigOrStructureA = isPigOrStructure(fixtureA);
                boolean isPigOrStructureB = isPigOrStructure(fixtureB);

                if (isBirdA && isPigOrStructureB) {
                    Bird bird = (Bird) fixtureA.getBody().getUserData();
                    processCollision(fixtureB, bird.getattack());
                } else if (isBirdB && isPigOrStructureA) {
                    Bird bird = (Bird) fixtureB.getBody().getUserData();
                    processCollision(fixtureA, bird.getattack());
                }
            }

            @Override
            public void endContact(Contact contact) {
                // Optional: Handle end of collision
            }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) {
                // Optional: Handle pre-solve logic
            }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) {
                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                if (isPigAndStructure(fixtureA, fixtureB)) {
                    applyDamageBasedOnVelocity(fixtureA, fixtureB);
                }
            }


            private boolean isPigAndStructure(Fixture fixtureA, Fixture fixtureB) {
                boolean isPigA = fixtureA != null && fixtureA.getBody().getUserData() instanceof Pigs;
                boolean isStructureB = fixtureB != null && fixtureB.getBody().getUserData() instanceof Structure;

                boolean isPigB = fixtureB != null && fixtureB.getBody().getUserData() instanceof Pigs;
                boolean isStructureA = fixtureA != null && fixtureA.getBody().getUserData() instanceof Structure;

                return (isPigA && isStructureB) || (isPigB && isStructureA);
            }

            private void applyDamageBasedOnVelocity(Fixture fixtureA, Fixture fixtureB) {
                Body structureBody = null;
                Pigs pig = null;

                if (fixtureA.getBody().getUserData() instanceof Structure &&
                    fixtureB.getBody().getUserData() instanceof Pigs) {
                    structureBody = fixtureA.getBody();
                    pig = (Pigs) fixtureB.getBody().getUserData();
                } else if (fixtureB.getBody().getUserData() instanceof Structure &&
                    fixtureA.getBody().getUserData() instanceof Pigs) {
                    structureBody = fixtureB.getBody();
                    pig = (Pigs) fixtureA.getBody().getUserData();
                }

                if (structureBody == null || pig == null) return;

                float verticalVelocity = structureBody.getLinearVelocity().y;

                if (verticalVelocity < -0.5f) {
                    float damageAmount = Math.abs(verticalVelocity) * 5f;
                    processHealthReduction(pig, (Structure) structureBody.getUserData(), damageAmount);
                }
            }


            private void applyDamageForPigAndStructure(Fixture fixtureA, Fixture fixtureB, float collisionForce) {
                if (fixtureA.getBody().getUserData() instanceof Pigs &&
                    fixtureB.getBody().getUserData() instanceof Structure) {
                    Pigs pig = (Pigs) fixtureA.getBody().getUserData();
                    Structure structure = (Structure) fixtureB.getBody().getUserData();
                    processHealthReduction(pig, structure, collisionForce);
                } else if (fixtureB.getBody().getUserData() instanceof Pigs &&
                    fixtureA.getBody().getUserData() instanceof Structure) {
                    Pigs pig = (Pigs) fixtureB.getBody().getUserData();
                    Structure structure = (Structure) fixtureA.getBody().getUserData();
                    processHealthReduction(pig, structure, collisionForce);
                }
            }

            private void processHealthReduction(Pigs pig, Structure structure, float collisionForce) {
                float damageAmount = collisionForce ;
              int   currenthealth=pig.gethealth();
              int currenthealth2=structure.gethealth();
                System.out.println(currenthealth);
                System.out.println(currenthealth2);
                System.out.println("Touched");
                currenthealth -= damageAmount;
                currenthealth2 -= damageAmount;
                System.out.println(currenthealth);
                System.out.println(currenthealth2);
                pig.sethealth(currenthealth);
                structure.sethealth(currenthealth2);

                System.out.println("Pig health after collision: " + pig.health);

                if (currenthealth <= 0) {
                    pig.destroyed = true;
                    Gdx.app.log("GameScreen", "Pig destroyed in collision with structure!");
                    bodiesToDestroy.add(pig.body); // Schedule pig body for destruction
                }
                if (currenthealth2 <= 0) {
                    structure.destroyed = true;
                    Gdx.app.log("GameScreen", "Structure destroyed in structure!");
                    bodiesToDestroy.add(structure.body);
                }
            }



            private boolean isBird(Fixture fixture) {
                return fixture != null && fixture.getBody().getUserData() instanceof Bird;
            }

            private boolean isPigOrStructure(Fixture fixture) {
                return fixture != null && (fixture.getBody().getUserData() instanceof Pig ||
                    fixture.getBody().getUserData() instanceof Structure);
            }
            public void processCollision(Fixture fixture, float damage) {
                if (fixture.getBody().getUserData() instanceof Pigs) {
                    Pigs pig = (Pigs) fixture.getBody().getUserData();
                    int currentHealth = pig.gethealth();
                    currentHealth -= damage;
                    pig.sethealth(currentHealth);

                    System.out.println("Pig health: " + pig.health);

                    if (currentHealth <= 0) {
                        pig.destroyed = true;
                        Gdx.app.log("GameScreen", "Pig destroyed!");
                        bodiesToDestroy.add(fixture.getBody());
                    }
                } else if (fixture.getBody().getUserData() instanceof Structure) {
                    Structure structure = (Structure) fixture.getBody().getUserData();

                    System.out.println(damage);
                    int currentHealth = structure.gethealth();
                    System.out.println(currentHealth);
                  currentHealth -= damage;
                    structure.sethealth(currentHealth);

                    System.out.println("Structure health: " + structure.health);

                    if (currentHealth <= 0) {
                        structure.destroyed = true; // Mark the structure as destroyed
                        Gdx.app.log("GameScreen", "Structure destroyed!"); // Debugging log
                        bodiesToDestroy.add(fixture.getBody()); // Schedule structure body for destruction
                    }
                }
            }


        });





    }
    public void update(float deltaTime) {
        world.step(deltaTime, 6, 2);

        for (Body body : bodiesToDestroy) {
            if (body != null && body.isActive()) {
                Gdx.app.log("GameScreen", "Destroying body: " + body.getUserData());
                world.destroyBody(body);
            }
        }
        bodiesToDestroy.clear();

        Iterator<Map.Entry<Bird, Long>> iter = birdLaunchTimes.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<Bird, Long> entry = iter.next();
            Body bird = entry.getKey().body;
            long launchTime = entry.getValue();

            if (TimeUtils.nanoTime() - launchTime > 4_000_000_000L) {
                world.destroyBody(bird);
                entry.getKey().destroyed = true;
                iter.remove();
            }
        }


    }

    private boolean lose() {
        boolean check1 = false;
        boolean check2 = false;
        for (Bird bird : birds) {
            if (bird.destroyed) {
                check1 = true;
            }
            else {
                check1 = false;
                break;
            }
        }
        for (Pigs pig : Pig) {
            if (pig.destroyed) {
                check2 = true;
            }
            else {
                check2 = false;
                break;
            }
        }
        return check1 && !check2;
    }

    private boolean win() {
        boolean check = false;
        for (Pigs pig : Pig) {
            if (pig.destroyed) {
                check = true;
            }
            else {
                check = false;
                break;
            }
        }
        return check;
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
        fixtureDef.restitution = restitution;

        body.createFixture(fixtureDef);
        return body;
    }

    public void False(){
        steelblock.destroyed=false;
        steelblock2.destroyed=false;
        steelblock3.destroyed=false;
        steelslab.destroyed=false;
        steelslab2.destroyed=false;
        steeltriangle.destroyed=false;
        pig.destroyed=false;
        armpig.destroyed=false;
        rdbrd.destroyed=false;
        blbrd.destroyed=false;
        prbrd.destroyed=false;
    }

    private void updateTrajectory() {
        trajectoryPoints.clear();
        Vector2 direction = slingStretch.cpy().sub(slingOrigin);

        int steps = 15;

        for (int i = 1; i <= steps; i++) {
            float t = (i * (float) 1.5) / (float) steps;
            float x = slingOrigin.x + direction.x * t;
            float y = slingOrigin.y + direction.y * t;
            trajectoryPoints.add(new Vector2(x, y));
        }
    }

    @Override
    public void render(float delta) {
        update(delta);
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        batch.draw(bg, 0, 0, WORLD_WIDTH, WORLD_HEIGHT);

        if (steelblock.body != null && !steelblock.destroyed) {
            batch.draw(steelblock.steelblock,
                steelblock.body.getPosition().x * 100 - 18,
                steelblock.body.getPosition().y * 100 - 18,
                36, 36);

        }

        if (steelblock2.body != null && !steelblock2.destroyed) {
            batch.draw(steelblock2.steelblock,
                steelblock2.body.getPosition().x * 100 - 18,
                steelblock2.body.getPosition().y * 100 - 18,
                36, 36);
        }

        if (steelblock3.body != null && !steelblock3.destroyed) {
            batch.draw(steelblock3.steelblock,
                steelblock3.body.getPosition().x * 100 - 18,
                steelblock3.body.getPosition().y * 100 - 18,
                36, 36);
        }

        if (steeltriangle.body != null && !steeltriangle.destroyed) {
            batch.draw(steeltriangle.region,
                steeltriangle.body.getPosition().x * 100 - 18,
                steeltriangle.body.getPosition().y * 100 - 18,
                36, 36);
        }

        if (steelslab.body != null && !steelslab.destroyed) {
            batch.draw(steelslab.region,
                steelslab.body.getPosition().x * 100 - 37,
                steelslab.body.getPosition().y * 100 - 9,
                74, 18);
        }

        if (steelslab2.body != null && !steelslab2.destroyed) {
            batch.draw(steelslab2.region,
                steelslab2.body.getPosition().x * 100 - 37,
                steelslab2.body.getPosition().y * 100 - 9,
                74, 18);
        }

        // Draw pigs
        if (armpig.body != null && !armpig.destroyed) {
            batch.draw(armpig.textureregion,
                armpig.body.getPosition().x * 100 - 19.5f,
                armpig.body.getPosition().y * 100 - 19.5f,
                39, 39); // Sprite size
        }

        if (pig.body != null && !pig.destroyed) {
            batch.draw(pig.textureregion,
                pig.body.getPosition().x * 100 - 19.5f,
                pig.body.getPosition().y * 100 - 19.5f,
                39, 39); // Sprite size
        }

        batch.draw(slingshot, slingOrigin.x - 25, slingOrigin.y - 35, 50, 67);

        for (int i = 0; i < birds.size; i++) {
            Body bird = birds.get(i).body;
            if (bird != null && !birds.get(i).destroyed) {
                if (i == 0 ) {
                    batch.draw(rdbrd.textureregion,
                        bird.getPosition().x * 100 - 18,
                        bird.getPosition().y * 100 - 18,
                        36, 36);
                }
                if (i == 1 ) {
                    batch.draw(blbrd.textureregion,
                        bird.getPosition().x * 100 - 18,
                        bird.getPosition().y * 100 - 18,
                        36, 36);
                }
                if (i == 2 ) {
                    batch.draw(prbrd.textureregion,
                        bird.getPosition().x * 100 - 18,
                        bird.getPosition().y * 100 - 18,
                        36, 36);
                }
            }
        }

        for (Vector2 point : trajectoryPoints) {
            batch.draw(dottexture, point.x + 5, point.y + 10, 3, 3);
        }

        batch.end();

        world.step(1 / 60f, 6, 2);

        if (win()) {
            main.wscreen.previousScreen = main.game1;
            main.setScreen(main.wscreen);
        }
        if (lose()) {

            main.setScreen(main.lscreen);
        }

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
        Array<Body> bodies = new Array<>();
        world.getBodies(bodies);
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
        world.dispose();
    }
}
