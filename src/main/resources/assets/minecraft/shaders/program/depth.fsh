#version 120

uniform sampler2D DiffuseSampler;
varying vec2 texCoord;

void main(){
    vec4 c = texture2D(DiffuseSampler, texCoord);
    gl_FragColor = vec4(c.r, 0, 0, c.a);
}