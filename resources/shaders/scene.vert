#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec3 normal;

out vec3 outPosition;
out vec3 outNormal;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

void main()
{
    mat4 modelViewMatrix = viewMatrix * modelMatrix;
    vec4 mvPosition =  modelViewMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * mvPosition;
    outPosition = mvPosition.xyz;
    outNormal = normalize(mat3(transpose(inverse(modelMatrix))) * normal);
}