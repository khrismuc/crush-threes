package com.khrismuc.crushthrees;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;

public class StartScreen extends GameScreen {

	Viewport viewport;
	Texture splash;
	boolean done_loading = false;
	SpriteBatch batch;

	public StartScreen() {
		splash = new Texture("startscreen.png");
		viewport = Sys.game.viewport;
		batch = new SpriteBatch();
	}

	@Override
	public void render(float delta) {
		super.render(delta);

		if (!Sys.loading && !done_loading) {
			done_loading = true;
			splash.dispose();
			splash = new Texture("startscreen_loaded.png");
		}

		batch.setTransformMatrix(viewport.getCamera().view);
		batch.setProjectionMatrix(viewport.getCamera().projection);

		float scale = 3f;

		batch.begin();
		float w = splash.getWidth() * scale;
		float h = splash.getHeight() * scale;
		float x = (viewport.getWorldWidth() - w) / 2f;
		float y = (viewport.getWorldHeight() - h) / 2f;
		batch.draw(splash, x, y, w, h);
		batch.end();
		
		if (done_loading)
			Sys.game.setScreen(Sys.game.mainScreen);

	}

	public void onClick(int x, int y) {
		if (Sys.loading)
			return;
		Sys.game.setScreen(Sys.game.mainScreen);
	}

	@Override
	public void dispose() {
		super.dispose();
		splash.dispose();
		batch.dispose();
	}

}
