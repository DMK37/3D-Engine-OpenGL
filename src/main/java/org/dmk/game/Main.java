package org.dmk.game;

import org.joml.*;
import org.dmk.engine.*;
import org.dmk.engine.graph.*;
import org.dmk.engine.scene.*;
import org.dmk.engine.scene.lights.*;

import java.lang.Math;

import static org.lwjgl.glfw.GLFW.*;

public class Main implements IAppLogic {

    private static final float MOUSE_SENSITIVITY = 0.1f;
    private static final float MOVEMENT_SPEED = 0.005f;

    private SceneControls sceneControls;

    Entity carEntity;

    public static void main(String[] args) {
        Main main = new Main();
        Window.WindowOptions opts = new Window.WindowOptions();
        opts.antiAliasing = false;
        Engine gameEng = new Engine("GK4", opts, main);
        gameEng.start();
    }

    @Override
    public void cleanup() {
    }

    @Override
    public void init(Window window, Scene scene, Render render) {

        String terrainModelId = "terrain";

        Model terrainModel = ModelLoader.loadModel(terrainModelId, "resources/models/terrain/terrain.obj");
        scene.addModel(terrainModel);
        Entity terrainEntity = new Entity("terrainEntity", terrainModelId);
        terrainEntity.setScale(17.0f);
        terrainEntity.updateModelMatrix();
        scene.addEntity(terrainEntity);

        scene.setBezier(new Bezier());
        Model bezierModel = ModelLoader.loadBezier("bezier", scene.getBezier());
        scene.addModel(bezierModel);
        Entity bezierEntity = new Entity("bezierEntity", "bezier");

        bezierEntity.setPosition(0f, 5f, 0f);
        bezierEntity.updateModelMatrix();
        scene.addEntity(bezierEntity);


        Model bishopModel = ModelLoader.loadModel("bishop-model", "resources/models/chess/figures/Bishop.obj");
        scene.addModel(bishopModel);
        Entity bishopEntity = new Entity("row-entity1", bishopModel.getId());
        bishopEntity.setScale(0.1f);
        bishopEntity.setPosition(0f, 0f, 14f);
        bishopEntity.setRotation(1, 0, 0, (float) Math.toRadians(-90));
        bishopEntity.updateModelMatrix();
        scene.addEntity(bishopEntity);

        Entity bishopEntity2 = new Entity("row-entity2", bishopModel.getId());
        bishopEntity2.setScale(0.1f);
        bishopEntity2.setPosition(0f, 0f, -14f);
        bishopEntity2.setRotation(1, 0, 0, (float) Math.toRadians(-90));
        bishopEntity2.updateModelMatrix();
        scene.addEntity(bishopEntity2);

        Model rookModel = ModelLoader.loadModel("rook-model", "resources/models/chess/figures/Rook.obj");
        scene.addModel(rookModel);
        Entity rookEntity = new Entity("rook-entity1", rookModel.getId());
        rookEntity.setScale(0.1f);
        rookEntity.setPosition(14f, 0f, 0f);
        rookEntity.setRotation(1, 0, 0, (float) Math.toRadians(-90));
        rookEntity.updateModelMatrix();
        scene.addEntity(rookEntity);

        Entity rookEntity2 = new Entity("rook-entity2", rookModel.getId());
        rookEntity2.setScale(0.1f);
        rookEntity2.setPosition(-14f, 0f, 0f);
        rookEntity2.setRotation(1, 0, 0, (float) Math.toRadians(-90));
        rookEntity2.updateModelMatrix();
        scene.addEntity(rookEntity2);


        Model car = ModelLoader.loadModel("car-model", "resources/models/car/Chevrolet_Camaro_SS_High.obj");
        scene.addModel(car);
        carEntity = new Entity("car-entity", car.getId());
        carEntity.setPosition(0f, 0.6f, -radius);
        carEntity.setScale(0.4f);
        carEntity.updateModelMatrix();
        scene.addEntity(carEntity);

        Model setModel = ModelLoader.loadModel("set-model", "resources/models/chess/chessSet/Chess.obj");
        scene.addModel(setModel);
        Entity setEntity = new Entity("set-entity", setModel.getId());
        setEntity.setScale(0.1f);
        setEntity.setPosition(0f, 0f, 0f);
        setEntity.updateModelMatrix();
        scene.addEntity(setEntity);


        SceneLights sceneLights = new SceneLights();
        AmbientLight ambientLight = sceneLights.getAmbientLight();
        ambientLight.setIntensity(0.5f);
        ambientLight.setColor(0.8f, 0.8f, 0.8f);

        DirLight dirLight = sceneLights.getDirLight();
        dirLight.setIntensity(0.8f);
        dirLight.setDirection(new Vector3f(0, 1, 0));


        sceneLights.getPointLights().add(new PointLight(new Vector3f(1, 1, 1),
                new Vector3f(3f, 1.5f, 0f), 1.0f));

        sceneLights.getPointLights().add(new PointLight(new Vector3f(1, 1, 1),
                new Vector3f(-3.5f, 1.5f, -3.5f), 1.0f));

        sceneLights.getPointLights().add(new PointLight(new Vector3f(1, 1, 1),
                new Vector3f(2.5f, 6.5f, 2.5f), 1.0f));

        Vector3f coneDir = new Vector3f(0, -0.5f, 0.2f);
        sceneLights.getSpotLights().add(new SpotLight(new PointLight(new Vector3f(1, 0, 0),
                new Vector3f(-1f, 0.8f, -7f), 0.5f), coneDir, 30.0f));
        sceneLights.getSpotLights().add(new SpotLight(new PointLight(new Vector3f(1, 0, 0),
                new Vector3f(-1f, 0.8f, -7f), 0.5f), coneDir, 30.0f));
        scene.setSceneLights(sceneLights);

        SkyBox skyBox = new SkyBox("resources/models/skybox/skybox.obj");
        skyBox.getSkyBoxEntity().setScale(100);
        skyBox.getSkyBoxEntity().updateModelMatrix();
        scene.setSkyBox(skyBox);

        scene.setFog(new Fog(true, new Vector3f(0.5f, 0.5f, 0.5f), 0.05f));

        sceneControls = new SceneControls(scene);
        scene.setGuiInstance(sceneControls);
    }

    @Override
    public void input(Window window, Scene scene, long diffTimeMillis, boolean inputConsumed) {
        if (inputConsumed) {
            return;
        }
        if(sceneControls.getCameraType() != CameraType.FREE) {
            return;
        }
        float move = diffTimeMillis * MOVEMENT_SPEED;
        Camera camera = scene.getCamera();
        if (window.isKeyPressed(GLFW_KEY_W)) {
            camera.moveForward(move);
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            camera.moveBackwards(move);
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            camera.moveLeft(move);
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            camera.moveRight(move);
        }

        MouseInput mouseInput = window.getMouseInput();
        if (mouseInput.isRightButtonPressed()) {
            Vector2f displVec = mouseInput.getDisplVec();
            camera.addRotation((float) Math.toRadians(-displVec.x * MOUSE_SENSITIVITY), (float) Math.toRadians(-displVec.y * MOUSE_SENSITIVITY));
        }
    }

    private float rotation = 0;
    private final float radius = 10;
    @Override
    public void update(Window window, Scene scene, long diffTimeMillis) {
        float positionX = (float) Math.sin(Math.toRadians(rotation)) * radius;
        float positionZ = (float) Math.cos(Math.toRadians(rotation)) * radius;
        carEntity.setPosition(positionX, 0.6f, positionZ);
        carEntity.setRotation(0, 1, 0, (float) Math.toRadians(90 + rotation));

        SpotLight spotLight = scene.getSceneLights().getSpotLights().get(0);

        float lightX = (float) Math.sin(Math.toRadians(rotation + 12)) * (radius - 0.5f);
        float lightZ = (float) Math.cos(Math.toRadians(rotation + 12)) * (radius - 0.5f);
        spotLight.setPointLight(new PointLight(spotLight.getPointLight().getColor(),
                new Vector3f(lightX, 0.5f, lightZ), 0.8f));
        //spotLight.setConeDirection(new Vector3f((float) -Math.sin(Math.toRadians(rotation)), -0.2f, (float) -Math.cos(Math.toRadians(rotation))));

        spotLight = scene.getSceneLights().getSpotLights().get(1);

        lightX = (float) Math.sin(Math.toRadians(rotation + 12)) * (radius + 0.5f);
        lightZ = (float) Math.cos(Math.toRadians(rotation + 12)) * (radius + 0.5f);
        spotLight.setPointLight(new PointLight(spotLight.getPointLight().getColor(),
                new Vector3f(lightX, 0.5f, lightZ), 0.8f));
        //spotLight.setConeDirection(new Vector3f((float) -Math.sin(Math.toRadians(rotation)), -0.2f, (float) -Math.cos(Math.toRadians(rotation))));

        Camera carCamera = sceneControls.getCarCamera();

        float cameraX = (float) Math.sin(Math.toRadians(rotation + 8)) * (radius + 0.15f);
        float cameraZ = (float) Math.cos(Math.toRadians(rotation + 8)) * (radius + 0.15f);
        carCamera.setPosition(cameraX, 1f, cameraZ);
        carCamera.setRotation((float) Math.toRadians(25), (float) Math.toRadians(90-rotation));
        rotation += 0.5f;
        if (rotation > 360) {
            rotation = 0;
        }
        carEntity.updateModelMatrix();
    }
}