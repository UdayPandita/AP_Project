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
public class GameScreen2 implements Screen {
    private final Main main;
    private static final float WORLD_WIDTH = 800;
    private static final float WORLD_HEIGHT = 480;
    Texture buttons;
    private final Stage stage;
    private final SpriteBatch batch;
    private Texture bg, tnt, textures, textures2, slingshot, bird;
    private TextureRegion region,character, brd;
    private OrthographicCamera camera;
    private final Viewport viewport;
    Redbird rdbrd;
    Blackbird blbrd;
    PurpleBird prbrd;
    Armourpig armpig;
    Kingpig kngpig;
    Chiefpig chfpig;
    Steelblock steelblock;
    Steelblock steelblock2;
    Steelblock steelblock3;
    Steelblock steelblock4;
    Steelblock steelblock5;
    Steelblock steelblock6;
    private Texture dottexture;
    private World world;
    private Vector2 slingOrigin = new Vector2(125, 190);
    private Vector2 slingStretch = new Vector2(slingOrigin);
    private boolean dragging = false;
    private Array<Vector2> trajectoryPoints;
    private Array<Bird> birds;
    private int currentBirdIndex;
    private Array<Pigs> Pig;
    private Array<Structure> Structure;
    private  Array<Body> bodiesToDestroy;
    private Map<Bird, Long> birdLaunchTimes = new HashMap<>();
    public GameScreen2(final Main main) {
        this.main = main;
        this.batch = main.batch;
        buttons = new Texture("buttons.png");
        rdbrd = new Redbird();
        blbrd = new Blackbird();
        prbrd = new PurpleBird();
        armpig = new Armourpig();
        kngpig = new Kingpig();
        chfpig = new Chiefpig();
        steelblock = new Steelblock();
        steelblock2 = new Steelblock();
        steelblock3 = new Steelblock();
        steelblock4 = new Steelblock();
        steelblock5 = new Steelblock();
        steelblock6 = new Steelblock();
        ImageButton pauseButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(buttons, 408, 174, 118, 108)));

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
        currentBirdIndex = 0;
        dottexture = new Texture(pixmap);
        bodiesToDestroy = new Array<>();

        Table table = new Table();
        table.setFillParent(true);
        stage.addActor(table);

        table.top().right();
        table.add(pauseButton).pad(10).width(80);

        pauseButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Gdx.app.log("GameScreen", "Pause button clicked");
                main.pause.previousScreen = main.game2;
                main.setScreen(main.pause);
            }
        });

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
                    float distance = dragVector.len();
                    float forceScale = Math.min(distance / 150f, 2f);

                    float speedMultiplier = 0.25f;
                    Vector2 launchDirection = dragVector.scl(forceScale * speedMultiplier);

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

        bg = new Texture(Gdx.files.internal("background2.png"));
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
                main.pause.previousScreen = main.game2;
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

        rdbrd.body = createBody(world, BodyDef.BodyType.DynamicBody, 80 / 100f, 195/ 100f, birdShape, 1f, 0.3f, 0.5f); // Place bird at (80px, 200px) in Box2D world
        rdbrd.body.setLinearDamping(1);
        blbrd.body = createBody(world, BodyDef.BodyType.DynamicBody, 60/ 100f, 195 / 100f, birdShape, 1f, 0.3f, 0.5f); // Place bird at (160px, 200px) in Box2D world
        blbrd.body.setLinearDamping(1);
        prbrd.body = createBody(world, BodyDef.BodyType.DynamicBody, 40 / 100f, 195 / 100f, birdShape, 1f, 0.3f, 0.5f); // Place bird at (240px, 200px) in Box2D world
        prbrd.body.setLinearDamping(1);
        birds.add(rdbrd);
        birds.add(blbrd);
        birds.add(prbrd);
        birdShape.dispose();

        CircleShape pigShape = new CircleShape();
        pigShape.setRadius(19.5f / 100f); // 19.5 pixels converted to Box2D units
        armpig.body = createBody(world, BodyDef.BodyType.DynamicBody, 578 / 100f, 152 / 100f, pigShape, 1f, 5.0f, 0.5f);
        armpig.body.setLinearDamping(2.0f);
        chfpig.body=createBody(world,BodyDef.BodyType.DynamicBody,538/100f,152/100f,pigShape,1f,5.0f,0.5f);
        chfpig.body.setLinearDamping(2.0f);
        kngpig.body=createBody(world,BodyDef.BodyType.DynamicBody,555/100f,260/100f,pigShape,1f,5.0f,0.5f);
        kngpig.body.setLinearDamping(2.0f);
        pigShape.dispose();
        Pig.add(chfpig);
        Pig.add(armpig);
        Pig.add(kngpig);

        PolygonShape steelblockShape = new PolygonShape();
        steelblockShape.setAsBox(18 / 100f, 18 / 100f); // S
        steelblock.body = createBody(world, BodyDef.BodyType.DynamicBody, 500 / 100f, 152/ 100f, steelblockShape, 2.5f, 0.5f, 0.2f);
        steelblock2.body = createBody(world, BodyDef.BodyType.DynamicBody, 520 / 100f, 189 / 100f, steelblockShape, 2.5f, 0.5f, 0.2f);
        steelblock3.body = createBody(world, BodyDef.BodyType.DynamicBody, 540 / 100f, 226 / 100f, steelblockShape, 2.5f, 0.5f, 0.2f);
        steelblock4.body=createBody(world, BodyDef.BodyType.DynamicBody, 578 / 100f, 226 / 100f, steelblockShape, 2.5f, 0.5f, 0.2f);
        steelblock5.body=createBody(world, BodyDef.BodyType.DynamicBody, 602 / 100f, 189 / 100f, steelblockShape, 2.5f, 0.5f, 0.2f);
        steelblock6.body=createBody(world, BodyDef.BodyType.DynamicBody, 620 / 100f, 152 / 100f, steelblockShape, 2.5f, 0.5f, 0.2f);
        steelblockShape.dispose();
        rdbrd.body.setUserData(rdbrd);
        prbrd.body.setUserData(prbrd);
        blbrd.body.setUserData(blbrd);
        armpig.body.setUserData(armpig);

        kngpig.body.setUserData(kngpig);
        chfpig.body.setUserData(chfpig);
        steelblock.body.setUserData(steelblock);
        steelblock2.body.setUserData(steelblock2);
        steelblock3.body.setUserData(steelblock3);
        steelblock4.body.setUserData(steelblock4);
        steelblock5.body.setUserData(steelblock5);
        steelblock6.body.setUserData(steelblock6);


        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.type = BodyDef.BodyType.StaticBody;
        groundBodyDef.position.set(WORLD_WIDTH / 2 / 100f, 145/ 100f);

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
                    processCollision(fixtureB, bird.attack);
                } else if (isBirdB && isPigOrStructureA) {
                    Bird bird = (Bird) fixtureB.getBody().getUserData();
                    processCollision(fixtureA, bird.attack);
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
                    bodiesToDestroy.add(pig.body);
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
                        structure.destroyed = true;
                        Gdx.app.log("GameScreen", "Structure destroyed!");
                        bodiesToDestroy.add(fixture.getBody());
                    }
                }
            }
        });
    }
    public void update(float deltaTime) {
        world.step(deltaTime, 6, 2);

        for (Body body : bodiesToDestroy) {
            if (body != null && body.isActive()) {
                Gdx.app.log("GameScreen", "Destroying body: " + body.getUserData()); // Debugging log
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
        if (steelblock4.body != null && !steelblock4.destroyed) {
            batch.draw(steelblock4.steelblock,
                steelblock4.body.getPosition().x * 100 - 18,
                steelblock4.body.getPosition().y * 100 - 18,
                36, 36);
        }
        if (steelblock5.body != null && !steelblock5.destroyed) {
            batch.draw(steelblock5.steelblock,
                steelblock5.body.getPosition().x * 100 - 18,
                steelblock5.body.getPosition().y * 100 - 18,
                36, 36);
        }
        if (steelblock6.body != null && !steelblock6.destroyed) {
            batch.draw(steelblock6.steelblock,
                steelblock6.body.getPosition().x * 100 - 18,
                steelblock6.body.getPosition().y * 100 - 18,
                36, 36);
        }
        if (armpig.body != null && !armpig.destroyed) {
            batch.draw(armpig.textureregion,
                armpig.body.getPosition().x * 100 - 19.5f,
                armpig.body.getPosition().y * 100 - 19.5f,
                39, 39);
        }
        if (kngpig.body != null && !kngpig.destroyed) {
            batch.draw(kngpig.textureregion,
                kngpig.body.getPosition().x * 100 - 19.5f,
                kngpig.body.getPosition().y * 100 - 19.5f,
                39, 39);
        }
        if (chfpig.body != null && !chfpig.destroyed) {
            batch.draw(chfpig.textureregion,
                chfpig.body.getPosition().x * 100 - 19.5f,
                chfpig.body.getPosition().y * 100 - 19.5f,
                39, 39);
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



        for (Vector2 point : trajectoryPoints) {
            batch.draw(dottexture, point.x + 5, point.y + 10, 3, 3);
        }

        batch.end();

        world.step(1 / 60f, 6, 2);

        if (win()) {
            main.wscreen.previousScreen = main.game2;
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
