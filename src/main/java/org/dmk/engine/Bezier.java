package org.dmk.engine;

import org.joml.Vector3f;

public class Bezier {
    private final Vector3f[][] controlPoints;

    private static final int TRIANGLES_IN_ROW = 12;

    private final Vector3f[][] points;

    private float[] positions;
    private float[] normals;
    private int[] indices;


    public Bezier() {
        this.controlPoints = new Vector3f[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                controlPoints[i][j] = new Vector3f(i * 1f / 3f, j * 1f / 3f, 0);
            }
        }
        points = new Vector3f[TRIANGLES_IN_ROW + 1][TRIANGLES_IN_ROW + 1];
    }

    public void triangulate() {
        calculatePoints();
        positions = new float[(TRIANGLES_IN_ROW + 1) * (TRIANGLES_IN_ROW + 1) * 3];
        normals = new float[(TRIANGLES_IN_ROW + 1) * (TRIANGLES_IN_ROW + 1) * 3];
        indices = new int[TRIANGLES_IN_ROW * TRIANGLES_IN_ROW * 6];

        for (int i = 0; i < TRIANGLES_IN_ROW + 1; i++) {
            for (int j = 0; j < TRIANGLES_IN_ROW + 1; j++) {
                int a = i * (TRIANGLES_IN_ROW + 1) * 3 + j * 3;
                positions[a] = points[i][j].x;
                positions[a + 1] = points[i][j].z;
                positions[a + 2] = points[i][j].y;
                Vector3f normal = calculateNormalVector(points[i][j].x, points[i][j].y);
                normals[a] = normal.x;
                normals[a + 1] = normal.z;
                normals[a + 2] = normal.y;
            }
        }


        int counter = 0;
        for (int i = 0; i < TRIANGLES_IN_ROW; i++) {
            for (int j = 0; j < TRIANGLES_IN_ROW; j++) {
                int a = i * (TRIANGLES_IN_ROW + 1) + j;
                int b = a + 1;
                int c = (i + 1) * (TRIANGLES_IN_ROW + 1) + j;
                int d = c + 1;
                indices[counter++] = a;
                indices[counter++] = b;
                indices[counter++] = c;
                indices[counter++] = c;
                indices[counter++] = b;
                indices[counter++] = d;
            }
        }
        resize(1.5f);
    }

    private void resize(float scale) {
        for(int i = 0; i < positions.length; i++) {
            positions[i] *= scale;
        }
    }

    public float[] getPositions() {
        return positions;
    }

    public float[] getNormals() {
        return normals;
    }

    public int[] getIndices() {
        return indices;
    }

    private void calculatePoints() {
        for (int i = 0; i < TRIANGLES_IN_ROW + 1; i++) {
            for (int j = 0; j < TRIANGLES_IN_ROW + 1; j++) {
                float x = i * 1f / TRIANGLES_IN_ROW;
                float y = j * 1f / TRIANGLES_IN_ROW;
                points[i][j] = new Vector3f(x, y, calculateBezierPoint(x, y));
            }
        }
    }

    private static long factorial(int n) {
        long fact = 1;
        for (int i = 2; i <= n; i++) {
            fact *= i;
        }
        return fact;
    }

    private static long combinations(int n, int i) {
        return factorial(n) / (factorial(i) * factorial(n - i));
    }

    private float bernstein(int i, int n, float t) {
        return (float) (combinations(n, i) * Math.pow(t, i) * Math.pow(1 - t, n - i));
    }

    private float calculateBezierPoint(float x, float y) {
        float z = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                float b = bernstein(i, 3, x) * bernstein(j, 3, y);
                z += controlPoints[i][j].z * b;
            }
        }
        return z;
    }

    private Vector3f calculateDerivativeBezierPointX(float x, float y) {
        Vector3f derivative = new Vector3f(1, 0, 0);
        float z = 0;
        for (int i = 0; i < 4 - 1; i++) {
            for (int j = 0; j < 4; j++) {
                float b = bernstein(i, 3 - 1, x) * bernstein(j, 3, y);
                z += (controlPoints[i + 1][j].z - controlPoints[i][j].z) * b;
            }
        }
        z *= 3;
        derivative.z = z;
        return derivative;
    }

    private Vector3f calculateDerivativeBezierPointY(float x, float y) {
        Vector3f derivative = new Vector3f(0, 1, 0);
        float z = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4 - 1; j++) {
                float b = bernstein(i, 3, x) * bernstein(j, 3 - 1, y);
                z += (controlPoints[i][j + 1].z - controlPoints[i][j].z) * b;
            }
        }
        z *= 3;
        derivative.z = z;
        return derivative;
    }

    private Vector3f calculateNormalVector(float x, float y) {
        Vector3f derivativeX = calculateDerivativeBezierPointX(x, y);
        Vector3f derivativeY = calculateDerivativeBezierPointY(x, y);
        Vector3f normal = new Vector3f();
        normal.cross(derivativeX, derivativeY);
        normal.normalize();
        if (Float.isNaN(normal.x) || Float.isNaN(normal.y) || Float.isNaN(normal.z)) return new Vector3f(0, 0, 1);
        return normal;
    }

    public Vector3f[][] getControlPoints() {
        return controlPoints;
    }
}
