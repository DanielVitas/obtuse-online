package com.obtuse.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		// libGDX 1.9.8 defaults to an RGB565 surface with no alpha (r=5, g=6, b=5, a=0).
		// Phone GPUs offer that EGL config; the emulator's EGL does not, so config
		// selection returns nothing usable and eglCreateContext throws
		// IllegalArgumentException on the GL thread before create() ever runs.
		// RGBA8888 exists everywhere, and costs a little bandwidth for less banding.
		config.r = 8;
		config.g = 8;
		config.b = 8;
		config.a = 8;
		config.depth = 16;
		config.stencil = 0;
		config.numSamples = 0;
		initialize(new Obtuse(), config);
	}
}
