package com.khrismuc.crushthrees;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

public class Ball extends Actor {

	TextureRegion ball, light;
	private float speed = 200f, xM, yM;
	private float initialMovementAngle;
	private float rotationSpeed = 500f;

	public Ball() {
		this.setPosition(160f, 120f);
		Texture t = Sys.am.get("ball.png", Texture.class);
		ball = new TextureRegion(t, 0, 0, 32, 32);
		light = new TextureRegion(t, 32, 0, 32, 32);

		initialMovementAngle = Sys.random.nextInt(360);
		xM = MathUtils.cosDeg(initialMovementAngle) * speed;
		yM = MathUtils.sinDeg(initialMovementAngle) * speed;
		setScale((Sys.random.nextInt(10) + 8f) / 12f);
		setScale(2f);
		setSize(ball.getRegionWidth(), ball.getRegionHeight());
		setOrigin(Align.center);
	}

	@Override
	public void act(float delta) {
		// gravity
		yM -= 500 * delta;
		moveBy(xM * delta, yM * delta);

		if (getX() < 0f || getX() > Sys.game.viewport.getWorldWidth() - getWidth()) {
			xM = -0.8f * xM;
			moveBy(xM * 2f * delta, 0f);
			rotationSpeed = -rotationSpeed;
		}
		if (getY() < 0f || getY() > Sys.game.viewport.getWorldHeight() - getHeight()) {
			yM = -0.82f * yM;
			if (getY() < 0f && yM < 70f) {
				yM = 0f;
				setY(0f);
			}
			moveBy(0f, yM * 2f * delta);
			if (rotationSpeed * xM > 0)
				rotationSpeed = -rotationSpeed;
			if (yM == 0f) {
				xM = 0.992f * xM;
				rotationSpeed = (360f * -xM) / (32f * getScaleX() * MathUtils.PI);
			}

		}
		rotateBy(rotationSpeed * delta);

		if (xM * xM + yM * yM < 3f) {
			xM = 0f;
			yM = 0f;
		}
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		float x = getX() - getWidth() / 2f;
		float y = getY() - getHeight() / 2f;

		x = getX();
		y = getY();

		batch.draw(ball, x, y, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(),
				getRotation());
		batch.draw(light, x, y, getOriginX(), getOriginY(), getWidth(), getHeight(), getScaleX(), getScaleY(), 0f);
	}

}
