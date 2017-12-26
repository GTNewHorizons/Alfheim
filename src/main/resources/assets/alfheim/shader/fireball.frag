#version 120
precision highp float;
precision highp int;

varying float noise;

uniform sampler2D bgl_RenderedTexture;

float random( vec3 scale, float seed ) {
  return fract( sin( dot( gl_FragCoord.xyz + seed, scale ) ) * 43758.5453 + seed ) ;
}

void main() {
	float brightness = 0.9;
	
    // get a random offset
    float offset = .01 * random( vec3( 12.9898, 78.233, 0151.7182 ), 0.0 );

    // not quite depth, but how bright the sphere is (higher is dimmer)
    float depth = 0.25;

    // lookup vertically in the texture, using noise and offset
    // to get the right RGB colour
    vec2 tPos = vec2(0, (brightness + depth) * noise + offset );
    vec4 color = texture2D(bgl_RenderedTexture, (brightness - depth) - tPos);
    gl_FragColor = vec4( color.rgb, 1.0 );

}