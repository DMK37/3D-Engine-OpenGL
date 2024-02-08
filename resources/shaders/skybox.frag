#version 330

out vec4 fragColor;

in vec3 outPosition;
in vec3 outNormal;

uniform vec4 diffuse;

struct Fog
{
    int activeFog;
    vec3 color;
    float density;
};

struct AmbientLight
{
    float factor;
    vec3 color;
};

struct DirLight
{
    vec3 color;
    vec3 direction;
    float intensity;
};

uniform AmbientLight ambientLight;
uniform Fog fog;
uniform DirLight dirLight;


vec4 calcLightColor(vec4 diffuse, vec3 lightColor, float light_intensity, vec3 position, vec3 to_light_dir, vec3 normal) {
    vec4 diffuseColor = vec4(0, 0, 0, 1);

    // Diffuse Light
    float diffuseFactor = max(dot(normal, to_light_dir), 0.0);
    diffuseColor = diffuse * vec4(lightColor, 1.0) * light_intensity * diffuseFactor;

    return diffuseColor;
}

vec4 calcDirLight(vec4 diffuse, DirLight light, vec3 position, vec3 normal) {
    return calcLightColor(diffuse, light.color, light.intensity, position, normalize(light.direction), normal);
}


vec4 calcFog(vec3 pos, vec4 color, Fog fog, vec3 ambientLight, DirLight dirLight) {
    vec3 fogColor = fog.color * (ambientLight + dirLight.color * dirLight.intensity);
    float distance = length(pos);
    float fogFactor = 1.0 / exp((distance * fog.density) * (distance * fog.density));
    fogFactor = clamp(fogFactor, 0.0, 1.0);

    vec3 resultColor = mix(fogColor, color.xyz, fogFactor);
    return vec4(resultColor.xyz, color.w);
}

void main()
{
    fragColor = calcDirLight(diffuse, dirLight, outPosition, outNormal);

    if(fog.activeFog == 1)
    {
        fragColor = calcFog(outPosition, fragColor, fog, ambientLight.color, dirLight);
    }
    else
    {
        fragColor = diffuse;
    }
}