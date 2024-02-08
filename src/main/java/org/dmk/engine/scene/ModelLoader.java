package org.dmk.engine.scene;

import org.dmk.engine.Bezier;
import org.joml.Vector4f;
import org.lwjgl.PointerBuffer;
import org.lwjgl.assimp.*;
import org.lwjgl.system.MemoryStack;
import org.dmk.engine.graph.*;

import java.io.File;
import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.assimp.Assimp.*;

public class ModelLoader {

    private ModelLoader() {
        // Utility class
    }

    public static Model loadModel(String modelId, String modelPath) {
        return loadModel(modelId, modelPath, aiProcess_GenSmoothNormals | aiProcess_JoinIdenticalVertices |
                aiProcess_Triangulate | aiProcess_FixInfacingNormals | aiProcess_CalcTangentSpace | aiProcess_LimitBoneWeights |
                aiProcess_PreTransformVertices);
    }

    public static Model loadBezier(String modelId, Bezier bezier) {
        bezier.triangulate();
        List<Material> materialList = new ArrayList<>();
        Mesh mesh = new Mesh(bezier.getPositions(), bezier.getNormals(), bezier.getIndices());
        Material defaultMaterial = new Material();
        defaultMaterial.setAmbientColor(new Vector4f(0.4902f, 0.1490f, 0.8039f, 1.0f));
        defaultMaterial.setDiffuseColor(new Vector4f(0.4902f, 0.1490f, 0.8039f, 1.0f));
        defaultMaterial.setSpecularColor(new Vector4f(0.3500f, 0.3500f, 0.3500f, 1.0f));
        defaultMaterial.getMeshList().add(mesh);
        materialList.add(defaultMaterial);
        return new Model(modelId, materialList);
    }

    public static Model loadModel(String modelId, String modelPath, int flags) {
        File file = new File(modelPath);
        if (!file.exists()) {
            throw new RuntimeException("Model path does not exist [" + modelPath + "]");
        }

        AIScene aiScene = aiImportFile(modelPath, flags);
        if (aiScene == null) {
            throw new RuntimeException("Error loading model [modelPath: " + modelPath + "]");
        }

        int numMaterials = aiScene.mNumMaterials();
        List<Material> materialList = new ArrayList<>();
        for (int i = 0; i < numMaterials; i++) {
            AIMaterial aiMaterial = AIMaterial.create(aiScene.mMaterials().get(i));
            materialList.add(processMaterial(aiMaterial));
        }

        int numMeshes = aiScene.mNumMeshes();
        PointerBuffer aiMeshes = aiScene.mMeshes();
        Material defaultMaterial = new Material();
        for (int i = 0; i < numMeshes; i++) {
            AIMesh aiMesh = AIMesh.create(aiMeshes.get(i));
            Mesh mesh = processMesh(aiMesh);
            int materialIdx = aiMesh.mMaterialIndex();
            Material material;
            if (materialIdx >= 0 && materialIdx < materialList.size()) {
                material = materialList.get(materialIdx);
            } else {
                material = defaultMaterial;
            }
            material.getMeshList().add(mesh);
        }

        if (!defaultMaterial.getMeshList().isEmpty()) {
            materialList.add(defaultMaterial);
        }

        return new Model(modelId, materialList);
    }

    private static int[] processIndices(AIMesh aiMesh) {
        List<Integer> indices = new ArrayList<>();
        int numFaces = aiMesh.mNumFaces();
        AIFace.Buffer aiFaces = aiMesh.mFaces();
        for (int i = 0; i < numFaces; i++) {
            AIFace aiFace = aiFaces.get(i);
            IntBuffer buffer = aiFace.mIndices();
            while (buffer.remaining() > 0) {
                indices.add(buffer.get());
            }
        }
        return indices.stream().mapToInt(Integer::intValue).toArray();
    }

    private static Material processMaterial(AIMaterial aiMaterial) {
        Material material = new Material();

        AIColor4D color = AIColor4D.create();

        int result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_AMBIENT, aiTextureType_NONE, 0,
                color);
        if (result == aiReturn_SUCCESS) {
            material.setAmbientColor(new Vector4f(color.r(), color.g(), color.b(), color.a()));
        }

        result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_DIFFUSE, aiTextureType_NONE, 0,
                color);
        if (result == aiReturn_SUCCESS) {
            material.setDiffuseColor(new Vector4f(color.r(), color.g(), color.b(), color.a()));
        }

        result = aiGetMaterialColor(aiMaterial, AI_MATKEY_COLOR_SPECULAR, aiTextureType_NONE, 0,
                color);
        if (result == aiReturn_SUCCESS) {
            material.setSpecularColor(new Vector4f(color.r(), color.g(), color.b(), color.a()));
        }

        float reflectance = 0.0f;
        float[] shininessFactor = new float[]{0.0f};
        int[] pMax = new int[]{1};
        result = aiGetMaterialFloatArray(aiMaterial, AI_MATKEY_SHININESS_STRENGTH, aiTextureType_NONE, 0, shininessFactor, pMax);
        if (result != aiReturn_SUCCESS) {
            reflectance = shininessFactor[0];
        }
        material.setReflectance(reflectance);

        return material;

    }

    private static Mesh processMesh(AIMesh aiMesh) {
        float[] vertices = processVertices(aiMesh);
        float[] normals = processNormals(aiMesh);
        int[] indices = processIndices(aiMesh);

        return new Mesh(vertices, normals, indices);
    }

    private static float[] processNormals(AIMesh aiMesh) {
        AIVector3D.Buffer buffer = aiMesh.mNormals();
        float[] data = new float[buffer.remaining() * 3];
        int pos = 0;
        while (buffer.remaining() > 0) {
            AIVector3D normal = buffer.get();
            data[pos++] = normal.x();
            data[pos++] = normal.y();
            data[pos++] = normal.z();
        }
        return data;
    }

    private static float[] processVertices(AIMesh aiMesh) {
        AIVector3D.Buffer buffer = aiMesh.mVertices();
        float[] data = new float[buffer.remaining() * 3];
        int pos = 0;
        while (buffer.remaining() > 0) {
            AIVector3D textCoord = buffer.get();
            data[pos++] = textCoord.x();
            data[pos++] = textCoord.y();
            data[pos++] = textCoord.z();
        }
        return data;
    }
}