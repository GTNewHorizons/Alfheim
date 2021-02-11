#version 120

precision highp float;
precision highp int;

uniform float ftime;
uniform sampler2D explosion;
uniform sampler2D gravel;

const float smoothness = 50.0;
const float lacunarity = 2.0;
const float gain = 0.5;
const float magnatude = 1.1;

vec2 mBBS(vec2 val, float modulus) {
    val = mod(val, modulus);// For numerical consistancy.
    return mod(val * val, modulus);
}
float mnoise (vec3 pos) {
    float intArg = floor(pos.z);
    float fracArg = fract(pos.z);
    vec2 hash = mBBS(intArg * 3.0 + vec2(0, 3), smoothness);
    vec4 g = vec4 (
    texture2D(gravel, vec2(pos.x, pos.y + hash.x)/smoothness).xy,
    texture2D(gravel, vec2(pos.x, pos.y + hash.y)/smoothness).xy
    ) * 2.0 - 1.0;
    return mix(
    g.x + g.y * fracArg,
    g.z + g.w * (fracArg - 1.0),
    smoothstep(0.0, 1.0, fracArg)
    );
}

float turbulence(vec3 pos) {
    float sum  = 0.0;
    float freq = 1.0;
    float amp  = 1.0;
    for (int i = 0; i < 4; i++) {
        sum += abs(mnoise(pos * freq)) * amp;
        freq *= lacunarity;
        amp *= gain;
    }
    return sum;
}

vec4 sampleFire(vec3 loc, vec4 scale) {
    loc.xz = loc.xz * 2.0 - 1.0;
    vec2 st = vec2(sqrt(dot(loc.xz, loc.xz)), loc.y);
    loc.y -= ftime * scale.w;// Scrolling noise upwards over time.
    loc *= scale.xyz;// Scaling noise space.
    float offset = sqrt(st.y) * magnatude * turbulence(loc);
    st.y += offset;
    if (st.y > 1.0)
    return vec4(0, 0, 0, 1);
    vec4 result = texture2D(explosion, st);
    if (st.y < 0.1)
    result *= st.y / 0.1;
    return result;
}


void main(void) {
    vec2 uv = vec2(gl_TexCoord[0]);
    vec3 uvInput = vec3(uv.x - 0.5, uv.y, 0.0);
    vec3 color = sampleFire(uvInput, vec4(1.0, 2.0, 1.0, 0.5)).rgb * 1.5;
    gl_FragColor = vec4(color, color.r);
}