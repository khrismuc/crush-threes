package com.khrismuc.crushthrees;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class MainScreen extends GameScreen {

	public Stage mainStage;
	private Field field;

	public MainScreen() {
		
		bgColorB = 0.4f;

		mainStage = new Stage();
		mainStage.setViewport(Sys.game.viewport);

	}

	@Override
	public void show() {
		super.show();
		field = new Field(mainStage, Sys.game.fieldWidth, Sys.game.fieldHeight);
	}

	@Override
	public void render(float delta) {
		super.render(delta);
		mainStage.act();
		field.checkForHolesAndDead();
		mainStage.draw();
	}

	public void onClick(int x, int y) {
		Actor t = mainStage.hit(x, y, false);
		if (t != null) field.activate((Three) t);
	}

	@Override
	public void dispose() {
		super.dispose();
		field.dispose();
		mainStage.dispose();
	}
}
