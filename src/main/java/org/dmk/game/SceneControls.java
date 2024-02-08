package org.dmk.game;

import imgui.ImGui;
import imgui.ImGuiIO;
import imgui.flag.ImGuiCond;
import org.dmk.engine.Bezier;
import org.dmk.engine.IGuiInstance;
import org.dmk.engine.MouseInput;
import org.dmk.engine.Window;
import org.dmk.engine.graph.Model;
import org.dmk.engine.scene.Camera;
import org.dmk.engine.scene.Entity;
import org.dmk.engine.scene.ModelLoader;
import org.dmk.engine.scene.Scene;
import org.dmk.engine.scene.lights.DirLight;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class SceneControls implements IGuiInstance {


    private final Camera staticCamera;

    private final Camera freeCamera;

    private final Camera carCamera;

    private CameraType cameraType;

    private float[] coneX;
    private float[] coneY;
    private float[] coneZ;

    private float[] controlPoint1;
    private float[] controlPoint2;
    private float[] controlPoint3;
    private float[] controlPoint4;
    private float[] controlPoint5;
    private float[] controlPoint6;
    private float[] controlPoint7;
    private float[] controlPoint8;
    private float[] controlPoint9;
    private float[] controlPoint10;
    private float[] controlPoint11;
    private float[] controlPoint12;

    private ShadingType shadingType;

    private float[] spotlightColor;

    private float[] ambientLight;

    private float[] dirLightX;
    private float[] dirLightY;
    private float[] dirLightZ;
    private float[] dirLightColor;

    private boolean activeFog;

    public SceneControls(Scene scene) {
        staticCamera = new Camera();
        staticCamera.setPosition(-12f, 7.0f, 12f);
        staticCamera.addRotation((float) Math.toRadians(20.0f), (float) Math.toRadians(45f));
        freeCamera = new Camera();
        freeCamera.setPosition(-12f, 7.0f, 12f);
        carCamera = new Camera();
        cameraType = CameraType.STATIC;
        scene.setCamera(staticCamera);
        coneX = new float[] {-0.3f};
        coneY = new float[] {-0.4f};
        coneZ = new float[1];
        spotlightColor =  new float[] {1.0f, 1.0f, 0f};
        ambientLight = new float[] {0.3f, 0.3f, 0.3f};
        controlPoint1 = new float[1];
        controlPoint2 = new float[1];
        controlPoint3 = new float[1];
        controlPoint4 = new float[1];
        controlPoint5 = new float[1];
        controlPoint6 = new float[1];
        controlPoint7 = new float[1];
        controlPoint8 = new float[1];
        controlPoint9 = new float[] {1.5f};
        controlPoint10 = new float[1];
        controlPoint11 = new float[] {-0.3f};
        controlPoint12 = new float[1];
        shadingType = ShadingType.PHONG;

        dirLightX = new float[] {0.0f};
        dirLightY = new float[] {1.0f};
        dirLightZ = new float[] {0.0f};
        dirLightColor = new float[] {1.0f, 1.0f, 1.0f};
    }

    @Override
    public void drawGui() {
        ImGui.newFrame();
        ImGui.setNextWindowPos(0, 0, ImGuiCond.Always);
        ImGui.setNextWindowSize(450, 400);

        ImGui.begin("Controls");
        if (ImGui.collapsingHeader("Cameras")) {
            if (ImGui.radioButton("Static Camera", cameraType == CameraType.STATIC)) {
                cameraType = CameraType.STATIC;
            }
            if (ImGui.radioButton("Free Camera", cameraType == CameraType.FREE)) {
                cameraType = CameraType.FREE;
            }
            if (ImGui.radioButton("Car Camera", cameraType == CameraType.CAR)) {
                cameraType = CameraType.CAR;
            }
        }

        if (ImGui.collapsingHeader("Spotlight")) {
            ImGui.sliderFloat("Cone dir - x", coneX, -1.0f, 1.0f, "%.2f");
            ImGui.sliderFloat("Cone dir - y", coneY, -1.0f, 1.0f, "%.2f");
            ImGui.sliderFloat("Cone dir - z", coneZ, -1.0f, 1.0f, "%.2f");
            ImGui.colorEdit3("Spotlight Color", spotlightColor);
        }

        if(ImGui.collapsingHeader("Ambient Light")) {
            ImGui.colorEdit3("Ambient Light Color", ambientLight);
        }

        if(ImGui.collapsingHeader("Directional Light")) {
            ImGui.sliderFloat("Directional Light - x", dirLightX, -1.0f, 1.0f, "%.2f");
            ImGui.sliderFloat("Directional Light - y", dirLightY, -1.0f, 1.0f, "%.2f");
            ImGui.sliderFloat("Directional Light - z", dirLightZ, -1.0f, 1.0f, "%.2f");
            ImGui.colorEdit3("Directional Light Color", dirLightColor);
        }

        if(ImGui.collapsingHeader("Shading")) {
            if(ImGui.radioButton("Flat", shadingType == ShadingType.FLAT)) {
                shadingType = ShadingType.FLAT;
            }
            if (ImGui.radioButton("Gouraud", shadingType == ShadingType.GOURAUD)) {
                shadingType = ShadingType.GOURAUD;
            }
            if (ImGui.radioButton("Phong", shadingType == ShadingType.PHONG)) {
                shadingType = ShadingType.PHONG;
            }
        }

        if(ImGui.collapsingHeader("Fog")) {
            if(ImGui.checkbox("Active Fog", activeFog)) {
                activeFog = !activeFog;
            }

        }

        if (ImGui.collapsingHeader("Control Points")) {
            ImGui.sliderFloat("Control Point 1", controlPoint1, -2.0f, 2.0f, "%.2f");
            ImGui.sliderFloat("Control Point 2", controlPoint2, -2.0f, 2.0f, "%.2f");
            ImGui.sliderFloat("Control Point 3", controlPoint3, -2.0f, 2.0f, "%.2f");
            ImGui.sliderFloat("Control Point 4", controlPoint4, -2.0f, 2.0f, "%.2f");
            ImGui.sliderFloat("Control Point 5", controlPoint5, -2.0f, 2.0f, "%.2f");
            ImGui.sliderFloat("Control Point 6", controlPoint6, -2.0f, 2.0f, "%.2f");
            ImGui.sliderFloat("Control Point 7", controlPoint7, -2.0f, 2.0f, "%.2f");
            ImGui.sliderFloat("Control Point 8", controlPoint8, -2.0f, 2.0f, "%.2f");
            ImGui.sliderFloat("Control Point 9", controlPoint9, -2.0f, 2.0f, "%.2f");
            ImGui.sliderFloat("Control Point 10", controlPoint10, -2.0f, 2.0f, "%.2f");
            ImGui.sliderFloat("Control Point 11", controlPoint11, -2.0f, 2.0f, "%.2f");
            ImGui.sliderFloat("Control Point 12", controlPoint12, -2.0f, 2.0f, "%.2f");
        }
        ImGui.end();
        ImGui.endFrame();
        ImGui.render();
    }

    @Override
    public boolean handleGuiInput(Scene scene, Window window) {
        ImGuiIO imGuiIO = ImGui.getIO();
        MouseInput mouseInput = window.getMouseInput();
        Vector2f mousePos = mouseInput.getCurrentPos();
        imGuiIO.setMousePos(mousePos.x, mousePos.y);
        imGuiIO.setMouseDown(0, mouseInput.isLeftButtonPressed());
        imGuiIO.setMouseDown(1, mouseInput.isRightButtonPressed());

        boolean consumed = imGuiIO.getWantCaptureMouse() || imGuiIO.getWantCaptureKeyboard();
        if (consumed) {
            switch (cameraType) {
                case STATIC:
                    scene.setCamera(staticCamera);
                    break;
                case FREE:
                    scene.setCamera(freeCamera);
                    break;
                case CAR:
                    scene.setCamera(carCamera);
                    break;
            }
            scene.setShadingType(shadingType);
            scene.getSceneLights().getSpotLights().get(0).setConeDirection(coneX[0], coneY[0], coneZ[0]);
            scene.getSceneLights().getSpotLights().get(1).setConeDirection(coneX[0], coneY[0], coneZ[0]);
            scene.getSceneLights().getSpotLights().get(0).setColor(spotlightColor[0], spotlightColor[1], spotlightColor[2]);
            scene.getSceneLights().getSpotLights().get(1).setColor(spotlightColor[0], spotlightColor[1], spotlightColor[2]);

            DirLight dirLight = scene.getSceneLights().getDirLight();
            dirLight.setDirection(new Vector3f(dirLightX[0], dirLightY[0], dirLightZ[0]));
            dirLight.setColor(new Vector3f(dirLightColor[0], dirLightColor[1], dirLightColor[2]));

            scene.getSceneLights().getAmbientLight().setColor(ambientLight[0], ambientLight[1], ambientLight[2]);
            Vector3f[][] prevControlPoints = scene.getBezier().getControlPoints();
            boolean recalculate = false;

            scene.getFog().setActive(activeFog);

            if (prevControlPoints[1][0].z != controlPoint1[0]) {
                recalculate = true;
            }
            if (prevControlPoints[2][0].z != controlPoint2[0]) {
                recalculate = true;
            }
            if (prevControlPoints[0][1].z != controlPoint3[0]) {
                recalculate = true;
            }
            if (prevControlPoints[1][1].z != controlPoint4[0]) {
                recalculate = true;
            }
            if (prevControlPoints[2][1].z != controlPoint5[0]) {
                recalculate = true;
            }
            if (prevControlPoints[3][1].z != controlPoint6[0]) {
                recalculate = true;
            }
            if (prevControlPoints[0][2].z != controlPoint7[0]) {
                recalculate = true;
            }
            if (prevControlPoints[1][2].z != controlPoint8[0]) {
                recalculate = true;
            }
            if (prevControlPoints[2][2].z != controlPoint9[0]) {
                recalculate = true;
            }
            if (prevControlPoints[3][2].z != controlPoint10[0]) {
                recalculate = true;
            }
            if (prevControlPoints[1][3].z != controlPoint11[0]) {
                recalculate = true;
            }
            if (prevControlPoints[2][3].z != controlPoint12[0]) {
                recalculate = true;
            }

            if(recalculate) {
                Bezier bezier = new Bezier();
                Vector3f[][] controlPoints = bezier.getControlPoints();
                controlPoints[1][0].z = controlPoint1[0];
                controlPoints[2][0].z = controlPoint2[0];
                controlPoints[0][1].z = controlPoint3[0];
                controlPoints[1][1].z = controlPoint4[0];
                controlPoints[2][1].z = controlPoint5[0];
                controlPoints[3][1].z = controlPoint6[0];
                controlPoints[0][2].z = controlPoint7[0];
                controlPoints[1][2].z = controlPoint8[0];
                controlPoints[2][2].z = controlPoint9[0];
                controlPoints[3][2].z = controlPoint10[0];
                controlPoints[1][3].z = controlPoint11[0];
                controlPoints[2][3].z = controlPoint12[0];

                scene.setBezier(bezier);
                Model bezierModel = ModelLoader.loadBezier("bezier", scene.getBezier());
                scene.addModel(bezierModel);
                Entity bezierEntity = new Entity("bezierEntity", "bezier");
                bezierEntity.setScale(5.0f);

                bezierEntity.setPosition(0f, 5f, 0f);
                //bezierEntity.setRotation(1, 0, 0, (float) Math.toRadians(-90));
                bezierEntity.updateModelMatrix();
                scene.addEntity(bezierEntity);
            }


        }
        return consumed;
    }

    public Camera getCarCamera() {
        return carCamera;
    }

    public CameraType getCameraType() {
        return cameraType;
    }
}


