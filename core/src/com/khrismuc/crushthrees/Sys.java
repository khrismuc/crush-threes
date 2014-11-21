package com.khrismuc.crushthrees;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.math.RandomXS128;

public class Sys {

	public static CrushThrees game;
	public static boolean DEBUG = false;
	public static AssetManager am = new AssetManager();
	public static boolean loading = true;
	public static RandomXS128 random = new RandomXS128();
	
	public static void log(String text) {
		if (DEBUG) System.out.println(text);
	}

}
