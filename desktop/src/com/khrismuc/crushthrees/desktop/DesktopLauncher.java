package com.khrismuc.crushthrees.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.khrismuc.crushthrees.CrushThrees;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "Crush Threes";
		config.width = 480;
		config.height = 800;
		config.resizable = false;
		config.fullscreen = false;
		config.addIcon("bin/ic_launcher.png", FileType.Internal);
		
		new LwjglApplication(new CrushThrees(), config);
	}
}
