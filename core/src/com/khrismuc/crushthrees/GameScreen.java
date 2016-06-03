package com.khrismuc.crushthrees;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

public abstract class GameScreen implements Screen {

	float bgColorR = 0f, bgColorG = 0f, bgColorB = 0f;

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(bgColorR, bgColorG, bgColorB, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

	public void onClick(int x, int y) {	
	}

}
