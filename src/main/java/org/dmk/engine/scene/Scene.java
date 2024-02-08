package org.dmk.engine.scene;

import org.dmk.engine.Bezier;
import org.dmk.engine.IGuiInstance;
import org.dmk.engine.graph.*;
import org.dmk.engine.scene.lights.SceneLights;
import org.dmk.game.ShadingType;

import java.util.*;

public class Scene {

    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    private Camera camera;
    private Fog fog;
    private IGuiInstance guiInstance;
    private Map<String, Model> modelMap;
    private Projection projection;
    private SceneLights sceneLights;

    private Bezier bezier;

    private ShadingType shadingType;

    public ShadingType getShadingType() {
        return shadingType;
    }

    public void setShadingType(ShadingType shadingType) {
        this.shadingType = shadingType;
    }

    public void setBezier(Bezier bezier) {
        this.bezier = bezier;
    }

    public Bezier getBezier() {
        return bezier;
    }

    private SkyBox skyBox;
    public Scene(int width, int height) {
        modelMap = new HashMap<>();
        projection = new Projection(width, height);
        camera = new Camera();
        fog = new Fog();
        shadingType = ShadingType.PHONG;
    }

    public void addEntity(Entity entity) {
        String modelId = entity.getModelId();
        Model model = modelMap.get(modelId);
        if (model == null) {
            throw new RuntimeException("Could not find model [" + modelId + "]");
        }
        model.getEntitiesList().add(entity);
    }

    public void addModel(Model model) {
        modelMap.put(model.getId(), model);
    }

    public void cleanup() {
        modelMap.values().forEach(Model::cleanup);
    }

    public Camera getCamera() {
        return camera;
    }

    public Fog getFog() {
        return fog;
    }

    public IGuiInstance getGuiInstance() {
        return guiInstance;
    }

    public Map<String, Model> getModelMap() {
        return modelMap;
    }


    public Projection getProjection() {
        return projection;
    }

    public SceneLights getSceneLights() {
        return sceneLights;
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }

    public void resize(int width, int height) {
        projection.updateProjMatrix(width, height);
    }

    public void setFog(Fog fog) {
        this.fog = fog;
    }

    public void setGuiInstance(IGuiInstance guiInstance) {
        this.guiInstance = guiInstance;
    }

    public void setSceneLights(SceneLights sceneLights) {
        this.sceneLights = sceneLights;
    }

    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }
}
