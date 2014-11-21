package com.khrismuc.crushthrees;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CrushThrees extends Game {

	private final float gameWidth = 480f;
	private final float gameHeight = 800f;

	public final int fieldWidth = 7;
	public final int fieldHeight = 7;
	
	public final int cellSize = (int) (gameWidth / fieldWidth);
	
	public GameScreen currentScreen;
	public GameScreen startScreen;
	public GameScreen mainScreen;
	
	public OrthographicCamera camera;
	public Viewport viewport;

	public CrushThrees() {
		super();
		Sys.game = this;
	}

	@Override
	public void create() {

		// DEBUG mode on
		Sys.DEBUG = true;
		
		camera = new OrthographicCamera(gameWidth, gameHeight);			
		viewport = new FitViewport(gameWidth, gameHeight, camera);
		camera.translate(gameWidth / 2f, gameHeight / 2f);
		camera.update();

		// screens
		startScreen = new StartScreen();
		mainScreen = new MainScreen();

		// assets
		Sys.am.load("threes.png", Texture.class);

		setScreen(startScreen);

	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		viewport.update(width, height);
	}

	public void setScreen(GameScreen screen) {
		super.setScreen(screen);
		currentScreen = (GameScreen) screen;
	}

	boolean leftButtonWasDown = false;

	@Override
	public void render() {
		super.render();

		// check if still loading
		if (Sys.am.update())
			Sys.loading = false;

		// handle input
		boolean leftButtonIsDown = Gdx.input.isButtonPressed(Buttons.LEFT);
		if (leftButtonWasDown && !leftButtonIsDown) {
			Vector3 click = viewport.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0f));
			int x = (int) click.x, y = (int) click.y;
			currentScreen.onClick(x, y);
		}
		leftButtonWasDown = leftButtonIsDown;
	}

	@Override
	public void dispose() {
		super.dispose();
		Sys.am.dispose();
	}

}
