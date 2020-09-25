#version 120
uniform sampler2D tex;
uniform float fogDensity;
uniform vec3 fogColor;

varying vec4 fragPos;
varying vec4 view;
varying vec2 texCoord;

void main() {
	float dist = length(view);
	float fog = exp(- dist * fogDensity);
	fog = clamp(fog, 0.0, 1.0);
	
	gl_FragColor = texture2D(tex, texCoord);
	gl_FragColor = mix(vec4(fogColor, 1.0), gl_FragColor, vec4(fog));
}	